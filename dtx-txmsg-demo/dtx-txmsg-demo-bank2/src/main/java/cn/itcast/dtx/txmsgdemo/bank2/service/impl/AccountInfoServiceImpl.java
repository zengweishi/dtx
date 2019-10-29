package cn.itcast.dtx.txmsgdemo.bank2.service.impl;

import cn.itcast.dtx.txmsgdemo.bank2.dao.AccountInfoDao;
import cn.itcast.dtx.txmsgdemo.bank2.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank2.service.AccountInfoService;
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
 * @date 2019/10/29 10:54
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoDao accountInfoDao;

    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Override
    public void updateAccount(AccountChangeEvent accountChangeEvent) {
        logger.info("消费者执行事务，事务号：{}",accountChangeEvent.getTxNo());
        //幂等校验
        if (accountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            logger.info("请勿重复消费，事务号：{}",accountChangeEvent.getTxNo());
            return;
        }
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        logger.info("更新本地事务，消费成功，事务号：{}",accountChangeEvent.getTxNo());

    }
}
