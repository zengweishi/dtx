package cn.itcast.dtx.notifydemo.bank1.client;

import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/29 14:54
 */
@FeignClient(value = "dtx-notifymsg-demo-pay",fallback = PayFallBack.class)
public interface PayClient {
    @RequestMapping("/pay/getPayResult/{txNo}")
    public AccountPay getPayResult(@PathVariable("txNO") String txNo);
}
