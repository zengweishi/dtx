package cn.itcast.dtx.tccdemo.bank1.client;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 17:05
 */
@FeignClient(value = "tcc-demo-bank2",fallback = Bank2ClientFallback.class)
public interface Bank2Client {
    @RequestMapping("/bank2/transfer")
    @Hmily
    String transfer(@RequestParam("amount") Double amount);
}
