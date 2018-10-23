package com.tecsun.card.controller.threadtask.synchro.cardsynchro;

import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.MidService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
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
public class CollectToCardSynchroThreadTask implements Runnable {
    private static final Logger                logger = LoggerFactory.getLogger(CollectToCardSynchroThreadTask.class);
    /**
     * 采集库service类
     */
    private              CollectService        collectService;
    /**
     * 中间库service类(存放公安照片)
     */
    private              MidService            midService;
    /**
     * 正式库service类
     */
    private              CardService           cardService;
    /**
     * List
     */
    private              List<BasicPersonInfo> beanList;
    /**
     * 待复制照片路径
     */
    private              String                imgPath;
    /**
     * 只包含idCard的照片
     */
    private              List<String>          idCardList;

    /**
     * 日志路径
     */
    private String logPath;

    /**
     * 错误文件夹路径
     */
    private String errorImgpath = "E://synchroClarence//errorImg//";

    /**
     * 是否需要从数据库获取照片,如果不需要复制照片,则需要传入照片路径
     */
    private boolean getImgFromDatabase = false;
//    public DataSynchroRunnable(CollectService collectService,
//                                          CardService cardService,
//                                          List<BasicPersonInfo> beanList,
//                                          String logPath,
//                                          String imgPath,
//                                          boolean getImgFromDatabase
//                                          ) {
//        this.collectService = collectService;
//        this.cardService = cardService;
//        this.beanList = beanList;
//    }
//
//    public DataSynchroRunnable(CollectService collectService,
//                                          CardService cardService,
//                                          List<BasicPersonInfo> beanList,
//                                          String imgPath) {
//        this.collectService = collectService;
//        this.midService = midService;
//        this.cardService = cardService;
//        this.beanList = beanList;
//        this.imgPath = imgPath;
//    }
//
//    public DataSynchroRunnable(CollectService collectService,
//                                          MidService midService,
//                                          CardService cardService,
//                                          List<BasicPersonInfo> beanList,
//                                          boolean getImgFromDatabase) {
//        this.collectService = collectService;
//        this.midService = midService;
//        this.cardService = cardService;
//        this.beanList = beanList;
//        this.getImgFromDatabase = getImgFromDatabase;
//    }

    /**
     *@Description
     *@params  
     *@return  
     *@author  0214
     *@createTime  2018/9/4
     *@updateTime
     */
    public CollectToCardSynchroThreadTask(CollectService collectService,
                                          CardService cardService,
                                          MidService midService,
                                          List<BasicPersonInfo> beanList,
                                          List<String> idCardList,
                                          String imgPath,
                                          String logPath,
                                          boolean getImgFromDatabase
    ) {
        this.collectService = collectService;
        this.cardService = cardService;
        this.midService = midService;
        this.beanList = beanList;
        this.idCardList = idCardList;
        this.imgPath = imgPath;
        this.logPath = logPath;
        this.getImgFromDatabase = getImgFromDatabase;
    }


    /**
     * 1、人员信息校验
     * 2、根据身份证信息判断此人是否存在卡管
     * 3、同步卡管
     */
    @Override
    public void run() {
        // 线程名字
        String threadName = Thread.currentThread().getName();
        // 当前线程处理的人数
        int               totalNum  = idCardList.size();
        ArrayList<String> successList = new ArrayList<>();
        ArrayList<String> errorList = new ArrayList<>();
        for (String idCard : idCardList) {
            try {
                // 源图片路径
                String srcImgPath = imgPath + File.separator + idCard + ".jpg";
                // 从采集库中获取人员详情


                // 1、卡管是否存在
                CollectVO collectVO = new CollectVO();
                collectVO.setCertNum(idCard);
//                 boolean userExist = cardService.userExistInCard(idCard, null);
//                 if (userExist) {
//                     // 1-1、如果卡管存在此人,则更新此人在采集库的同步状态
// //                    collectVO.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
// //                    collectVO.setDealStaus(Constants.COLLECT_QUALIFIED);
// //                    collectService.updateUserInfoStatusByIdCardAndName(collectVO);
//                     logger.info("[采集库 => 卡管库] 此人{} 已存在卡管库,不进行同步更新", idCard);
//                     successList.add(idCard + "_" + "卡管库已存在此人");
//                     continue;
//                 }

//                BasicPersonInfo basicPersonInfo = collectService.getBasicInfoByIDCard(idCard);
                BasicPersonInfo basicPersonInfo = midService.getBasicInfoByIdCard(idCard);
                if (null == basicPersonInfo) {
                    // 采集库不存在此人
                    logger.info("[采集库 => 卡管库] 此人{} 不存在采集库", idCard);
                    errorList.add(idCard + "_" + "采集库不存在");
                    continue;
                }
                // 2、人员信息校验
                boolean validateSuccess = collectService.validateuserInfo(basicPersonInfo);
                if (!validateSuccess) {
                    // 如果校验不成功,则跳出FOR循环,并更新用户信息 同步状态为:0 数据处理状态为 04
//                    collectVO.setSynchroStatus(Constants.COLLECT_NO_SYNCHRO);
//                    collectVO.setDealStaus(Constants.COLLECT_USERINFO_ERROR);
//                    collectVO.setDealMsg(basicPersonInfo.getDealMsg());
//                    collectService.updateUserInfoStatusByIdCardAndName(collectVO);
                    logger.info("[采集库 => 卡管库] 人员:{} 信息校验失败~ {}", basicPersonInfo.getCertNum(), basicPersonInfo.getDealMsg());
                    errorList.add(idCard + "_" + basicPersonInfo.getDealMsg());
                    // 较验失败
                    FileUtils.copyFile(new File(srcImgPath), new File(errorImgpath + idCard + ".jpg"));
                    continue;
                }


                Ac01PO ac01PO = new Ac01PO();
                // 3-1、区域判断
                String regionalCode = basicPersonInfo.getRegionalCode();
                if (regionalCode.length() == 6) {
                    // 549900
                    ac01PO.setAac301(Constants.OUTER_CODE_REGIONAL);
                    ac01PO.setAac301a(regionalCode);
                    ac01PO.setAac301b(regionalCode + "00");
                } else {
                    String codeRoot = regionalCode.substring(0, 4);
                    codeRoot = StringUtils.rightPad(codeRoot, 6, "0");
                    ac01PO.setAac301(codeRoot);
                    ac01PO.setAac301a(regionalCode.substring(0, 6));
                    ac01PO.setAac301b(regionalCode.substring(0, 8));
                }

                // 4、照片处理
                // 图片保存路径
                String codeForImg      = ac01PO.getAac301();
                String localImgPath    = "E://tecsun//file//photo//personPhoto" + File.separator + codeForImg + File.separator + idCard + ".jpg";
                String databaseImgPath = File.separator + codeForImg + File.separator + idCard + ".jpg";
                basicPersonInfo.setPhotoUrl(databaseImgPath);
                if (getImgFromDatabase) {
                    // 照片[中间库10.24.250.20]
                    MidImgDAO midImgDAO = midService.getImgFromGONGAN(idCard);

                    if (null == midImgDAO && midImgDAO.getXp().length == 0) {
                        // ②如果为空,则查询表 COLLECT_PHOTO
                        midImgDAO = midService.getImgFromGAT12(idCard);
                        if (null == midImgDAO && midImgDAO.getXp().length == 0) {
                            // ③如果为空,则查询表 GAT12
                            midImgDAO = midService.getImgFromCOLLECTPHOTO(idCard);
                            if (null == midImgDAO && midImgDAO.getXp().length == 0) {
                                logger.info("[采集库 => 卡管库] 人员:" + idCard + "公安照片不存在数据库");
                                // 是否需要记录状态
                                collectVO.setSynchroStatus(Constants.COLLECT_NO_SYNCHRO);
                                collectVO.setDealStaus(Constants.COLLECT_IMG_ERROR);
                                collectVO.setDealMsg("不存在公安照片");
                                collectService.updateUserInfoStatusByIdCardAndName(collectVO);
                                continue;
                            }
                        }
                    }
                    // 写入指定文件夹
                    byte[] bytes = midImgDAO.getXp();
                    FileUtils.writeByteArrayToFile(new File(localImgPath), bytes);
                } else if (null != imgPath && !("").equals(imgPath)) {
                    // 2-2-2 如果存在外在文件路径,则复制这个路径到指定文件夹
                    if (!new File(imgPath).exists()) {
                        throw new FileNotFoundException("文件夹不存在");
                    }
                    if (!new File(srcImgPath).exists()) {
                        throw new FileNotFoundException("用户:" + idCard + " 照片不存在");
                    }
                    FileUtils.copyFile(new File(srcImgPath), new File(localImgPath));
                    logger.info("[采集库 => 卡管库] 用户: {} 照片[数据中心处理过]复制成功", idCard);
                }

                // 4、藏文
                String zangName = collectService.getZangNameByIdCard(idCard);
                if (null != zangName && !("").equals(zangName)) {
                    basicPersonInfo.setZangName(zangName);
                } else {
                    basicPersonInfo.setZangName(Constants.ZANG_NAME_NOT_EXIST);
                }

                // 5、数据库同步
                try {
                    cardService.assembleAC01(ac01PO, basicPersonInfo);
                } catch (Exception e) {

                }
                basicPersonInfo.setRegionalCode(ac01PO.getAac301());
                boolean synchroBean = cardService.insertCardAC01AndBusApplyFromCollect(ac01PO, basicPersonInfo);

                if (synchroBean) {
                    logger.info("[采集库 => 卡管库] 人员: {} 已正常同步完成", idCard);
                } else {
                    logger.info("[采集库 => 卡管库] 人员: {} 同步失败", idCard);
                    errorList.add(idCard + "_数据库同步失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("人员: {}, 同步出错:" + idCard);
                errorList.add(idCard + "_" + "同步出错_" + e.getMessage());
            }
        }

        try {
            SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            StringBuilder    str = new StringBuilder();
            str.append(logPath);
            str.append(File.separator);
            str.append(df.format(new Date()));
            str.append("_" + "success");
            str.append("_" + totalNum);
            str.append(".txt");
            TxtUtil.writeTxt(new File(str.toString()), "UTF-8", successList);
            StringBuilder sb = new StringBuilder(logPath);
            sb.append(File.separator);
            sb.append(df.format(new Date()));
            sb.append("_" + "fail");
            sb.append("_" + errorList.size());
            sb.append(".txt");
            TxtUtil.writeTxt(new File(sb.toString()), "UTF-8", errorList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
