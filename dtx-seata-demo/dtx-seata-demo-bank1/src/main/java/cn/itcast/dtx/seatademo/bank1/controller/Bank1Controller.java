package cn.itcast.dtx.seatademo.bank1.controller;

import cn.itcast.dtx.seatademo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 *      1. 用户服务的 TM 向 TC 申请开启一个全局事务，全局事务创建成功并生成一个全局唯一的XID。
 *      2. 用户服务的 RM 向 TC 注册 分支事务，该分支事务在用户服务执行新增用户逻辑，并将其纳入 XID 对应全局 事务的管辖。
 *      3. 用户服务执行分支事务，向用户表插入一条记录。
 *      4. 逻辑执行到远程调用积分服务时(XID 在微服务调用链路的上下文中传播)。积分服务的RM 向 TC 注册分支事务，该分支事务执行增加积分的逻辑，并将其纳入 XID 对应全局事务的管辖。
 *      5. 积分服务执行分支事务，向积分记录表插入一条记录，执行完毕后，返回用户服务。
 *      6. 用户服务分支事务执行完毕。
 *      7. TM 向 TC 发起针对 XID 的全局提交或回滚决议。
 *      8. TC 调度 XID 下管辖的全部分支事务完成提交或回滚请求。
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 15:34
 */
@RestController
public class Bank1Controller {
    @Autowired
    AccountInfoService accountInfoService;

    @RequestMapping("/transfer")
    public String transfer(Double amount) {
        accountInfoService.updateAccount(amount);
        return "bank1"+amount;
    }

}
