package cn.itcast.seatademo.bank2.service.impl;

import cn.itcast.seatademo.bank2.dao.AccountInfoDao;
import cn.itcast.seatademo.bank2.entity.AccountInfo;
import cn.itcast.seatademo.bank2.service.AccountInfoService;
import io.seata.core.context.RootContext;
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
 * @date 2019/10/28 15:53
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoDao accountInfoDao;

    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccount(Double amount) {
        logger.info("******** Bank2 begin,xid:{}", RootContext.getXID());
        int i = accountInfoDao.updateAccountBalance("2", amount);
        //人为制造异常
        if (amount == 2) {
            throw new RuntimeException("转账金额不允许为2！");
        }
    }
}
