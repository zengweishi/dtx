package cn.itcast.dtx.notifydemo.bank1.listener;

import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import cn.itcast.dtx.notifydemo.bank1.model.AccountChangeEvent;
import cn.itcast.dtx.notifydemo.bank1.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description:监听充值系统的消息
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 14:41
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "topic_notifymsg",consumerGroup = "consumer_group_notifymsg_bank1")
public class NotifymsgListener implements RocketMQListener<AccountPay> {
    private Logger logger = LoggerFactory.getLogger(NotifymsgListener.class);

    @Autowired
    private AccountInfoService accountInfoService;

    @Override
    public void onMessage(AccountPay accountPay) {
        logger.info("开始监听消息！");
        AccountChangeEvent accountChangeEvent = new AccountChangeEvent();
        accountChangeEvent.setAccountNo(accountPay.getAccountNo());
        accountChangeEvent.setAmount(accountPay.getPayAmount());
        accountChangeEvent.setTxNo(accountPay.getId());
        accountInfoService.updateAccount(accountChangeEvent);
        logger.info("消息监听处理完成！");
    }
}
