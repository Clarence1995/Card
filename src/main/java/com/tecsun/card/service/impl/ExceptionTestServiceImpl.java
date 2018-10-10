package com.tecsun.card.service.impl;

import com.tecsun.card.dao.card.BusApplyDao;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.beans.Transient;

/**
 * @author 0214
 * @createTime 2018/9/26
 * @description
 */
@Service("exceptionTest")
public class ExceptionTestServiceImpl {
    @Autowired
    private CardService cardService;

    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public void test() {
        for (int i = 0; i < 10; i++) {
            BusApplyPO busApplyPO = new BusApplyPO();
            busApplyPO.setId(0L);
            busApplyPO.setPersonId(233);
            busApplyPO.setBusinessType(Constants.BUS_APLY_BUSINESSTYPE_NEW_APPLY);
            busApplyPO.setStatus(Constants.BUS_APPLY_STATUS_APPLY);
            busApplyPO.setRegionalId(Constants.USUAL_ADDRESS_CODE);
            try {
                if (i == 5) {
                    busApplyPO.setStatus("233");
                }
                cardService.insertBusApply(busApplyPO);
            } catch (Exception e) {
                System.out.println("第" + i + "次出现异常,原因: " + e);
                continue;
            }
        }

    }

}
