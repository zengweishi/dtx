package cn.itcast.dtx.notifydemo.bank1.service;

import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import cn.itcast.dtx.notifydemo.bank1.model.AccountChangeEvent;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 14:40
 */
public interface AccountInfoService {
    void updateAccount(AccountChangeEvent accountChangeEvent);

    AccountPay queryPayResult(String txNo);
}
