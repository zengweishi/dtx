package cn.itcast.dtx.notifydemo.bank1.client;

import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import org.springframework.stereotype.Component;

/**
 * description:远程调用失败降级处理
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 14:55
 */
@Component
public class PayFallBack implements PayClient {
    @Override
    public AccountPay getPayResult(String txNo) {
        AccountPay accountPay = new AccountPay();
        accountPay.setResult("fail");
        return accountPay;
    }
}
