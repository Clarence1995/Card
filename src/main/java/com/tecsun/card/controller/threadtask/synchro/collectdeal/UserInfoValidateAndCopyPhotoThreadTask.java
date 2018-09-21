package com.tecsun.card.controller.threadtask.synchro.collectdeal;

import com.tecsun.card.common.Constants;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.MidService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserInfoValidateAndCopyPhotoThreadTask implements Runnable {

    private static final Logger logger = Logger.getLogger(UserInfoValidateAndCopyPhotoThreadTask.class);
    // 采集库service类
    private CollectService collectService;
    // 中间库service类(存放公安照片)
    private MidService midService;
    // 正式库service类
    private CardService cardService;

    private List<BasicPersonInfoPO> list;
    public UserInfoValidateAndCopyPhotoThreadTask(){}


    public UserInfoValidateAndCopyPhotoThreadTask (CollectService collectService,
                                                   MidService midService,
                                                   CardService cardService,
                                                   List<BasicPersonInfoPO> list) {
        this.collectService = collectService;
        this.midService = midService;
        this.cardService = cardService;
        this.list = list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void run () {
        try {
            for (BasicPersonInfoPO basicBean : list) {
                CollectVO beanDao = new CollectVO();
                String dealMsg = "";
                String idCard = basicBean.getCertNum();
                beanDao.setCertNum(idCard);
                // 1、基础人员信息校验
                boolean babyCardResult = false;
                babyCardResult = CollectPersonInfoValidateUtil.BabyCard(idCard);
                if (babyCardResult) {
                    basicBean.setIsBaby(Constants.babyCard);
                } else {
                    basicBean.setIsBaby(Constants.notBabyCard);
                }
                boolean basicInfoValidResult = CollectPersonInfoValidateUtil.validateBasicInfoNotNull(basicBean);
                beanDao.setBadyCard(basicBean.getIsBaby());
                beanDao.setCertNum(idCard);
                if (!basicInfoValidResult) {
                    beanDao.setSynchroStatus("42");
                    beanDao.setDealMsg(basicBean.getDealMsg());
                    beanDao.setDealStaus("04");
                    collectService.updateUserInfoStatusByIdCardAndName(beanDao);
                    continue;
                }

                // 基础信息正常
                String imgSrcPath = "E://TSBphotoPath20180723//" + idCard + ".jpg";
                String imgDestPath = "E://TSBshouldbeDeal//" + idCard + ".jpg";
                if (!new File(imgSrcPath).exists()) {
                    // 若TSB照片不存在,同步状态为42,信息为TSB照片不存在
                    beanDao.setSynchroStatus("42");
                    beanDao.setDealStaus("02"); // 照片异常
                    beanDao.setDealMsg("人员基础信息正常 TSB、公安照片均不存在");
                } else {
                    try {
                        FileUtils.copyFile(new File(imgSrcPath), new File(imgDestPath));
                        logger.info("[采集库] 人员: " + idCard + " 照片复制成功");
                        beanDao.setSynchroStatus("45");
                        beanDao.setDealStaus("10");
                        collectService.updateUserInfoStatusByIdCardAndName(beanDao);
                        logger.info("[采集库] 人员: " + idCard + "更新状态为 45 10 ");
                    } catch (IOException e) {
                        logger.error("照片复制失败");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
