package cn.itcast.dtx.notifydemo.bank1.controller;

import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import cn.itcast.dtx.notifydemo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 15:31
 */
@RestController
public class Bank1Controller {
    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * description: 测试主动查询充值结果
     * @author weishi.zeng
     * @date 2019/10/29 15:39
     * @param [txNo]
     * @return cn.itcast.dtx.notifydemo.bank1.entity.AccountPay
     */
    @RequestMapping("/getPayResult/{txNo}")
    private AccountPay getPayResult(@PathVariable("txNo") String txNo) {
        return accountInfoService.queryPayResult(txNo);
    }
}
