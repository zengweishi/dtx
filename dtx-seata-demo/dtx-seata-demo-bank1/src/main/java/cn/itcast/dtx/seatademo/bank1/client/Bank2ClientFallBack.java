package cn.itcast.dtx.seatademo.bank1.client;

import org.springframework.stereotype.Component;

/**
 * description:远程服务调用失败的降级处理
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 15:43
 */
@Component
public class Bank2ClientFallBack implements Bank2Client {

    @Override
    public String transfer(Double amount) {
        return "fallback";
    }
}
