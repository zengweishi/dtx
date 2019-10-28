package cn.itcast.dtx.tccdemo.bank2.service.impl;

import cn.itcast.dtx.tccdemo.bank2.dao.AccountInfoDao;
import cn.itcast.dtx.tccdemo.bank2.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
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
 * @date 2019/10/28 17:52
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoDao accountInfoDao;

    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    /**
     * description: try
     * @author weishi.zeng
     * @date 2019/10/28 17:58
     * @param [amount]
     * @return void
     */
    @Override
    @Hmily(confirmMethod = "commit",cancelMethod = "cancel")
    @Transactional(rollbackFor = Exception.class)
    public void updateAccount(Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        logger.info("************ Bank2 开始执行 try，事务id：{}",transId);
    }

    /**
     * description: confirm
     * @author weishi.zeng
     * @date 2019/10/28 18:01
     * @param [amount]
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    public void commit(Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        //幂等判断
        if (accountInfoDao.isExistConfirm(transId) > 0) {
            logger.info("************ Bank2 已经执行 confirm，无需重复执行，事务id：{}",transId);
            return;
        }
        //增加金额
        accountInfoDao.addAccountBalance("2",amount);
        //增加日志记录
        accountInfoDao.addConfirm(transId);
    }

    /**
     * description: cancel
     * @author weishi.zeng
     * @date 2019/10/28 18:01
     * @param [amount]
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        logger.info("************ Bank2 开始执行 cancel，事务id：{}",transId);
    }
}
