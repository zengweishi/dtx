package cn.itcast.dtx.txmsgdemo.bank1.service;

import cn.itcast.dtx.txmsgdemo.bank1.model.AccountChangeEvent;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 9:49
 */
public interface AccountInfoService {
    void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

    void doUpdateAccount(AccountChangeEvent accountChangeEvent);
}
