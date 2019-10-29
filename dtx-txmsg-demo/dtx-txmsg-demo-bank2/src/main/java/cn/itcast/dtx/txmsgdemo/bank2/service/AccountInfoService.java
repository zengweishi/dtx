package cn.itcast.dtx.txmsgdemo.bank2.service;

import cn.itcast.dtx.txmsgdemo.bank2.model.AccountChangeEvent;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 10:54
 */
public interface AccountInfoService {
    void updateAccount(AccountChangeEvent accountChangeEvent);
}
