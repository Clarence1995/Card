package com.tecsun.card.service.threadrunnable;

import com.tecsun.card.common.clarencezeroutils.CollectDatabaseUtils;
import com.tecsun.card.common.clarencezeroutils.DateUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.CollectTwoService;
import com.tecsun.card.service.MidService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 0214
 * @Description 同步任务线程
 * @createTime 2018/9/4
 * @updateTime
 */
public class DataSynchroRunnable implements Runnable {
    private static final Logger            logger = LoggerFactory.getLogger(DataSynchroRunnable.class);
    /**
     * 采集库service类
     */
    private              CollectService    collectService;
    /**
     * 采集库70
     */
    private              CollectTwoService collectTwoService;
    /**
     * 中间库service类(存放公安照片)
     */
    private              MidService        midService;
    /**
     * 正式库service类
     */
    private              CardService       cardService;
    /**
     * 待复制照片路径
     */
    private              String            imgPath;
    /**
     * 只包含idCard的照片
     */
    private              List<String>      idCardList;


    /**
     * 日志路径
     */
    private String logPath;


    /**
     * 是否需要从数据库获取照片,如果不需要复制照片,则需要传入照片路径
     */
    private boolean getImgFromDatabase;

    /**
     * 是否需要判断基础人员信息
     */
    private boolean eValidateUserInfo;

    private final String LOG_NAME = "人员同步日志记录";

    private boolean getImgFromFile;

    private final String SUCCESS_LOG_NAME = "[SUCCESS]";
    private final String FAIL_LOG_NAME    = "[FAIL]";

    /**
     * DataSynchroRunnable 构造器
     *
     * @param collectService     采集Service
     * @param collectTwoService  采集70service
     * @param cardService        卡Service
     * @param midService         中间Service
     * @param idCardList         身份IDcard
     * @param imgFilePath        照片文件夹
     * @param logRootPath        日志文件夹
     * @param getImgFromDatabase 是否从数据库获取
     * @param eValidateUserInfo  是否需要判断人员基础信息
     */
    public DataSynchroRunnable(CollectService collectService,
                               CollectTwoService collectTwoService,
                               CardService cardService,
                               MidService midService,
                               List<String> idCardList,
                               String imgFilePath,
                               String logRootPath,
                               boolean getImgFromDatabase,
                               boolean eValidateUserInfo,
                               boolean getImgFromFile) {
        this.collectService = collectService;
        this.collectTwoService = collectTwoService;
        this.cardService = cardService;
        this.midService = midService;
        this.idCardList = idCardList;
        this.imgPath = imgFilePath;
        this.logPath = logRootPath;
        this.getImgFromDatabase = getImgFromDatabase;
        this.eValidateUserInfo = eValidateUserInfo;
        this.getImgFromFile = getImgFromFile;
    }


    /**
     * 1、人员信息校验
     * 2、根据身份证信息判断此人是否存在卡管
     * 3、同步卡管
     */
    @Override
    public void run() {
        // 1、判断
        if (ObjectUtils.isEmpty(logPath)) {
            throw new NullPointerException("logPath 不能为空");
        }
        if (ObjectUtils.isEmpty(imgPath)) {
            throw new NullPointerException("imgPath 不能为空");
        }

        if (!ObjectUtils.notEmpty(idCardList)) {
            throw new NullPointerException("idCardList 不能为空");
        }


        int               totalNum    = idCardList.size();
        ArrayList<String> successList = new ArrayList<>();
        ArrayList<String> errorList   = new ArrayList<>();

        for (String idCard : idCardList) {
            StringBuilder successSb = new StringBuilder(idCard);
            StringBuilder failSb    = new StringBuilder(idCard);
            // 1、确定TSB照片路径
            StringBuilder tsbImgPath = new StringBuilder(imgPath);
            tsbImgPath.append(Constants.SEPARATOR);
            tsbImgPath.append(idCard);
            tsbImgPath.append(Constants.IMG_SUFFIX);

            // 专门用来更新采集库的人员状态
            CollectVO updateCollectStatusBean = new CollectVO();
            updateCollectStatusBean.setCertNum(idCard);
            // 2、卡管库是否存在
            boolean userExist = cardService.userExistJudgeByIdCardAndName(idCard, null);
            if (userExist) {
                // 此人在卡管存在,需要更新在采集库的同步状态
                updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
                updateCollectStatusBean.setDealStaus(Constants.COLLECT_QUALIFIED);
                updateCollectStatusBean.setDealMsg("");
                collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
                logger.info("[0214 采集同步] 人员ID: {} 存在卡管库,不进行同步更新", idCard);
                successSb.append("_卡管库已存在此人");
                successList.add(successSb.toString());
                continue;
            }

            // 3、采集
            // 获取采集人员详情
            BasicPersonInfoPO basicPersonInfoPO = collectService.getBasicInfoByIDCard(idCard, null);
            if (null == basicPersonInfoPO) {
                // 采集库不存在此人
                logger.info("[0214 采集同步] 人员ID: {} 不存在采集库", idCard);
                failSb.append("_采集库不存在");
                errorList.add(failSb.toString());
                continue;
            }
            // 2、人员信息校验
            Ac01PO ac01PO = new Ac01PO();
            ac01PO.setAac147(idCard);
            if (eValidateUserInfo) {
                // 基础信息校验
                boolean checkUserInfo = CollectDatabaseUtils.checkCollectUserInfo(basicPersonInfoPO);
                // 区划校验
                ac01PO.setAac301b(basicPersonInfoPO.getRegionalCode());
                boolean checkRegionalCode = CollectDatabaseUtils.checkRegionalcode(ac01PO);
                if (!(checkUserInfo && checkRegionalCode)) {
                    // 如果校验不成功,则跳出FOR循环,并更新用户信息 同步状态为:0 数据处理状态为 04
                    updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_NO_SYNCHRO);
                    updateCollectStatusBean.setDealStaus(Constants.COLLECT_USERINFO_ERROR);
                    String        userInfoDealMsg     = basicPersonInfoPO.getDealMsg();
                    String        regionalCodeDealMsg = ac01PO.getDealMsg();
                    StringBuilder sb                  = new StringBuilder();
                    if (null != userInfoDealMsg) {
                        sb.append(userInfoDealMsg);
                    }
                    if (null != regionalCodeDealMsg && null != userInfoDealMsg) {
                        sb.append(";" + regionalCodeDealMsg);
                    } else if (null != regionalCodeDealMsg) {
                        sb.append(regionalCodeDealMsg);
                    }
                    updateCollectStatusBean.setDealMsg(sb.toString());
                    // 采集表人员状态更新
                    collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
                    logger.info("[0214 采集同步] 人员ID:{}, 信息校验失败,原因为: {}", idCard, sb.toString());
                    failSb.append("_校验失败:" + sb.toString());
                    errorList.add(failSb.toString());
                    continue;
                }

            }
            // 4、照片处理
            String codeForImg = ac01PO.getAac301();
            // 照片存放路径
            StringBuilder imgFilePath = new StringBuilder(Constants.IMG_113_FILE_ROOT_PATH);
            imgFilePath.append(Constants.SEPARATOR + ac01PO.getAac301() + Constants.SEPARATOR + idCard + Constants.IMG_SUFFIX);
            // 数据库字段所需要的路径
            String databaseImgPath = File.separator + codeForImg + File.separator + idCard + ".jpg";
            basicPersonInfoPO.setPhotoUrl(databaseImgPath);
            if (getImgFromDatabase) {
                // 是否从数据库中获取照片
                // 照片[中间库10.24.250.20]
                MidImgDAO midImgDAO = midService.getImgFromGONGAN(idCard);
                if (null == midImgDAO && midImgDAO.getXp().length == 0) {
                    // ②如果为空,则查询表 COLLECT_PHOTO
                    midImgDAO = midService.getImgFromGAT12(idCard);
                    if (null == midImgDAO && midImgDAO.getXp().length == 0) {
                        // ③如果为空,则查询表 GAT12
                        midImgDAO = midService.getImgFromCOLLECTPHOTO(idCard);
                        if (null == midImgDAO && midImgDAO.getXp().length == 0) {
                            logger.info("[0214 采集同步] 人员:" + idCard + "数据库不存在此人公安照片");
                            // 是否需要记录状态
                            updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_NO_SYNCHRO);
                            updateCollectStatusBean.setDealStaus(Constants.COLLECT_IMG_ERROR);
                            updateCollectStatusBean.setDealMsg("不存在公安照片");
                            failSb.append("_数据库不存在公安照片");
                            errorList.add(failSb.toString());
                            collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
                            continue;
                        }
                    }
                }
                // 写入指定文件夹
                byte[] bytes = midImgDAO.getXp();
                try {
                    FileUtils.writeByteArrayToFile(new File(imgFilePath.toString()), bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 从文件夹获取人员照片
            if (getImgFromFile) {
                if (ObjectUtils.isEmpty(imgPath)) {
                    throw new NullPointerException("[0214 采集同步] imgpath 不能为空");
                }
                // String srcImgPath = imgPath + Constants.SEPARATOR + idCard + Constants.IMG_SUFFIX;
                String disImgPath = imgFilePath.toString();
                if (!new File(tsbImgPath.toString()).exists()) {
                    logger.error("[0214 采集同步] 人员: {}, 照片路径不存在: {}", idCard, disImgPath);
                    failSb.append("_图片路径不存在");
                    continue;
                } else {
                    try {
                        FileUtils.copyFile(new File(tsbImgPath.toString()), new File(disImgPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.info("[0214 采集同步] 人员: {} 照片 (源于指定文件夹)复制成功, 目标路径为: {} ", idCard, disImgPath);
                }

            }
            // 4、藏文
            String zangName = collectService.getZangNameByIdCard(idCard);
            if (ObjectUtils.notEmpty(zangName)) {
                basicPersonInfoPO.setZangName(zangName);
            } else {
                basicPersonInfoPO.setZangName(Constants.ZANG_NAME_NOT_EXIST);
            }

            // 5、同步到卡管库
            try {
                cardService.assembleAC01(ac01PO, basicPersonInfoPO);
            } catch (Exception e) {
                logger.error("[0214 采集同步: 数据组装出错], 原因: {}", e);
                failSb.append("_数组组装出错:" + e);
                errorList.add(failSb.toString());
                continue;
            }
            basicPersonInfoPO.setRegionalCode(ac01PO.getAac301());
            // △ 数据库同步
            boolean synchroBean = cardService.insertCardAC01AndBusApplyFromCollect(ac01PO, basicPersonInfoPO);
            if (synchroBean) {
                logger.info("[0214 采集同步] 人员: {} 已正常同步完成", idCard);
                successSb.append("_采集人员同步完成");
            } else {
                logger.info("[0214 采集同步] 人员: {} 同步失败", idCard);
                failSb.append("_数据库同步失败");
                errorList.add(failSb.toString());
                continue;
            }
            successList.add(successSb.toString());
        }

        try {
            // 日志文件路径
            successList.add("处理总人数： " + totalNum + "。成功人数：" + successList.size() + ";失败人数： " + errorList.size());
            errorList.add("处理总人数： " + totalNum + "。成功人数：" + (successList.size() - 1) + ";失败人数： " + errorList.size());
            StringBuilder logFilePath = new StringBuilder(logPath);
            logFilePath.append(Constants.SEPARATOR + LOG_NAME + DateUtils.getNowYMDHM() + SUCCESS_LOG_NAME + Constants.TXT_SUFFIX);
            TxtUtil.writeTxt(new File(logFilePath.toString()), "UTF-8", successList);

            logFilePath.replace(logFilePath.toString().lastIndexOf("\\") + 1,
                    logFilePath.toString().length(), LOG_NAME + DateUtils.getNowYMDHM() + FAIL_LOG_NAME + Constants.TXT_SUFFIX);
            TxtUtil.writeTxt(new File(logFilePath.toString()), "UTF-8", errorList);
        } catch (IOException e) {
            logger.error("[0214 采集同步] 文件复制出错：{}" ,e);
        }
    }
}
