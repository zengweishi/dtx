package cn.itcast.dtx.seatademo.bank1.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 15:42
 */
@FeignClient(value = "seata-demo-bank2",fallback = Bank2ClientFallBack.class)
public interface Bank2Client {
    @RequestMapping("/bank2/transfer")
    public String transfer(@RequestParam("amount") Double amount);
}
