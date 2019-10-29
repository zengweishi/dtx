package cn.itcast.dtx.txmsgdemo.bank2.listener;

import cn.itcast.dtx.txmsgdemo.bank2.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank2.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description:消费者监听消息
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 10:57
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "topic_txmsg",consumerGroup = "consumer_txmsg_group_bank2")
public class TxmsgConsumer implements RocketMQListener<String> {
    @Autowired
    private AccountInfoService accountInfoService;

    private Logger logger = LoggerFactory.getLogger(TxmsgConsumer.class);

    @Override
    public void onMessage(String s) {
        logger.info("消费者开始监听消息：{}",s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(jsonObject.getString("accountChangeEvent"), AccountChangeEvent.class);
        accountChangeEvent.setAccountNo("2");
        accountInfoService.updateAccount(accountChangeEvent);
    }
}
