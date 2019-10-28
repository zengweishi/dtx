package cn.itcast.dtx.tccdemo.bank1.service.impl;

import cn.itcast.dtx.tccdemo.bank1.client.Bank2Client;
import cn.itcast.dtx.tccdemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.tccdemo.bank1.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.bean.context.HmilyTransactionContext;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 17:05
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountInfoService {
    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountInfoDao accountInfoDao;
    @Autowired
    private Bank2Client bank2Client;

    /**
     * description: try
     * @author weishi.zeng
     * @date 2019/10/28 17:17
     * @param [amount]
     * @return void
     */
    @Override
    @Hmily(confirmMethod = "commit",cancelMethod = "rollback")
    @Transactional(rollbackFor = Exception.class)
    public void updateAccount(Double amount) {
        //获取事务ID
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        logger.info("********* Bank1 开始 try:"+transId);
        int existTry = accountInfoDao.isExistTry(transId);
        //幂等校验
        if (existTry > 0) {
            logger.info("************ Bank1 try 已经执行，无需重复执行，事务id：{}",transId);
            return;
        }
        //悬挂处理
        if (accountInfoDao.isExistCancel(transId) > 0 || accountInfoDao.isExistConfirm(transId) > 0) {
            logger.info("************ Bank1 已经执行confirm或cancel，悬挂处理，事务id：{}",transId);
            return;
        }
        //账户扣减
        if(accountInfoDao.subtractAccountBalance("1",amount) <= 0) {
            throw new HmilyRuntimeException("Bank1扣减失败，事务id:"+transId);
        }
        //扣减成功，增加try记录
        accountInfoDao.addTry(transId);
        //远程调用增加金额
        String transfer = bank2Client.transfer(amount);
        if (transfer.equals("fallback")) {
            throw new HmilyRuntimeException("Bank2异常，事务id:"+transId);
        }

        //人为制造异常
        if (amount == 3) {
            throw new HmilyRuntimeException("人为制造异常，amount不能为3，事务id:"+transId);
        }
        logger.info("********* Bank1 结束 try:"+transId);

    }

    /**
     * description: confirm
     * @author weishi.zeng
     * @date 2019/10/28 17:17
     * @param []
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    public void commit(Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        logger.info("************ Bank1 开始执行 confirm，事务id：{}",transId);
    }

    /**
     * description: cancel
     * @author weishi.zeng
     * @date 2019/10/28 17:17
     * @param []
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    public void rollback(Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        logger.info("************ Bank1 开始执行 cancel，事务id：{}",transId);
        //空回滚处理
        if (accountInfoDao.isExistTry(transId) <= 0) {
            logger.info("************ Bank1 try 失败，无需执行cancel，事务id：{}",transId);
            return;
        }
        //幂等校验
        if (accountInfoDao.isExistCancel(transId) > 0) {
            logger.info("************ Bank1 已经执行cancel，无需重复执行，事务id：{}",transId);
            return;
        }
        //回滚金额
        int i = accountInfoDao.subtractAccountBalance("1", amount);
        accountInfoDao.addCancel(transId);
        logger.info("************ Bank1 结束执行 cancel，事务id：{}",transId);

    }
}
