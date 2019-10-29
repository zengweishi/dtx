package cn.itcast.dtx.notifymsg.pay.service.impl;

import cn.itcast.dtx.notifymsg.pay.dao.AccountPayDao;
import cn.itcast.dtx.notifymsg.pay.entity.AccountPay;
import cn.itcast.dtx.notifymsg.pay.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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
 * @date 2019/10/29 14:26
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);
    @Autowired
    private AccountPayDao accountPayDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * description: 充值并发送消息
     * @author weishi.zeng
     * @date 2019/10/29 14:39
     * @param [accountPay]
     * @return cn.itcast.dtx.notifymsg.pay.entity.AccountPay
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountPay updateAccount(AccountPay accountPay) {
        logger.info("****** 开始充值!");
        int result = accountPayDao.insertAccountPay(accountPay.getId(), accountPay.getAccountNo(), accountPay.getPayAmount(), "success");
        if (result > 0) {
            rocketMQTemplate.convertAndSend("topic_notifymsg",accountPay);
            return accountPay;
        }
        return null;
    }

    /**
     * description: 给账户系统主动查询充值结果
     * @author weishi.zeng
     * @date 2019/10/29 15:03
     * @param [txNo]
     * @return cn.itcast.dtx.notifymsg.pay.entity.AccountPay
     */
    @Override
    public AccountPay getPayResult(String txNo) {
        AccountPay payResult = accountPayDao.findByIdTxNo(txNo);
        return payResult;
    }
}
