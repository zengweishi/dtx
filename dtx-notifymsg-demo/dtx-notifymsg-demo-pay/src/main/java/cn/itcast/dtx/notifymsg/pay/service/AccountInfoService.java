package cn.itcast.dtx.notifymsg.pay.service;

import cn.itcast.dtx.notifymsg.pay.entity.AccountPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 14:26
 */
public interface AccountInfoService {
    AccountPay updateAccount(AccountPay accountPay);

    AccountPay getPayResult(String txNo);
}
