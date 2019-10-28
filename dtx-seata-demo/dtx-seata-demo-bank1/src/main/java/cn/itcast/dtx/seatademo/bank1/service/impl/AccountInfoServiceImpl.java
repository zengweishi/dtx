package cn.itcast.dtx.seatademo.bank1.service.impl;

import cn.itcast.dtx.seatademo.bank1.client.Bank2Client;
import cn.itcast.dtx.seatademo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.seatademo.bank1.entity.AccountInfo;
import cn.itcast.dtx.seatademo.bank1.service.AccountInfoService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/10/28 15:35
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    Bank2Client bank2Client;

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void updateAccount(Double amount) {
        logger.info("********* Bank1 begin,xid:{}", RootContext.getXID());
        //A扣减金额
        int i = accountInfoDao.updateAccountBalance("1", -1 * amount);
        //向B转账
        String transfer = bank2Client.transfer(amount);
        if (transfer.equals("fallback")) {
            throw new RuntimeException("下游服务调用失败！");
        }
        //认为制造异常
        if(amount == 3) {
            throw new RuntimeException("转账金额不能为3");
        }
    }
}
