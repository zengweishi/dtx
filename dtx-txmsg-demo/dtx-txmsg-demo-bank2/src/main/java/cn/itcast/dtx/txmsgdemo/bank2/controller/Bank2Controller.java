package cn.itcast.dtx.txmsgdemo.bank2.controller;

import cn.itcast.dtx.txmsgdemo.bank2.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 10:54
 */
@RestController
public class Bank2Controller {
    @Autowired
    private AccountInfoService accountInfoService;

}
