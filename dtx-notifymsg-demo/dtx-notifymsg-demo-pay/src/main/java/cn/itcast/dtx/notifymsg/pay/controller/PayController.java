package cn.itcast.dtx.notifymsg.pay.controller;

import cn.itcast.dtx.notifymsg.pay.entity.AccountPay;
import cn.itcast.dtx.notifymsg.pay.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * description:
 *      （首先账户系统请求充值系统进行充值）
 *      1.充值系统进行充值，然后发送消息到账户系统
 *      2.账户系统监听消息，监听到消息后修改账户
 *      3.若长时间没有监听到消息，那么主动查询充值系统的充值结果
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 14:25
 */
@RestController
public class PayController {
    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * description: 充值
     * @author weishi.zeng
     * @date 2019/10/29 15:04
     * @param [accountPay]
     * @return cn.itcast.dtx.notifymsg.pay.entity.AccountPay
     */
    @RequestMapping("transfer")
    public AccountPay transfer(AccountPay accountPay) {
        //生成事务号
        String txNo = UUID.randomUUID().toString();
        accountPay.setId(txNo);
        return accountInfoService.updateAccount(accountPay);
    }

    /**
     * description: 给账户系统主动查询充值结果
     * @author weishi.zeng
     * @date 2019/10/29 15:04
     * @param [txNo]
     * @return cn.itcast.dtx.notifymsg.pay.entity.AccountPay
     */
    @RequestMapping("/getPayResult/{txNo}")
    public AccountPay getPayResult(@PathVariable("txNO") String txNo) {
        return accountInfoService.getPayResult(txNo);
    }

}
