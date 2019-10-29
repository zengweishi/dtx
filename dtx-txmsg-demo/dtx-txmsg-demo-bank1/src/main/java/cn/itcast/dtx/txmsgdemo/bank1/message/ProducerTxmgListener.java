package cn.itcast.dtx.txmsgdemo.bank1.message;

import cn.itcast.dtx.txmsgdemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.txmsgdemo.bank1.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank1.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * description:MQ回调生产者
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 10:18
 */
@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_bank1")
public class ProducerTxmgListener implements RocketMQLocalTransactionListener {
    private Logger logger = LoggerFactory.getLogger(ProducerTxmgListener.class);
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountInfoDao accountInfoDao;
    /**
     * description: 执行本地事务
     * @author weishi.zeng
     * @date 2019/10/29 10:19
     * @param [message, o]
     * @return org.apache.rocketmq.spring.core.RocketMQLocalTransactionState
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            String jsonString = new String((byte[]) message.getPayload());
            System.out.println("====================="+jsonString+"*********************");
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            AccountChangeEvent accountChangeEvent = JSONObject.parseObject(jsonObject.getString("accountChangeEvent"), AccountChangeEvent.class);
            accountInfoService.doUpdateAccount(accountChangeEvent);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            logger.error("事务执行失败，e:{}",e);
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * description: 回查事务状态
     * @author weishi.zeng
     * @date 2019/10/29 10:19
     * @param [message]
     * @return org.apache.rocketmq.spring.core.RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        //首先获取事务号
        String jsonString = new String((byte[]) message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(jsonObject.getString("accountChangeEvent"), AccountChangeEvent.class);
        String txNo = accountChangeEvent.getTxNo();
        if (accountInfoDao.isExistTx(txNo) <= 0){
            //执行本地事务的时候才会插入事务号，此时说明该本地事务没有执行
            return RocketMQLocalTransactionState.UNKNOWN;
        } else {
            //数据库中事务号存在，说明本地事务已执行
            return RocketMQLocalTransactionState.COMMIT;
        }
    }
}
