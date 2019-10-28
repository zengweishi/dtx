package cn.itcast.dtx.tccdemo.bank1.controller;

import cn.itcast.dtx.tccdemo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 17:04
 */
@RestController
public class Bank1Controller {
    @Autowired
    private AccountInfoService accountInfoService;

    @RequestMapping("transfer")
    public String transfer(@RequestParam("amount") Double amount) {
        accountInfoService.updateAccount(amount);
        return "bank1:"+amount;
    }
}
