package com.tecsun.card.service.threadrunnable;

import com.tecsun.card.common.clarencezeroutils.*;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.service.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 0214
 * @Description 同步任务线程
 * @createTime 2018/9/4
 * @updateTime
 */
public class DataSynchroRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DataSynchroRunnable.class);

    /**
     * 数据处理Service
     */
    private DataHandleService dataHandleService;
    /**
     * 待复制照片路径
     */
    private String            imgFilePath;
    /**
     * 只包含idCard的照片
     */
    private List<String>      idCardList;

    /**
     * 日志路径
     */
    private String logPath;


    /**
     * 是否需要从数据库获取照片,如果不需要复制照片,则需要传入照片路径
     */
    private boolean eGetImgFromDatabase;
    /**
     * 是否需要复制照片
     */
    private boolean eCopyImgFromHadDeal;

    /**
     * 是否需要判断基础人员信息
     */
    private boolean eValidateUserInfo;

    /**
     * 是否需要和公安同步
     */
    private boolean eCompareWithGongAn;
    /**
     * 是否需要删除AC01(用于做异常数据同步)
     */
    private boolean eDeleteAC01User;
    /**
     * 是否跳过40采集库数据
     */
    private boolean eGetCollectInfoFromSeventyDatabase;
    /**
     * 是否需要和公安接口人员信息是否进行比对
     */
    private boolean eCompareWithGongAnDatabase;
    /**
     * 是否需要标记人员为优先
     */
    private boolean eMarkPriority;
    /**
     * 是否为藏区人员
     */
    private boolean eZang;
    /**
     * 是否需要复制失败人员照片到指定文件夹
     */
    private boolean copyImgFail;
    private boolean getImgFromFile;

    private final String LOG_NAME         = "人员同步日志记录";
    private final String LOG_THREAD_NAME  = "人员同步所有线程日志记录";
    private final String SUCCESS_LOG_NAME = "[SUCCESS]";
    private final String FAIL_LOG_NAME    = "[FAIL]";
    private       Object object           = new Object();

    /**
     * DataSynchroRunnable 构造器
     * @param collectService    采集Service
     * @param collectTwoService  采集70service
     * @param cardService        卡Service
     * @param midService         中间Service
     * @param idCardList         身份IDcard
     * @param imgFilePath        照片文件夹
     * @param logRootPath        日志文件夹
     * @param getImgFromDatabase 是否从数据库获取
     * @param eValidateUserInfo  是否需要判断人员基础信息
     * @param ignoreCollect      是否忽略采集库(如果忽略,则从PUBLIC 20中获取数据)
     * @param eCompareWithGongAn 是否和公安库进行信息比对
     * @param eDeleteAC01User    是否需要删除AC01人员数据(用做异常数据新申领)
     */
    // public DataSynchroRunnable(CollectService collectService,
    //                            CollectTwoService collectTwoService,
    //                            CardService cardService,
    //                            MidService midService,
    //                            List<String> idCardList,
    //                            String imgFilePath,
    //                            String logRootPath,
    //                            boolean getImgFromDatabase,
    //                            boolean eValidateUserInfo,
    //                            boolean getImgFromFile,
    //                            boolean ecopyImg,
    //                            boolean ignoreCollect,
    //                            boolean eCompareWithGongAn,
    //                            boolean eDeleteAC01User) {
    //     this.collectService = collectService;
    //     this.collectTwoService = collectTwoService;
    //     this.cardService = cardService;
    //     this.midService = midService;
    //     this.idCardList = idCardList;
    //     this.imgFilePath = imgFilePath;
    //     this.logPath = logRootPath;
    //     this.eGetImgFromDatabase = getImgFromDatabase;
    //     this.eValidateUserInfo = eValidateUserInfo;
    //     this.getImgFromFile = getImgFromFile;
    //     this.ecopyImg = ecopyImg;
    //     this.ignoreCollect = ignoreCollect;
    //     this.eCompareWithGongAn = eCompareWithGongAn;
    //     this.eDeleteAC01User = eDeleteAC01User;
    // }

    /**
     * @param dataHandleService            数据处理Service
     * @param idCardList                   身份证IDList
     * @param logPath                      日志文件路径(所有线程写入一个文件里面。如果出现公安接口异常的数据的话,则每个线程写入单独的文件夹里面)
     * @param egetImgFromDatabase          是否需要从本地数据库复制公安照片
     * @param eValidateUserInfo            是否需要校验人员信息
     * @param eIgnoreFourtyCollectDatabase 是否需要忽略40采集库人员(因为对于区外异地来讲,113是不能直接70区外采集数据库的,奇葩)
     * @param eCompareWithGongAnDatabase   是否需要和公安接口进行人员信息比对
     * @param eDeleteAC01User              是否需要删除AC01人员表(这个是处理异常人员数据的。先把AC01人员删除掉,再从采集库里面同步到卡管库)
     * @param eCopyImgFromHadDeal          是否需要复制数据中心处理过的照片(因为做同步的时候,可能只需要把采集库的人员信息导入到卡管库,但是人员图片可能已经存在了)
     * @param eMarkPriority                是否需要标记人员为优先
     * @param eZang                        是否为藏区人员
     * @param eCopyImgFail                 是否需要复制同步失败人员照片
     * @param imgFilePath                  如果需要复制照片,则需要传递照片根路径
     */
    public DataSynchroRunnable(DataHandleService dataHandleService,
                               List<String> idCardList,
                               String logPath,
                               Boolean egetImgFromDatabase,
                               Boolean eValidateUserInfo,
                               Boolean eIgnoreFourtyCollectDatabase,
                               Boolean eCompareWithGongAnDatabase,
                               Boolean eDeleteAC01User,
                               Boolean eCopyImgFromHadDeal,
                               Boolean eMarkPriority,
                               Boolean eZang,
                               Boolean eCopyImgFail,
                               String imgFilePath) {
        this.dataHandleService = dataHandleService;
        this.idCardList = idCardList;
        this.logPath = logPath;
        this.eGetImgFromDatabase = egetImgFromDatabase;
        this.eValidateUserInfo = eValidateUserInfo;
        this.eGetCollectInfoFromSeventyDatabase = eIgnoreFourtyCollectDatabase;
        this.eCompareWithGongAnDatabase = eCompareWithGongAnDatabase;
        this.eDeleteAC01User = eDeleteAC01User;
        this.eCopyImgFromHadDeal = eCopyImgFromHadDeal;
        this.eMarkPriority = eMarkPriority;
        this.eZang = eZang;
        this.copyImgFail = eCopyImgFail;
        this.imgFilePath = imgFilePath;
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
        if (ObjectUtils.isEmpty(imgFilePath)) {
            throw new NullPointerException("imgFilePath 不能为空");
        }
        if (!ObjectUtils.notEmpty(idCardList)) {
            throw new NullPointerException("idCardList 不能为空。此线程不参与同步");
        }

        // 2、数据准备
        int               totalNum    = idCardList.size();
        int               good        = 0;
        int               bad         = 0;
        ArrayList<String> successList = new ArrayList<>();
        // 错误日志集合
        ArrayList<String> errorLogList = new ArrayList<>();
        // 异常日志信息
        ArrayList<String> exceptionLogList = new ArrayList<>();
        // 照片复制出错
        ArrayList<String>       copyImgFailList     = new ArrayList<>();
        List<Ac01PO>            userList            = new ArrayList<>(10);
        Map<String, BusApplyPO> busApplyList        = new HashMap<>(10);
        List<BasicPersonInfo>   basicPersonInfoList = new ArrayList<>(10);

        // 遍历集合(重新开线程处理)

        for (int i = 0; i < idCardList.size(); i++) {
            String idCard = idCardList.get(i);
            try {
                boolean commit = false;
                Result result = dataHandleService
                        .handleCollectSynchro(idCard,
                                eGetImgFromDatabase,
                                eValidateUserInfo,
                                eGetCollectInfoFromSeventyDatabase,
                                eCompareWithGongAnDatabase,
                                eDeleteAC01User,
                                eCopyImgFromHadDeal,
                                eMarkPriority,
                                eZang,
                                imgFilePath);
                if (Constants.FAIL_RESULT_CODE == result.getStateCode()) {
                    // 失败人员信息
                    errorLogList.add(result.getMsg());
                    bad++;
                } else if (Constants.SUCCESS_RESULT_CODE == result.getStateCode()) {
                    // 成功人员信息
                    Map<String, Object> resultMap           = (Map<String, Object>) result.getData();
                    Ac01PO              ac01Bean            = (Ac01PO) resultMap.get("AC01");
                    BusApplyPO          busApplyBean        = (BusApplyPO) resultMap.get("BUS_APPLY");
                    BasicPersonInfo     basicPersonInfoBean = (BasicPersonInfo) resultMap.get("BASIC_PERSON_INFO");
                    userList.add(ac01Bean);
                    busApplyList.put(busApplyBean.getApplyIdCard(), busApplyBean);
                    basicPersonInfoList.add(basicPersonInfoBean);
                } else {
                    // 异常人员信息
                    bad++;
                    exceptionLogList.add(result.getMsg());
                }
                if (userList.size() == 10) {
                    commit = true;
                } else if (i == idCardList.size() - 1) {
                    commit = true;
                }
                if (commit && userList.size() > 0) {
                    BasicPersonInfoDAO idList = new BasicPersonInfoDAO();
                    idList.setDealStatus(Constants.COLLECT_QUALIFIED);
                    idList.setDealMsg("合格");
                    idList.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
                    List<Long> idList2 = new ArrayList<>();
                    for (BasicPersonInfo basicPersonInfo : basicPersonInfoList) {
                        idList2.add(basicPersonInfo.getId());
                    }
                    idList.setIdList(idList2);
                    // 插入操作
                    Result result1 = dataHandleService.handleSynchroByUserListBusList(userList, busApplyList, idList);
                    if (Constants.EXCEPTION_RESULT_CODE == result1.getStateCode()) {
                        List<Ac01PO> userErrorList = (List<Ac01PO>) result1.getData();
                        for (Ac01PO ac01PO : userErrorList) {
                            errorLogList.add(ac01PO.getAac147() + "_批量同步出现异常");
                            bad++;
                        }
                        continue;
                    } else {
                        good += userList.size();
                    }
                    userList = new ArrayList<>(10);
                }
            } catch (Exception e) {
                logger.error("[0214 采集同步] 人员ID:{} 同步出现异常。原因: {}", idCard, e);
                bad++;
                if (null != e.getMessage()) {
                    exceptionLogList.add(idCard + "_同步出现异常 " + e.getMessage());
                } else {
                    exceptionLogList.add(idCard + "_同步出现异常 ");
                }
            }
        }
        try {
            if (copyImgFail) {
                try {
                    // 失败照片复制
                    String str = PropertyUtils.get("CARD_FAIL_IMG_PATH");
                    if (ObjectUtils.notEmpty(errorLogList)) {
                        for (String s : errorLogList) {
                            String id = s.split("_")[0];
                            MyFileUtils.copyImg(imgFilePath, str, id);
                        }
                    } else if (ObjectUtils.notEmpty(exceptionLogList)) {
                        for (String s : exceptionLogList) {
                            String id = s.split("_")[0];
                            MyFileUtils.copyImg(imgFilePath, str, id);
                        }
                    }
                } catch (Exception e) {
                    logger.error("[0214 复制失败照片出错。原因: {}", e);
                    // exceptionLogList.add()
                }
            }
            // 日志文件路径
            synchronized (object) {
                successList.add("线程:" + Thread.currentThread().getName() + ": 处理总人数:" + totalNum + "。成功人数:" + good + ";失败人数:" + bad);
                if (ObjectUtils.notEmpty(errorLogList)) {
                    StringBuilder logFilePath = new StringBuilder(logPath);
                    logFilePath.append(Constants.SEPARATOR + LOG_NAME + PropertyUtils.get("CARD_LOG_FAIL") + Constants.TXT_SUFFIX);
                    TxtUtil.appendTxt(new File(logFilePath.toString()), errorLogList);
                    TxtUtil.textFormat(logFilePath.toString());
                }
                if (ObjectUtils.notEmpty(exceptionLogList)) {
                    StringBuilder exceptionLogFilePath = new StringBuilder(logPath);
                    exceptionLogFilePath.append(Constants.SEPARATOR + LOG_NAME + PropertyUtils.get("CARD_LOG_EXCEPTION") + Constants.TXT_SUFFIX);
                    TxtUtil.appendTxt(new File(exceptionLogFilePath.toString()), exceptionLogList);
                    TxtUtil.textFormat(exceptionLogFilePath.toString());
                }
                if (ObjectUtils.notEmpty(copyImgFailList)) {
                    StringBuilder exceptionLogFilePath = new StringBuilder(logPath);
                    exceptionLogFilePath.append(Constants.SEPARATOR + LOG_NAME + "_COPYIMGERROR" + Constants.TXT_SUFFIX);
                    TxtUtil.writeTxt(new File(exceptionLogFilePath.toString()), "UTF-8", exceptionLogList);
                    TxtUtil.textFormat(exceptionLogFilePath.toString());
                }
                logger.info("[0214 采集同步 完成] ~----------当前线程: {}, 处理总人数: {}, 成功人数: {}, 失败人数: {}-----------------------",
                        Thread.currentThread().getName(), totalNum, good, bad);
                StringBuilder threadDealLogPath = new StringBuilder(logPath);
                threadDealLogPath.append(Constants.SEPARATOR + LOG_THREAD_NAME + Constants.TXT_SUFFIX);
                TxtUtil.appendTxt(new File(threadDealLogPath.toString()), successList);
            }
        } catch (IOException e) {
            logger.error("[0214 采集同步] 日志文件写入出错：{}", e);
        }

        // for (String idCard : idCardList) {
        //     StringBuilder successSb = new StringBuilder(idCard);
        //     StringBuilder failSb    = new StringBuilder(idCard);
        //     // 1、确定TSB照片路径
        //     StringBuilder tsbImgPath = null;
        //     if (ecopyImg) {
        //         tsbImgPath = new StringBuilder(imgFilePath);
        //         tsbImgPath.append(Constants.SEPARATOR);
        //         tsbImgPath.append(idCard);
        //         tsbImgPath.append(Constants.IMG_SUFFIX);
        //     }
        //
        //     // 专门用来更新采集库的人员状态
        //     CollectVO updateCollectStatusBean = new CollectVO();
        //     updateCollectStatusBean.setCertNum(idCard);
        //
        //
        //     // 2、卡管库是否存在
        //     boolean userExist = cardService.userExistJudgeByIdCardAndName(idCard, null);
        //
        //     // 2-1: 如果卡管存在,判断是否需要删除,如果删除的话,则把userExist置为空
        //     if (userExist && eDeleteAC01User) {
        //         // 两者成立,进行
        //         try {
        //             boolean result = cardService.deleteAC01ByIdCardAndName(idCard, null);
        //             if (result) {
        //                 userExist = false;
        //             } else {
        //                 errorList.add(idCard + "_人员删除失败,失败原因: 可能人员不存在,可能传入的身份证为空");
        //                 continue;
        //             }
        //         } catch (Exception e) {
        //             logger.error("人员删除异常--身份证号:{},异常原因: {}", idCard,e);
        //             errorList.add(idCard + "_人员在AC01表删除时异常");
        //             continue;
        //         }
        //     }
        //
        //     if (userExist) {
        //         // 此人在卡管存在,需要更新在采集库的同步状态
        //         updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
        //         updateCollectStatusBean.setDealStaus(Constants.COLLECT_QUALIFIED);
        //         updateCollectStatusBean.setDealMsg("");
        //         try {
        //             collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //         logger.info("[0214 采集同步] 人员ID: {} 存在卡管库,不进行同步更新", idCard);
        //         successSb.append("_卡管库已存在此人");
        //         successList.add(successSb.toString());
        //         continue;
        //     }
        //
        //     // 3、采集
        //     // 获取采集人员详情
        //     BasicPersonInfo basicPersonInfo = null;
        //     //
        //     if (!ignoreCollect) {
        //         try {
        //             basicPersonInfo = collectService.getBasicInfoByIDCard(idCard, null);
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //     } else {
        //         try {
        //             basicPersonInfo = collectService.getSingleBasicPersonByIdcardFromMidTwenty(idCard, null);
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //     }
        //     if (null == basicPersonInfo && !ignoreCollect) {
        //         try {
        //             basicPersonInfo = collectService.getSingleBasicPersonByIdcardFromMidTwenty(idCard, null);
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //         if (null == basicPersonInfo) {
        //             // 采集库不存在此人
        //             logger.info("[0214 采集同步] 人员ID: {} 不存在采集库和中间库20", idCard);
        //             failSb.append("_采集库&中间库20不存在");
        //             errorList.add(failSb.toString());
        //             continue;
        //         }
        //     }
        //     // 2、人员信息校验: 基本信息校验
        //     Ac01PO ac01PO = new Ac01PO();
        //     ac01PO.setAac147(idCard);
        //     if (eValidateUserInfo) {
        //         // 基础信息校验
        //         boolean checkUserInfo = CollectDatabaseUtils.checkCollectUserInfo(basicPersonInfo);
        //         // 区划校验
        //         ac01PO.setAac301b(basicPersonInfo.getRegionalCode());
        //         boolean checkRegionalCode = CollectDatabaseUtils.checkRegionalcode(ac01PO);
        //         if (!(checkUserInfo && checkRegionalCode)) {
        //             // 如果校验不成功,则跳出FOR循环,并更新用户信息 同步状态为:0 数据处理状态为 04
        //             updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_NO_SYNCHRO);
        //             updateCollectStatusBean.setDealStaus(Constants.COLLECT_USERINFO_ERROR);
        //             String        userInfoDealMsg     = basicPersonInfo.getDealMsg();
        //             String        regionalCodeDealMsg = ac01PO.getDealMsg();
        //             StringBuilder sb                  = new StringBuilder();
        //             if (null != userInfoDealMsg) {
        //                 sb.append(userInfoDealMsg);
        //             }
        //             if (null != regionalCodeDealMsg && null != userInfoDealMsg) {
        //                 sb.append(";" + regionalCodeDealMsg);
        //             } else if (null != regionalCodeDealMsg) {
        //                 sb.append(regionalCodeDealMsg);
        //             }
        //             updateCollectStatusBean.setDealMsg(sb.toString());
        //             // 采集表人员状态更新
        //             try {
        //                 collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
        //             } catch (Exception e) {
        //                 e.printStackTrace();
        //             }
        //             logger.info("[0214 采集同步] 人员ID:{}, 信息校验失败,原因为: {}", idCard, sb.toString());
        //             failSb.append("_校验失败:" + sb.toString());
        //             errorList.add(failSb.toString());
        //             continue;
        //         }
        //
        //     }
        //     // 3、公安信息校验: 比对失败,则加入errorList,比对成功,则放行
        //
        //
        //     // 4、照片处理
        //     String codeForImg = ac01PO.getAac301();
        //     // 照片存放路径
        //     StringBuilder imgFilePath = new StringBuilder(Constants.IMG_113_FILE_ROOT_PATH);
        //     imgFilePath.append(Constants.SEPARATOR + ac01PO.getAac301() + Constants.SEPARATOR + idCard + Constants.IMG_SUFFIX);
        //     // 数据库字段所需要的路径
        //     String databaseImgPath = File.separator + codeForImg + File.separator + idCard + ".jpg";
        //     basicPersonInfo.setPhotoUrl(databaseImgPath);
        //     if (ecopyImg) {
        //         if (eGetImgFromDatabase) {
        //             // 是否从数据库中获取照片
        //             // 照片[中间库10.24.250.20]
        //             MidImgDAO midImgDAO = midService.getImgFromGONGAN(idCard);
        //             if (null == midImgDAO && midImgDAO.getXp().length == 0) {
        //                 // ②如果为空,则查询表 COLLECT_PHOTO
        //                 midImgDAO = midService.getImgFromGAT12(idCard);
        //                 if (null == midImgDAO && midImgDAO.getXp().length == 0) {
        //                     // ③如果为空,则查询表 GAT12
        //                     midImgDAO = midService.getImgFromCOLLECTPHOTO(idCard);
        //                     if (null == midImgDAO && midImgDAO.getXp().length == 0) {
        //                         logger.info("[0214 采集同步] 人员:" + idCard + "数据库不存在此人公安照片");
        //                         // 是否需要记录状态
        //                         updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_NO_SYNCHRO);
        //                         updateCollectStatusBean.setDealStaus(Constants.COLLECT_IMG_ERROR);
        //                         updateCollectStatusBean.setDealMsg("不存在公安照片");
        //                         failSb.append("_数据库不存在公安照片");
        //                         errorList.add(failSb.toString());
        //                         try {
        //                             collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
        //                         } catch (Exception e) {
        //                             e.printStackTrace();
        //                         }
        //                         continue;
        //                     }
        //                 }
        //             }
        //             // 写入指定文件夹
        //             byte[] bytes = midImgDAO.getXp();
        //             try {
        //                 FileUtils.writeByteArrayToFile(new File(imgFilePath.toString()), bytes);
        //             } catch (IOException e) {
        //                 e.printStackTrace();
        //             }
        //         }
        //         // 从文件夹获取人员照片
        //         if (getImgFromFile) {
        //             if (ObjectUtils.isEmpty(imgFilePath)) {
        //                 throw new NullPointerException("[0214 采集同步] imgpath 不能为空");
        //             }
        //             // String srcImgPath = imgFilePath + Constants.SEPARATOR + idCard + Constants.IMG_SUFFIX;
        //             String disImgPath = imgFilePath.toString();
        //             if (!new File(tsbImgPath.toString()).exists()) {
        //                 logger.error("[0214 采集同步] 人员: {}, 照片路径不存在: {}", idCard, disImgPath);
        //                 failSb.append("_图片路径不存在");
        //                 continue;
        //             } else {
        //                 try {
        //                     FileUtils.copyFile(new File(tsbImgPath.toString()), new File(disImgPath));
        //                 } catch (IOException e) {
        //                     e.printStackTrace();
        //                 }
        //                 logger.info("[0214 采集同步] 人员: {} 照片 (源于指定文件夹)复制成功, 目标路径为: {} ", idCard, disImgPath);
        //             }
        //
        //         }
        //     }
        //     // 4、藏文
        //     String zangName = collectService.getZangNameByIdCard(idCard);
        //     if (ObjectUtils.notEmpty(zangName)) {
        //         basicPersonInfo.setZangName(zangName);
        //     } else {
        //         basicPersonInfo.setZangName(Constants.ZANG_NAME_NOT_EXIST);
        //     }
        //
        //     // 5、同步到卡管库
        //     try {
        //         cardService.assembleAC01(ac01PO, basicPersonInfo);
        //     } catch (Exception e) {
        //         logger.error("[0214 采集同步: 数据组装出错], 原因: {}", e);
        //         failSb.append("_数组组装出错:" + e);
        //         errorList.add(failSb.toString());
        //         continue;
        //     }
        //     basicPersonInfo.setRegionalCode(ac01PO.getAac301());
        //     // △ 数据库同步
        //     boolean synchroBean = cardService.insertCardAC01AndBusApplyFromCollect(ac01PO, basicPersonInfo);
        //     if (synchroBean) {
        //         logger.info("[0214 采集同步] 人员: {} 已正常同步完成", idCard);
        //         successSb.append("_采集人员同步完成");
        //     } else {
        //         logger.info("[0214 采集同步] 人员: {} 同步失败", idCard);
        //         failSb.append("_数据库同步失败");
        //         errorList.add(failSb.toString());
        //         continue;
        //     }
        //     successList.add(successSb.toString());
        // }

        // try {
        //     // 日志文件路径
        //     successList.add("处理总人数： " + totalNum + "。成功人数：" + successList.size() + ";失败人数： " + errorList.size());
        //     errorList.add("处理总人数： " + totalNum + "。成功人数：" + (successList.size() - 1) + ";失败人数： " + errorList.size());
        //     StringBuilder logFilePath = new StringBuilder(logPath);
        //     logFilePath.append(Constants.SEPARATOR + LOG_NAME + DateUtils.getNowYMDHM() + SUCCESS_LOG_NAME + Constants.TXT_SUFFIX);
        //     TxtUtil.writeTxt(new File(logFilePath.toString()), "UTF-8", successList);
        //
        //     logFilePath.replace(logFilePath.toString().lastIndexOf("\\") + 1,
        //             logFilePath.toString().length(), LOG_NAME + DateUtils.getNowYMDHM() + FAIL_LOG_NAME + Constants.TXT_SUFFIX);
        //     TxtUtil.writeTxt(new File(logFilePath.toString()), "UTF-8", errorList);
        //     logger.info("[0214 采集同步 完成] ~----------当前线程: {}, 处理总人数: {}, 成功人数: {}, 失败人数: {}-----------------------", Thread.currentThread().getName(), totalNum, successList.size(), errorList.size());
        // } catch (IOException e) {
        //     logger.error("[0214 采集同步] 文件复制出错：{}", e);
        // }
    }
}
