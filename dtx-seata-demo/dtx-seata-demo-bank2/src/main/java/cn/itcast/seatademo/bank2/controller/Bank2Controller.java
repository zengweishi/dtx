package cn.itcast.seatademo.bank2.controller;

import cn.itcast.seatademo.bank2.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 15:52
 */
@RestController
public class Bank2Controller {
    @Autowired
    private AccountInfoService accountInfoService;

    @RequestMapping("/transfer")
    public String transfer(Double amount) {
        accountInfoService.updateAccount(amount);
        return "bank2"+amount;
    }

}
