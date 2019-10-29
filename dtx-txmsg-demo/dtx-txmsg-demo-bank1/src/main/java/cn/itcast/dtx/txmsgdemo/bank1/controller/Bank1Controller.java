package cn.itcast.dtx.txmsgdemo.bank1.controller;

import cn.itcast.dtx.txmsgdemo.bank1.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * description:
 *      1.发送消息到MQ，此消息不可被订阅者消费
 *      2.生产者接收ack确认，执行本地事务
 *      3.再次发送消息到MQ，可被订阅者消费，若MQ没有接收到消息，会回查事务状态
 *      4.订阅者消费消息
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 9:48
 */
@RestController
public class Bank1Controller {
    @Autowired
    private AccountInfoService accountInfoService;

    @RequestMapping("transfer")
    public String transfer(@RequestParam("amount") Double amount) {
        //产生一个事务号
        String txNo = UUID.randomUUID().toString();
        AccountChangeEvent accountChangeEvent = new AccountChangeEvent("1", amount,txNo);
        accountInfoService.sendUpdateAccountBalance(accountChangeEvent);
        return "转账成功";
    }
}
