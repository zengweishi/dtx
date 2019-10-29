package cn.itcast.dtx.txmsgdemo.bank1.service.impl;

import cn.itcast.dtx.txmsgdemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.txmsgdemo.bank1.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank1.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 9:50
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * description: 发送消息
     * @author weishi.zeng
     * @date 2019/10/29 10:00
     * @param [accountChangeEvent]
     * @return void
     */
    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        //构建消息体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChangeEvent",accountChangeEvent);
        Message<String> message = MessageBuilder.withPayload(jsonObject.toJSONString()).build();
        //top名称消费者与生产者要一致才能监听到对应的消息 -n 127.0.0.1:9876 autoCreateTopicEnable=true
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1", "topic_txmsg", message, null);
        logger.info("***send message body={},result={}",message.getPayload(),transactionSendResult);
    }

    /**
     * description: 执行本地事务
     * @author weishi.zeng
     * @date 2019/10/29 10:34
     * @param [accountChangeEvent]
     * @return void
     */
    @Override
    public void doUpdateAccount(AccountChangeEvent accountChangeEvent) {
        logger.info("开始执行本地事务，事务号：{}",accountChangeEvent.getTxNo());
        //人为制造异常
        if (accountChangeEvent.getAmount() == 3) {
            throw new RuntimeException("人为制造异常,转账金额不能为3！");
        }
        accountInfoDao.updateAccountBalance("1",-1*accountChangeEvent.getAmount());
        //添加事务号，为幂等校验做准备
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        logger.info("结束执行本地事务，事务号：{}",accountChangeEvent.getTxNo());
    }
}
