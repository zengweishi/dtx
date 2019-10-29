package cn.itcast.dtx.notifydemo.bank1.service.impl;

import cn.itcast.dtx.notifydemo.bank1.client.PayClient;
import cn.itcast.dtx.notifydemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import cn.itcast.dtx.notifydemo.bank1.model.AccountChangeEvent;
import cn.itcast.dtx.notifydemo.bank1.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 14:40
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);
    @Autowired
    private AccountInfoDao accountInfoDao;
    @Autowired
    private PayClient payClient;

    @Override
    public void updateAccount(AccountChangeEvent accountChangeEvent) {
        logger.info("开始处理账户本地事务");
        //幂等校验
        if (accountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            logger.warn("请勿重复处理！");
            return;
        }
        //更新金额与事务记录
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        logger.info("账户事务处理完毕！");
    }

    /**
     * description: 若未接收到通知，则主动查询充值系统
     * @author weishi.zeng
     * @date 2019/10/29 14:50
     * @param
     * @return
     */
    @Override
    public AccountPay queryPayResult(String txNo) {
        AccountPay accountPay = new AccountPay();
        accountPay.setId(txNo);
        AccountPay payResult = payClient.getPayResult(txNo);
        //如果充值系统已经充值成功
        if (payResult.getResult().equals("success")) {
            AccountChangeEvent accountChangeEvent = new AccountChangeEvent();
            accountChangeEvent.setAmount(payResult.getPayAmount());
            accountChangeEvent.setTxNo(payResult.getId());
            accountChangeEvent.setAccountNo(payResult.getAccountNo());
            updateAccount(accountChangeEvent);
        }
        return accountPay;
    }
}
