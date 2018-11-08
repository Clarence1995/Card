package com.tecsun.card.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tecsun.card.common.ThreadPoolUtil;
import com.tecsun.card.common.clarencezeroutils.*;
import com.tecsun.card.common.excel.ExcelDataFormatter;
import com.tecsun.card.common.excel.ExcelUtil;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.dongruan.DongRuanUserInfoDAO;
import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.po.AZ01PO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.entity.vo.GongAnInfoVO;
import com.tecsun.card.entity.vo.UserInfoVO;
import com.tecsun.card.exception.HttpNetWorkException;
import com.tecsun.card.service.*;
import com.tecsun.card.service.threadrunnable.DongRuanSynchroRunnable;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author 0214
 * @description 数据处理服务
 * @createTime 2018/9/7
 */
@Service("dataHandleService")
public class DataHandleServiceImpl implements DataHandleService {

    private static final String SEPARATOR                      = File.separator;
    private static final String TSB_COLLECT_ROOT_PATH          = "E:\\tecsun\\photo\\personPhoto\\geren\\";
    private final        Logger logger                         = LoggerFactory.getLogger(this.getClass());
    private final        String TXT_SUFFIX                     = ".txt";
    private final        String IMG_SUFFIX                     = ".jpg";
    private final        String EXCEL_SUFFIX                   = ".xlsx";
    private final        String IMG_PATH_TSB                   = "TSBIMG";
    private final        String IMG_PATH_DATABASE              = "DATABASEIMG";
    private final        String LOG                            = "LOG";
    private final        String LOG_ROOT_FILE_NAME_TXT_SUCCESS = "数据处理结果【成功】（TXT）";
    private final        String LOG_ROOT_FILE_NAME_TXT_FAIL    = "数据处理结果【失败】（TXT）";
    private final        String SYNCHRO_NAME                   = "[0214 东软单位名称、单位姓名同步]";
    private final        String LOG_ROOT_FILE_NAME_EXCEL       = "数据处理结果（EXCEL）";
    private final        String COLLECT_DATABASE_NAME          = "COLLECT";
    private final        String CARD_DATABASE_NAME             = "CARD";
    private final        String MID_DATABASE_NAME              = "MID";
    private final        int    SINGLE_NUM                     = 1;
    private final        int    MORE_NUM                       = 2;
    private final        int    NULL_NUM                       = 0;

    @Autowired
    private CardService                cardService;
    @Autowired
    private MidService                 midService;
    @Autowired
    private CollectService             collectService;
    @Autowired
    private DongRuanService            dongRuanService;
    @Autowired
    private SystemService              systemService;
    @Autowired
    private CollectValidateServiceImpl collectValidateService;

    /**
     * @return com.tecsun.card.entity.Result
     * @Description 整理TXT文件里面的人员详情，可以处理采集、卡管中的数据情况
     * 结果会以两种格式的文件返回：1、Excel（记录人员详情）2、TXT简要记录（包含是否存在卡管、是否存在采集、是否拥有TSB照片、基础信息是否合格）
     * @param: filePath  TXT文件路径
     * @param: logRootPath   LOG日志文件路径
     * @param: imgPath   TSB照片路径
     * @param: copyImg   是否复制照片（包括TSB照片、数据库公安照片）
     * @param: eUpdateDatabase 是否更新数据库
     * @author 0214
     * @createTime 2018-09-18 16:05
     * @updateTime
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = Exception.class)
    public Result handleUserInfo(String filePath, String logRootPath, boolean copyImg) {
        Result result = new Result();
        try {
            String parentPath = StringUtils.getRootPath(filePath);
            // 1、读取TXT所有身份证信息
            List<String> cardList = TxtUtil.readLine(filePath, "UTF-8");
            logger.info("~--[0214] 从TXT文件获取总人数为: {} 人,TXT文件路径为: {}", cardList.size(), filePath);
            ArrayList<String> failList    = new ArrayList<>(cardList.size());
            ArrayList<String> successList = new ArrayList<>(cardList.size());
            List<UserInfoVO>  resultList  = new ArrayList<>(cardList.size());
            // 2、循环遍历
            for (String userTxtInfo : cardList) {
                // 卡管存在
                boolean cardExit = false;
                // 采集库存在
                boolean collectExit = false;
                // 用于TXT记录
                boolean validateForTxt = true;
                // 数据详情记录Bean
                UserInfoVO userInfo = new UserInfoVO();
                // 判断是否包含身份证和姓名
                String   name    = "";
                String   idCard  = "";
                String[] strings = userTxtInfo.split("_");
                if (strings.length > 1) {
                    if (strings[0].length() == 18) {
                        idCard = strings[0];
                        name = strings[1];
                    } else {
                        idCard = strings[1];
                        name = strings[0];
                    }
                } else {
                    idCard = userTxtInfo;
                }
                // 成功Stringbuild
                StringBuilder txtStringBuild = new StringBuilder(idCard);
                // ~-------组装Bean
                userInfo.setIdCard(idCard);
                userInfo.setName(name);
                // ~-------组装Bean

                boolean photoResult = true;

                // 1、判断卡管库是否存在此人,并记录是否重复
                // int exit = cardService.userExistInCard(idCard, name);
                int exit = 1;
                if (exit > NULL_NUM) {
                    // 卡管存在
                    userInfo.setCardHas(true);
                    cardExit = true;
                    if (exit > 1) {
                        userInfo.setUserCardRepeat("重复 [卡管库]");
                    } else {
                        userInfo.setUserCardRepeat("不重复 [卡管库]");
                    }
                    Ac01PO ac01PO = cardService.getAC01DetailByIdCardAndName(idCard, name);
                    // ~ 组装
                    userInfo.setUserStatus(ac01PO.getStatus());
                    userInfo.setRegionalCode(ac01PO.getAac301b());
                    userInfo.setName(ac01PO.getAac003());
                    if (null != ac01PO.getCardId()) {
                        AZ01PO az01PO = cardService.getCardByUserId(ac01PO.getCardId());
                        if (ObjectUtils.notEmpty(az01PO)) {
                            // 存在卡
                            userInfo.setECard("存在 [社保卡]");
                            userInfo.setCardStatus(az01PO.getStatus());
                            userInfo.setCardApplyStatus(az01PO.getAAZ502());
                            userInfo.setCardId(az01PO.getId());
                        } else {
                            // 不存在卡
                            userInfo.setECard("不存在 [社保卡]");
                        }
                    } else {
                        // 不存在卡
                        userInfo.setECard("不存在 [社保卡]");
                    }
                    txtStringBuild.append("_" + ac01PO.getAac003() + "_存在卡管库");
                    // txt 记录
                    successList.add(txtStringBuild.toString());
                    userInfo.setCollectHas(true);
                    userInfo.setUserCollectRepeat(false);
                    userInfo.setUserCollectRepeat(false);
                    userInfo.setUserInfoValid(true);
                    userInfo.setDatabaseImgHas(true);
                    userInfo.setTsbImgHas(true);
                    userInfo.setDealMsg(" ");
                    resultList.add(userInfo);
                    continue;
                } else {
                    // 卡管不存在
                    userInfo.setECard("");
                    userInfo.setUserStatus("-1");
                    userInfo.setCardId(0L);
                    userInfo.setRegionalCode("-1");
                    userInfo.setCardStatus("-1");
                    userInfo.setCardApplyStatus(-1);
                    userInfo.setUserCardRepeat("");
                    userInfo.setCardHas(false);
                    txtStringBuild.append("_" + name + "卡管不存在");
                }
                // ~-------卡管库判断完成---------


                // 2、判断采集库是否存在-----------------------
                // 2-1、采集库存在,判断人员基础信息是否合格
                int collectCount = collectService.getCountFromBasicInfo(idCard, name);
                if (collectCount > NULL_NUM) {
                    // 存在采集库
                    collectExit = true;
                    userInfo.setCollectHas(true);
                    if (collectCount > SINGLE_NUM) {
                        // 采集库重复
                        userInfo.setUserCollectRepeat(true);
                    } else {
                        // 采集库不重复
                        userInfo.setUserCollectRepeat(false);
                    }

                    // 获取采集库里面ID最大的做数据判断处理
                    BasicPersonInfo collectUser = collectService.getBasicInfoByIDCard(idCard, name);
                    userInfo.setName(collectUser.getName());
                    txtStringBuild.insert(0, collectUser.getName() + "_");
                    // 2-1、人员信息校验
                    boolean validateResult = collectService.validateuserInfo(collectUser);
                    if (validateResult) {
                        // 基础信息校验合格
                        userInfo.setDealMsg("基础信息合格");
                        userInfo.setUserInfoValid(true);
                        txtStringBuild.append("_基础信息校验合格");
                    } else {
                        // 基础校验失败
                        userInfo.setDealMsg(collectUser.getDealMsg());
                        userInfo.setUserInfoValid(false);
                        validateForTxt = false;
                        txtStringBuild.append("_" + collectUser.getDealMsg());
                    }

                    // 2-2、公安照片判断
                    boolean imgR = false;
                    if (copyImg && !cardExit && collectExit) {
                        // 复制照片
                        StringBuilder sb = new StringBuilder(parentPath);
                        sb.append(SEPARATOR);
                        imgR = getImgFromDatabaseByIdCard(idCard, true, sb.toString());
                    } else {
                        imgR = getImgFromDatabaseByIdCard(idCard, false);
                    }
                    if (imgR) {
                        // 如果照片存在
                        userInfo.setDatabaseImgHas(true);
                        txtStringBuild.append("_存在公安照片");
                    } else {
                        userInfo.setDatabaseImgHas(false);
                    }

                    // 2-3、TSB照片
                    boolean tsbImgR = false;
                    if (copyImg && !cardExit && collectExit) {
                        tsbImgR = getImgFromTSBByIdCard(idCard, copyImg, parentPath);
                    } else {
                        tsbImgR = getImgFromTSBByIdCard(idCard, false);
                    }
                    if (tsbImgR) {
                        userInfo.setTsbImgHas(true);
                        txtStringBuild.append("_存在TSB照片");
                    } else {
                        userInfo.setTsbImgHas(false);
                        validateForTxt = false;
                        txtStringBuild.append("_不存在TSB照片");
                    }
                } else {
                    userInfo.setCollectHas(false);
                    txtStringBuild.append("_不存在采集库");
                    failList.add(txtStringBuild.toString());
                }
                resultList.add(userInfo);


                // Txt日记记录
                if (validateForTxt) {
                    successList.add(txtStringBuild.toString());
                } else {
                    failList.add(txtStringBuild.toString());
                }
            }

            // 记录Excel
            // ~------------Excel修改化修改
            ExcelDataFormatter edf = new ExcelDataFormatter();
            //~-------------cardHas
            Map<String, String> collectHas = new HashMap<>();
            collectHas.put("true", "存在 [采集库]");
            collectHas.put("false", "不存在 [采集库]");

            Map<String, String> databaseImgHas = new HashMap<>();
            databaseImgHas.put("true", "存在 [公安照片]");
            databaseImgHas.put("false", "不存在 [公安照片]");

            Map<String, String> userCollectRepeat = new HashMap<>();
            userCollectRepeat.put("true", "重复 [采集库] ");
            userCollectRepeat.put("false", "不重复 [采集库]");

            Map<String, String> tsbImgHas = new HashMap<>();
            tsbImgHas.put("true", "存在 [TSB照片]");
            tsbImgHas.put("false", "不存在 [TSB照片]");

            Map<String, String> cardHas = new HashMap<>();
            cardHas.put("true", "存在 [卡管库]");
            cardHas.put("false", "不存在 [卡管库]");

            Map<String, String> userInfoValid = new HashMap<>();
            userInfoValid.put("true", "合格 [人员基础信息校验]");
            userInfoValid.put("false", "不合格 [人员基础信息校验]");

            // 采集库是否存在
            edf.set("collectHas", collectHas);
            // 公安照片是否存在
            edf.set("databaseImgHas", databaseImgHas);
            // 采集库是否重复
            edf.set("userCollectRepeat", userCollectRepeat);
            edf.set("tsbImgHas", tsbImgHas);
            edf.set("cardHas", cardHas);
            edf.set("userStatus", Constants.CAED_PERSON_STATUS_DIC);
            edf.set("cardStatus", Constants.CARD_CARD_STATUS_DIC);
            edf.set("cardApplyStatus", Constants.CARD_CARD_CARD_APPLY_DIC);
            edf.set("userInfoValid", userInfoValid);
            // 集合排序
            if (resultList.size() > SINGLE_NUM) {
                Collections.sort(resultList, new Comparator<UserInfoVO>() {
                    @Override
                    public int compare(UserInfoVO o1, UserInfoVO o2) {
                        // 是否存在采集库
                        if (!o1.getCollectHas().equals(o2.getCollectHas())) {
                            return o1.getCollectHas().compareTo(o1.getCollectHas());
                        }
                        // 基础信息是否合格
                        if (!o1.getUserInfoValid().equals(o2.getUserInfoValid())) {
                            return o1.getUserInfoValid().compareTo(o2.getUserInfoValid());
                        }

                        // 是否存在TSB照片
                        if (!o1.getTsbImgHas().equals(o2.getTsbImgHas())) {
                            return o1.getTsbImgHas().compareTo(o2.getTsbImgHas());
                        }
                        // 是否存在公安照片
                        if (!o1.getDatabaseImgHas().equals(o2.getDatabaseImgHas())) {
                            return o1.getDatabaseImgHas().compareTo(o2.getDatabaseImgHas());
                        }
                        // 是否存在卡管卡库
                        if (!o1.getCardHas().equals(o2.getCardHas())) {
                            return o1.getCardHas().compareTo(o2.getCardHas());
                        }

                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }

            MyFileUtils.generateFilePath(logRootPath);
            StringBuilder excelPath = new StringBuilder(logRootPath);
            excelPath.append(SEPARATOR + LOG + SEPARATOR + LOG_ROOT_FILE_NAME_EXCEL + DateUtils.getNowYMDHM() + EXCEL_SUFFIX);
            ExcelUtil.writeToFile(resultList, edf, excelPath.toString());


            //~-------------Txt记录
            StringBuilder txtFilePath = new StringBuilder(logRootPath);
            txtFilePath.append(SEPARATOR);
            txtFilePath.append(LOG);
            txtFilePath.append(SEPARATOR);
            txtFilePath.append(LOG_ROOT_FILE_NAME_TXT_SUCCESS + DateUtils.getNowYMDHM() + TXT_SUFFIX);
            CollectUtils.arrayListSortByLength(successList);
            CollectUtils.arrayListSortByLength(failList);
            // 写入成功TXT文件
            TxtUtil.writeTxt(new File(txtFilePath.toString()), "UTF-8", successList);

            txtFilePath.replace(txtFilePath.toString().lastIndexOf("\\") + 1,
                    txtFilePath.toString().length(), LOG_ROOT_FILE_NAME_TXT_FAIL + DateUtils.getNowYMDHM() + TXT_SUFFIX);
            // 写入失败TXT文件
            TxtUtil.writeTxt(new File(txtFilePath.toString()), "UTF-8", failList);
            logger.info("~[0214 人员数据处理完成] 总人数:{} 人, 成功人数: {} 人, 失败人数: {} 人, 日志文件目录为: {}", cardList.size(), successList.size(), failList.size(), logRootPath + LOG);
            result.setMsg("~[0214 人员数据处理完成] 总人数:" + cardList.size() + " 人, 成功人数:" + successList.size() + " 人, 失败人数: " + failList.size() + " 人, 日志文件目录为: " + logRootPath + LOG);
            result.setStateCode(200);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setStateCode(Constants.EXCEPTION_RESULT_CODE);
            result.setMsg("文本读取错误: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setStateCode(Constants.EXCEPTION_RESULT_CODE);
            result.setMsg("Excel写入错误: " + e.getMessage());
            return result;
        }
    }

    /**
     * 单个人员数据同步同步
     *
     * @param idCard                     身份证号码
     * @param imgFilePath                照片路径文件夹
     * @param eCopyImgFromHadDeal        是否需要复制数据中心处理过的照片到113指定文件夹内
     * @param egetImgFromDatabase        是否需要从本地数据库获取公安照片
     * @param eValidateUserInfo          是否需要校验人员基本信息
     * @param eGetCollectDataFromFourty  是否需要从70获取采集人员信息
     * @param eCompareWithGongAnDatabase 是否需要和公安对比人员信息
     * @param eDeleteAC01User            是否需要删除113AC01人员数据
     * @return
     */
    /**
     * @param idCard                     身份证号码
     * @param egetImgFromDatabase        是否需要从本地数据库获取公安照片
     * @param eValidateUserInfo          是否需要校验人员基本信息
     * @param eGetCollectDataFromFourty  是否需要从40采集库获取数据
     * @param eCompareWithGongAnDatabase 是否需要和公安库进行人员信息比对
     * @param eDeleteAC01User            是否需要删除卡管AC01人员表
     * @param eCopyImgFromHadDeal        是否需要复制数据中心处理过的照片到113指定文件夹内
     * @param eMarkPriority              是否需要标记人员状态为优先
     * @param eZang                      是否为藏区人员数据
     * @param imgFilePath                照片路径文件夹
     * @return
     */
    @Override
    public Result handleCollectSynchro(String idCard,
                                       Boolean egetImgFromDatabase,
                                       Boolean eValidateUserInfo,
                                       Boolean eGetCollectDataFromFourty,
                                       Boolean eCompareWithGongAnDatabase,
                                       Boolean eDeleteAC01User,
                                       Boolean eCopyImgFromHadDeal,
                                       Boolean eMarkPriority,
                                       Boolean eZang,
                                       String imgFilePath
    ) {
        // 1、不为空判断
        if (null == idCard) {
            throw new NullPointerException("[0214 同步] idCard不能为空");
        }
        if (eCopyImgFromHadDeal) {
            if (null == imgFilePath) {
                throw new NullPointerException("[0214 同步] imgFilePath不能为空");
            }
        }
        // 2、定义数据集合
        StringBuilder exceptionBuilder = new StringBuilder(idCard);
        StringBuilder errorLogBuilder  = new StringBuilder(idCard);
        Result        myResult         = new Result();
        // 2-1、专门用来更新采集库的人员状态
        CollectVO updateCollectStatusBean = new CollectVO();
        updateCollectStatusBean.setCertNum(idCard);

        // 3、卡管库是否存在 & 卡管是否删除此人(异常数据处理)
        boolean eUserExist = cardService.userExistJudgeByIdCardAndName(idCard, null);
        if (eUserExist && eDeleteAC01User) {
            // 3-1、如果卡管存在,且需要删除
            try {
                boolean result = cardService.deleteAC01ByIdCardAndName(idCard, null);
                if (result) {
                    // 删除成功,eUserExist置为false
                    eUserExist = false;
                } else {
                    errorLogBuilder.append("_人员删除失败,失败原因: 可能人员不存在,可能传入的身份证为空");
                    myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                    myResult.setData(errorLogBuilder);
                    return myResult;
                }
            } catch (Exception e) {
                logger.error("人员删除异常--身份证号:{},异常原因: {}", idCard, e);
                myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
                exceptionBuilder.append("_人员在AC01表删除时异常");
                myResult.setData(exceptionBuilder);
                return myResult;
            }
        }

        // 3-2、如果卡管存在,则需要将此人状态更新为已同步状态,并直接返回
        if (eUserExist) {
            updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
            updateCollectStatusBean.setDealStaus(Constants.COLLECT_QUALIFIED);
            updateCollectStatusBean.setDealMsg("合格");
            try {
                int a = collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
                if (a > 0) {
                    logger.info("[0214 采集同步] 人员身份证号: {} 在卡管库存在,回写采集库人员状态成功", idCard);
                    myResult.setStateCode(Constants.SUCCESS_RESULT_CODE);
                    return myResult;
                } else {
                    logger.error("[0214 采集同步] 人员身份证号: {} 在卡管库存在,回写采集库人员状态失效,没有人员状态获得更新,记录为异常数据", idCard);
                    errorLogBuilder.append("_回写采集库失败,无数据可更新");
                    myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                    myResult.setData(errorLogBuilder);
                    return myResult;
                }
            } catch (Exception e) {
                logger.error("[0214 采集同步] 人员身份证号: {} 在卡管库存在,回写采集库出现异常,异常原因:{}", idCard, e);
                exceptionBuilder.append("_回写采集库出现异常" + e.getMessage());
                myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
                myResult.setData(exceptionBuilder);
                return myResult;
            }
        }

        // 4、卡管库不存在,进行人员信息对比校验
        // 获取采集人员详情
        BasicPersonInfo basicPersonInfo = null;
        // 4-1、判断采集源数据获取: 如果从70(也指中间库20,因为113数据库连接不了70的采集库,所以我把70的数据复制到中间库20里面)获取
        try {
            basicPersonInfo = collectService.getBasicInfoByIDCard(idCard, null);
            if (null == basicPersonInfo) {
                // 4-6、如果20中间库还没有的话,则标记为异常数据
                logger.error("[0214 采集同步] 人员身份证号: {} 在卡管库存在,回写采集库出现错误,错误原因:{}", idCard, "该人员在40采集库不存在有效信息");
                errorLogBuilder.append("_人员在40采集库不存在有效信息");
                myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                myResult.setData(errorLogBuilder);
                return myResult;
            }

            // if (!eGetCollectDataFromFourty) {
            //     basicPersonInfo = collectService.getSingleBasicPersonByIdcardFromMidTwenty(idCard, null);
            //     if (null == basicPersonInfo) {
            //         // 4-2、中间库20不存在,尝试访问40采集库查看是否有这个人
            //         basicPersonInfo = collectService.getBasicInfoByIDCard(idCard, null);
            //         if (null == basicPersonInfo) {
            //             // 4-3、如果还没有这个人存在,则标记为异常数据
            //             logger.error("[0214 采集同步] 人员身份证号: {} 在卡管库存在,回写采集库出现错误,错误原因:{}", idCard, "该人员在40/70采集库不存在有效信息");
            //             errorLogBuilder.append("_人员在40/70采集库不存在有效信息");
            //             myResult.setStateCode(Constants.FAIL_RESULT_CODE);
            //             myResult.setData(errorLogBuilder);
            //             return myResult;
            //         }
            //     }
            // } else {
            //     // 4-4、从40采集库获取数据
            //     basicPersonInfo = collectService.getBasicInfoByIDCard(idCard, null);
            //     if (null == basicPersonInfo) {
            //         // 4-5、如果采集库40没有此人存在,则尝试访问20中间库
            //         basicPersonInfo = collectService.getSingleBasicPersonByIdcardFromMidTwenty(idCard, null);
            //         if (null == basicPersonInfo) {
            //             // 4-6、如果20中间库还没有的话,则标记为异常数据
            //             logger.error("[0214 采集同步] 人员身份证号: {} 在卡管库存在,回写采集库出现错误,错误原因:{}", idCard, "该人员在40/70采集库不存在有效信息");
            //             errorLogBuilder.append("_人员在40/70采集库不存在有效信息");
            //             myResult.setStateCode(Constants.FAIL_RESULT_CODE);
            //             myResult.setData(errorLogBuilder);
            //             return myResult;
            //         }
            //     }
            // }
        } catch (Exception e) {
            logger.error("[0214 采集同步] 人员身份证号: {} 获取采集库人员数据异常,异常原因:{}", idCard, e);
            exceptionBuilder.append("_获取采集库人员数据异常," + e.getMessage());
            myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
            myResult.setData(exceptionBuilder);
            return myResult;
        }


        // 5、---------------------------------- ~ 基本信息校验 START ----------------------------------
        Ac01PO ac01PO = new Ac01PO();
        ac01PO.setAac147(idCard);
        if (eValidateUserInfo) {
            // 基础信息校验
            boolean checkUserInfo = collectValidateService.checkCollectUserInfo(basicPersonInfo);
            // 区划校验
            ac01PO.setAac301b(basicPersonInfo.getRegionalCode());
            boolean checkRegionalCode = collectValidateService.checkRegionalcode(ac01PO);
            if (!(checkUserInfo && checkRegionalCode)) {
                // 如果校验不成功,则跳出FOR循环,并更新用户信息 同步状态为:0 数据处理状态为 04
                updateCollectStatusBean.setSynchroStatus(Constants.COLLECT_NO_SYNCHRO);
                updateCollectStatusBean.setDealStaus(Constants.COLLECT_USERINFO_ERROR);
                String        userInfoDealMsg     = basicPersonInfo.getDealMsg();
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
                try {
                    // 人员基本信息校验失败,更新采集表人员状态信息
                    int a = collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
                    if (a > 0) {
                        logger.info("[0214 采集同步] 身份证号:{}, 信息校验失败,原因为: {}", idCard, sb.toString());
                        myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                        myResult.setMsg(idCard + "_人员校验数据失败: " + sb.toString());
                        return myResult;
                    } else {
                        myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                        myResult.setMsg(idCard + "_人员校验数据更新失败: 数据库没有更新此人状态");
                        return myResult;
                    }
                } catch (HttpNetWorkException e) {
                    logger.error("[0214 采集同步] 人员身份证号: {} 信息校验失败,人员状态更新异常,异常原因:{}", idCard, e);
                    exceptionBuilder.append("_信息校验失败,人员状态更新异常," + e.getMessage());
                    myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
                    myResult.setData(exceptionBuilder);
                    return myResult;
                } catch (Exception e) {
                    logger.error("[0214 采集同步] 人员身份证号: {} 信息校验失败,人员状态更新异常,异常原因:{}", idCard, e);
                    exceptionBuilder.append("_信息校验失败,人员状态更新异常," + e.getMessage());
                    myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
                    myResult.setData(exceptionBuilder);
                    return myResult;
                }
            }

        } else {
            boolean checkRegionalCode = collectValidateService.checkRegionalcode(ac01PO);
        }
        // 5、---------------------------------- ~ 基本信息校验 END ----------------------------------

        // 6、---------------------------------- ~ 公安信息校验 START ----------------------------------
        String gongAnStatus = basicPersonInfo.getGongAnStatus();
        if (null == gongAnStatus) {
            gongAnStatus = Constants.COLLECT_GONGAN_COMPARE_NO;
        }
        if (gongAnStatus.equals(Constants.COLLECT_GONGAN_COMPARE_YES) || gongAnStatus.equals(Constants.COLLECT_GONGAN_COMPARE_UPDATE)) {

        } else if (gongAnStatus.equals(Constants.COLLECT_GONGAN_COMPARE_EXCEPTION) || gongAnStatus.equals(Constants.COLLECT_GONGAN_COMPARE_NO_DATA)) {
            // 如果为03:失败/04:公安库查无记录
            logger.info("[0214 采集同步] 人员身份证号: {} 公安信息校验失败或公安库查无此人");
            errorLogBuilder.append("_公安信息校验失败或公安库查无此人");
            myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
            myResult.setData(errorLogBuilder);
            return myResult;
        }
        // 如果需要和公安库进行比对,则需要判断是否为00:未校验,才能比对
            if (eCompareWithGongAnDatabase) {
            try {
                if (gongAnStatus.equals(Constants.COLLECT_GONGAN_COMPARE_NO)) {
                    Result compareWithGongAnResult = compareWithGongAnUserInfo(basicPersonInfo);
                    if (Constants.FAIL_RESULT_CODE == compareWithGongAnResult.getStateCode()) {
                        myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                        myResult.setMsg(idCard + "_公安信息比对不过:" + compareWithGongAnResult.getMsg());
                        return myResult;
                    }
                }
            } catch (Exception e) {
                logger.error("[0214 采集同步] 与公安信息对比发生异常。原因: {}", e);
                myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
                if (null != e.getMessage()) {
                    myResult.setMsg(idCard + "_与公安信息对比发生异常" + e.getMessage());
                } else {
                    myResult.setMsg(idCard + "_与公安信息对比发生异常");
                }
                return myResult;
            }
        }
        // 6、---------------------------------- ~ 公安信息校验 END ----------------------------------

        // 7、照片处理。源照片文件夹路径/目标路径/数据库路径
        // 7-1、数据库路径
        String databaseImgPath = File.separator + ac01PO.getAac301() + File.separator + idCard + ".jpg";
        basicPersonInfo.setPhotoUrl(databaseImgPath);
        // 7-2、源路径
        StringBuilder imgSrcPath = null;
        // 7-3、目标路径
        StringBuilder imgDisPath = new StringBuilder(Constants.IMG_113_FILE_ROOT_PATH);
        imgDisPath.append(Constants.SEPARATOR + ac01PO.getAac301() + Constants.SEPARATOR + idCard + Constants.IMG_SUFFIX);
        if (eCopyImgFromHadDeal) {
            boolean eBabyCard = true;
            // 7-3-1、判断是否为婴儿照片
            if (Constants.COLLECT_IS_BABY.equals(basicPersonInfo.getIsBaby())) {
                // 如果为婴儿卡
                eBabyCard = true;
            } else {
                eBabyCard = false;
            }
            if (eBabyCard) {
                // 就把照片复制到默认文件夹
                imgSrcPath = new StringBuilder(PropertyUtils.get("BABAY_IMG_PATH"));
            } else {
                imgSrcPath = new StringBuilder(imgFilePath + Constants.SEPARATOR);
                // 需要复制数据中心处理过的照片
                imgSrcPath.append(ac01PO.getAac147() + Constants.IMG_SUFFIX);
            }
            if (!new File(imgSrcPath.toString()).exists()) {
                logger.error("[0214 采集同步] 人员: {}, 待复制的数据中心处理过的照片不存在: {}", idCard, imgSrcPath.toString());
                myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                myResult.setMsg(idCard + "_待复制的数据中心处理过的照片不存在");
                return myResult;
            } else {
                try {
                    FileUtils.copyFile(new File(imgSrcPath.toString()), new File(imgDisPath.toString()));
                } catch (IOException e) {
                    logger.error("[0214 采集同步] 人员: {}, 复制数据中心照片到指定文件夹出错,原因为: {}", idCard, e);
                }
                logger.info("[0214 采集同步] 人员: {} 照片 (来源于指定文件夹)复制成功, 目标路径为: {} ", idCard, imgDisPath.toString());
            }
        }

        // 7-4、从本地数据库获取公安照片
        if (egetImgFromDatabase) {
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
                        try {
                            collectService.updateUserInfoStatusByIdCardAndName(updateCollectStatusBean);
                        } catch (Exception e) {
                            logger.error("[0214 采集同步] 人员:{}, 更新人员状态出错[不存在公安照片],原因:{}", idCard, e);
                        }
                        myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                        myResult.setMsg(idCard + "_" + "不存在此人公安照片");
                        return myResult;
                    }
                }
            }
            // 写入指定文件夹
            byte[] bytes = midImgDAO.getXp();
            try {
                FileUtils.writeByteArrayToFile(new File(imgDisPath.toString()), bytes);
            } catch (IOException e) {
                logger.error("[0214 采集同步] 人员: {}, 公安照片写入文件夹出错,错误原因: {}", idCard, e);
                myResult.setMsg(idCard + "_公安照片写入文件夹出错");
                myResult.setStateCode(Constants.FAIL_RESULT_CODE);
                return myResult;
            }
        }
        // 8、藏文
        String zangName = collectService.getZangNameByIdCard(idCard);
        if (ObjectUtils.notEmpty(zangName)) {
            basicPersonInfo.setZangName(zangName);
        } else {
            basicPersonInfo.setZangName(Constants.ZANG_NAME_NOT_EXIST);
        }


        // 9、同步
        try {
            // 9-1、人员信息组装
            cardService.assembleAC01(ac01PO, basicPersonInfo, eMarkPriority);
        } catch (Exception e) {
            logger.error("[0214 采集同步: 数据组装出错], 原因: {}", e);
            myResult.setStateCode(Constants.FAIL_RESULT_CODE);
            myResult.setMsg(idCard + "_数据组装出错(单位编号生成出错)");
            return myResult;
        }
        basicPersonInfo.setRegionalCode(ac01PO.getAac301());
        // △ 数据库同步

        // 10、组装
        BusApplyPO busApplyPO = new BusApplyPO();
        // 01 新申领
        busApplyPO.setBusinessType(Constants.BUS_APLY_BUSINESSTYPE_NEW_APPLY);
        // 00 申请
        busApplyPO.setStatus(Constants.BUS_APPLY_STATUS_APPLY);
        // 01 个人申领
        busApplyPO.setSource(Constants.BUS_APPLY_SOURCE_PERSON);
        // 申领表编码
        busApplyPO.setApplyFormCode(Constants.ADMIN + ac01PO.getAac301() + DateUtils.getYYYYMMDDFormatDateStr() + (new Random().nextInt(90) + 10));
        // 区域编码
        busApplyPO.setRegionalId(basicPersonInfo.getRegionalCode());
        busApplyPO.setApplyName(basicPersonInfo.getName());
        busApplyPO.setApplyIdCard(basicPersonInfo.getCertNum());
        busApplyPO.setApplyMobile(basicPersonInfo.getMobile());
        busApplyPO.setFlag(Constants.BUS_APPLY_FLAG_CHOOSE_NUM_NO);
        busApplyPO.setChooseCardNo("");
        // 00 不更换银行卡号
        busApplyPO.setChangeBankNo(Constants.BUS_APPLY_CHANGE_BANK_NUM_NO);
        // 申办人
        // 服务银行
        // 邮件地址
        String expressAddress = basicPersonInfo.getExpressAddress();
        if (null != expressAddress) {
            // 01 邮寄
            busApplyPO.setIsexpress(Constants.BUS_APPLY_EXPRESS_YES);
            busApplyPO.setExpressName(basicPersonInfo.getExpressName());
            busApplyPO.setExpressPhone(basicPersonInfo.getExpressPhone());
            busApplyPO.setExpressAddress(expressAddress);
        } else {
            // 00 不邮寄
            busApplyPO.setIsexpress(Constants.BUS_APPLY_EXPRESS_NO);
        }

        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("BASIC_PERSON_INFO", basicPersonInfo);
        resultMap.put("AC01", ac01PO);
        resultMap.put("BUS_APPLY", busApplyPO);

        myResult.setStateCode(Constants.SUCCESS_RESULT_CODE);
        myResult.setMsg("该人员可以进行同步");
        myResult.setData(resultMap);
        return myResult;

        // 20181024 修改 START ------
        // try {
        //     boolean synchroBean = cardService.insertCardAC01AndBusApplyFromCollect(ac01PO, basicPersonInfo);
        //     if (synchroBean) {
        //         logger.info("[0214 采集同步] 人员: {} 已正常同步完成", idCard);
        //         myResult.setStateCode(Constants.SUCCESS_RESULT_CODE);
        //         myResult.setMsg("同步完成");
        //         return myResult;
        //     } else {
        //         logger.info("[0214 采集同步] 人员: {} 同步失败", idCard);
        //         myResult.setStateCode(Constants.FAIL_RESULT_CODE);
        //         myResult.setMsg("同步失败");
        //         return myResult;
        //     }
        //
        // } catch (Exception e) {
        //     myResult.setStateCode(Constants.EXCEPTION_RESULT_CODE);
        //     if (null != e.getMessage()) {
        //         myResult.setMsg(idCard + "_同步失败" + e.getMessage());
        //     } else {
        //         myResult.setMsg(idCard + "_写入数据库失败");
        //     }
        //     return myResult;
        // }
        // 20181024 修改 START ------

    }


    /**
     * @return com.tecsun.card.entity.Result
     * @Description 处理采集库人员数据重复 人员重复数据同步状态置为9: 表示可删除,卡管已同步置为1,未同步置为0。
     * 如果卡管库的和采集库的人员信息不等,则需要卡管库里面的人员状态是否为异常,如果为异常,则更新信息,并重新申领
     * @param: idCard 身份证号
     * @author 0214
     * @createTime 2018-09-21 15:28
     * @updateTime
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public Boolean handleCollectDateRepeat(String idCard, String logFilePath) throws IOException {

        List<String> errorIds      = new ArrayList<>();
        List<String> errorLogsList = new ArrayList<>();
        // 1、查询重复IDCard
        // List<String> idCardList = collectService.getUserRepeatIdCard();
        // logger.info("[0214 采集库人员重复处理] 当前获取到重复人员数量为: {} 人", idCardList.size());
        // 日志记录
        // 人员重复,卡管库与采集库信息对比全都不过,且状态为19的人员ID,这批人是需要重新更新或删除新增、
        List<String> userExceptionList = new ArrayList<>();

        // 将会被更新状态为0/1的ID
        Long successId = 0L;
        // 是否已和卡管同步标志
        boolean hadSynchro = false;
        // 将会被更新状态为9的Id集合
        List<Long> updateNineList = new ArrayList<>();
        // 错误详情记录(用于日志记录)
        StringBuilder errorLogStringBuilder = new StringBuilder(idCard);

        // BasicPersonInfoDAO updateUserInCollect = new BasicPersonInfoDAO();
        // updateUserInCollect.setCertNum(idCard);

        // 1、获取采集库重复人员详情
        List<BasicPersonInfo> currentUserList = collectService.getUserInfoWithRepeat(idCard, null);

        // 2、获取卡管库中的人员详情
        Ac01PO userInCard = cardService.getAC01DetailByIdCardAndName(idCard, null);
        // 3、比对判断
        if (null != userInCard) {
            // 3-1、如果卡管库存在此人,则比对人员信息
            hadSynchro = true;
            for (BasicPersonInfo basicPersonInfo : currentUserList) {
                boolean result = true;
                // 姓名比对
                if (!basicPersonInfo.getName().equals(userInCard.getAac003())) {
                    errorLogStringBuilder.append("_姓名不匹配");
                    result = false;
                }
                // 身份证号比对
                if (!basicPersonInfo.getCertNum().equals(userInCard.getAac147())) {
                    errorLogStringBuilder.append("_身份证号不匹配");
                    result = false;
                }
                // 手机号比对
                if (!basicPersonInfo.getMobile().equals(userInCard.getAac067())) {
                    errorLogStringBuilder.append("_手机号不匹配");
                    result = false;
                }
                // 区域ID比对
                if (!basicPersonInfo.getRegionalCode().equals(userInCard.getAac301b())) {
                    errorLogStringBuilder.append("_区域ID不匹配");
                    result = false;
                }
                if (!result) {
                    // 如果当条人员数据比对失败,则将此条记录更新为9
                    updateNineList.add(basicPersonInfo.getId());
                    // 继续遍历
                    continue;
                }

                // 集合添加
                if (basicPersonInfo.getId() > successId) {
                    if (!(NULL_NUM == successId)) {
                        updateNineList.add(successId);
                    }
                    successId = basicPersonInfo.getId();
                } else {
                    updateNineList.add(basicPersonInfo.getId());
                }
            }

            // 3-2、卡管库存在,但是所有信息对比不过,则需要判断人员是否为异常状态
            // ①、异常状态: 日志记录
            // ②、非异常状态: 保留最大ID,并日志记录
            if (NULL_NUM == successId) {
                // 如果所有记录对比不过,则只保留最大ID
                if (Constants.USER_STATUS_EXCEPTION.equals(userInCard.getStatus())) {
                    // 人员状态异常,标记该人员数据,为以后重新做匹配
                    userExceptionList.add(idCard + "_所有信息比对失败,且人员状态为异常");
                    // 需要在采集库中保留最大的ID,并标注
                    successId = Collections.max(updateNineList);
                    updateNineList.remove(successId);
                } else {
                    errorLogsList.add(idCard + "_找不到能更新状态为0/1的人员ID");
                    logger.error("人员ID: {}, 找不到能更新状态为0/1的人员ID", idCard);
                    // 需要在采集库中保留最大的ID,并标注
                    successId = Collections.max(updateNineList);
                    updateNineList.remove(successId);
                }
            }
        } else {
            // 4、如果卡管不存在此人,则只保留最大ID即可,其他更新为9
            for (BasicPersonInfo basic : currentUserList) {
                if (successId < basic.getId()) {
                    if (!(NULL_NUM == successId)) {
                        // 如果不为0,则需要把successId加入到更新9的数组中
                        updateNineList.add(successId);
                    }
                    successId = basic.getId();
                } else {
                    updateNineList.add(basic.getId());
                }
            }
        }

        // 5、更新数据库
        try {
            List<Long> successIdList = new ArrayList<>(1);
            successIdList.add(successId);
            // 5-1、更新状态为0/1
            if (hadSynchro) {
                // 已同步 更新采集库同步状态为1,处理状态为01
                int successResult = collectService.updateUserInfoStatusByIdList(successIdList, Constants.COLLECT_HAD_SYNCHRO, Constants.COLLECT_QUALIFIED, "合格");
            } else {
                // 没有同步, 同步状态为 0
                int successResult = collectService.updateUserInfoStatusByIdList(successIdList, Constants.COLLECT_NO_SYNCHRO, null, null);
            }
            // 5-2、更新状态为 9
            int failResult = collectService.updateUserInfoStatusByIdList(updateNineList, Constants.COLLECT_USER_REPEAT, null, null);
            logger.info("[0214 采集库重复人员处理] 人员身份证号: {}, 成功ID: {}, 需要置为9的记录条数: {} 条, ID为:{}",
                    idCard, successId, updateNineList.size(), JSONObject.toJSONString(updateNineList));
        } catch (Exception e) {
            logger.error("[0214 采集库重复人员处理] 人员身份证号: {},更新出错,原因为: {}", idCard, e);
            errorLogsList.add(idCard + "_同步异常");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        // 错误日志记录
        if (ObjectUtils.notEmpty(errorLogsList)) {
            TxtUtil.appendTxt(new File(logFilePath + Constants.SEPARATOR + "[采集库重复人员处理] 异常数据日志" + Constants.TXT_SUFFIX), errorLogsList);
        }
        if (ObjectUtils.notEmpty(userExceptionList)) {
            TxtUtil.appendTxt(new File(logFilePath + Constants.SEPARATOR + "[采集库重复人员处理] 人员重复 卡管采集数据比对全部失败 人员状态异常(19)"
                    + Constants.TXT_SUFFIX), userExceptionList);
        }
        return true;
    }


    /**
     * @return boolean true：TSB照片存在、false：照片不存在
     * @Description 判断TSB照片是否存在并复制TSB照片（可选）
     * @param: idCard        IDCard
     * @param: copyImg       是否复制
     * @param: args          若复制照片、则传入目标路径
     * @author 0214
     * @createTime 2018-09-18 16:15
     * @updateTime
     */
    @Override
    public Boolean getImgFromTSBByIdCard(String idCard, boolean copyImg, String... args) throws IOException {
        String  srcImgPath = TSB_COLLECT_ROOT_PATH + idCard + IMG_SUFFIX;
        boolean result;
        logger.info("[0214 TSB照片复制] 源照片路径：{}", srcImgPath);
        File file = new File(srcImgPath);
        result = file.exists();
        if (copyImg && result) {
            // 复制TSB照片
            String disImgPathRoot = args[0];
            if (!new File(disImgPathRoot).isDirectory()) {
                throw new FileNotFoundException("[0214 TSB照片复制出错] 目标路径并非一个目录！ ");
            }
            StringBuilder disImgPath = new StringBuilder(disImgPathRoot);
            disImgPath.append(SEPARATOR);
            disImgPath.append(IMG_PATH_TSB);
            disImgPath.append(SEPARATOR);
            disImgPath.append(idCard);
            disImgPath.append(IMG_SUFFIX);
            FileUtils.copyFile(file, new File(disImgPath.toString()));
            logger.info("[0214 TSB照片复制成功] 人员ID: {}, 照片目标路径为: {}", idCard, disImgPath);
        }
        return result;
    }

    /**
     * 同步卡管库、采集库单位名称、单位编号(东软)
     *
     * @param databaseName 数据库名称 card:卡管库 / collect: 采集库
     * @param logFilePath  日志文件路径
     * @param threadCount  线程数
     * @return
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public Result handleCollectDepartmentNo(String databaseName, String logFilePath, Integer threadCount) {
        Result result          = new Result();
        int    totalCount      = 0;
        int    threadDealCount = 0;
        logger.info("~ [0214 单位名称、单位编号数据同步] --- STARTING {} -----------------", DateUtils.todayDate1());
        // 1、判断更新哪个数据库
        List<List<BasicPersonInfoDAO>> collectList = null;
        List<List<Ac01DAO>>            cardList    = null;
        if (ObjectUtils.isEmpty(databaseName)) {
            result.setStateCode(Constants.FAIL_RESULT_CODE);
            result.setMsg("[0214 东软单位名称和单位编号更新出错] databaseName 不能为空");
            return result;
        }
        if (databaseName.toUpperCase().equals(COLLECT_DATABASE_NAME)) {
            // 获取采集人员表
            List<BasicPersonInfoDAO> idCardList = collectService.listAllUserIDCard();
            totalCount = idCardList.size();
            collectList = ListThreadUtils.dynamicListThread(idCardList, threadCount);
        } else if (databaseName.toUpperCase().equals(CARD_DATABASE_NAME)) {
            // 获取卡管库人员表
            List<Ac01DAO> idCardList = cardService.listAllUserIdCard();
            totalCount = idCardList.size();
            cardList = ListThreadUtils.dynamicListThread(idCardList, threadCount);
        }
        // 2、开启线程
        if (null != collectList) {
            // 采集库更新
            for (List<BasicPersonInfoDAO> collectListForThread : collectList) {
                ThreadPoolUtil.getThreadPool().execute(new DongRuanSynchroRunnable(collectListForThread, null, this, logFilePath));
            }
            threadDealCount = collectList.get(0).size();
            logger.info("[0214 采集库单位名称、单位编号更新开始] 线程数量:{}, 待处理记录总数为: {} 条, 每个线程处理数量为:{}条", threadCount, totalCount, threadDealCount);
        } else if (null != cardList) {
            // 更新卡管库
            for (List<Ac01DAO> cardListForThread : cardList) {
                ThreadPoolUtil.getThreadPool().execute(new DongRuanSynchroRunnable(null, cardListForThread, this, logFilePath));
            }
            threadDealCount = cardList.get(0).size();
            logger.info("[0214 卡管库单位名称、单位编号更新开始] 线程数量:{}, 待处理记录总数为: {} 条, 每个线程处理数量为:{}条", threadCount, totalCount, cardList.get(0).size());
        }
        result.setStateCode(200);
        result.setMsg("所有线程已完全开启, 现在同步的数据库为:" + databaseName + ", 待同步人数为: " + totalCount + "人, 每个线程处理人数为: " + threadDealCount + "人");
        return result;
    }


    /**
     * 依据东软数据库更新卡管库、采集库单位名称和单位编号
     *
     * @param collectList
     * @param cardList
     * @param logFilePath
     * @return
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public Result handleDongRuanSynchro(List<BasicPersonInfoDAO> collectList, List<Ac01DAO> cardList, String logFilePath) {
        Result       result         = new Result();
        List<String> notExistIdCard = new ArrayList<>();
        // 总数
        int total = 0;
        // 成功人数
        int success = 0;
        // 失败人数
        int fail = 0;
        // 1、判断是哪个数据库,并获取IDCard和姓名
        if (null != collectList) {
            total = collectList.size();
            // 采集库做好准备,更新采集库单位编号
            for (BasicPersonInfoDAO basicPersonInfoDAO : collectList) {
                String userName = basicPersonInfoDAO.getName();
                String idCard   = basicPersonInfoDAO.getCertNum();
                try {
                    DongRuanUserInfoDAO dongRuanUserInfo = dongRuanService.getDongRuanUserInfoByIdAndName(idCard, userName);
                    // DongRuanUserInfoDAO dongRuanUserInfo = new DongRuanUserInfoDAO();
                    // dongRuanUserInfo.setAAB001("test");
                    // dongRuanUserInfo.setAAB004("hello");
                    CollectVO collectVO = new CollectVO();
                    collectVO.setCertNum(idCard);
                    collectVO.setName(userName);
                    if (null != dongRuanUserInfo) {
                        collectVO.setDepartmentNo(dongRuanUserInfo.getAAB001());
                        collectVO.setDepartmentName(dongRuanUserInfo.getAAB004());
                        // 更新采集库人员信息
                        collectVO.setDongRuanSynchroStatus(Constants.DONGRUAN_SYNCHRO_YES);
                        int a = collectService.updateUserInfoStatusByIdCardAndName(collectVO);
                        if (a > 0) {
                            success++;
                            logger.info("{} 采集库同步完成。人员ID: {} ,姓名:{}, 单位名称: {}, 单位编号:{}",
                                    SYNCHRO_NAME, idCard, basicPersonInfoDAO.getName(), dongRuanUserInfo.getAAB004(), dongRuanUserInfo.getAAB001());
                        } else {
                            notExistIdCard.add(idCard + "_" + userName + "东软信息存在,但是写入采集库失败");
                            fail++;
                            logger.info("{} 采集库同步失败。人员ID: {} , 姓名: {}", SYNCHRO_NAME, idCard, userName);
                        }
                    } else {
                        fail++;
                        collectVO.setDongRuanSynchroStatus(Constants.DONGRUAN_SYNCHRO_NO_INFORMATION);
                        int a = collectService.updateUserInfoStatusByIdCardAndName(collectVO);
                        if (a > 0) {
                            logger.info("{} 采集库同步完成。人员ID: {} 在东软视图库不存在", SYNCHRO_NAME, idCard);
                        } else {
                            logger.info("{} 采集库同步失败。人员ID: {} ", SYNCHRO_NAME, idCard);
                        }
                    }
                } catch (Exception e) {
                    fail++;
                    notExistIdCard.add(idCard + "_" + userName);
                    logger.error("{} 采集库同步异常。人员ID: {}, 姓名: {} ,原因: {}", SYNCHRO_NAME, idCard, userName, e);
                }
            }
        } else if (null != cardList) {
            total = cardList.size();
            // 卡管库做好准备
            for (Ac01DAO ac01DAO : cardList) {
                String  userName = ac01DAO.getAAC003();
                String  idCard   = ac01DAO.getAAC147();
                Ac01DAO ac01DAO1 = new Ac01DAO();
                ac01DAO1.setAAC147(idCard);
                ac01DAO1.setAAC003(userName);
                try {
                    DongRuanUserInfoDAO dongRuanUserInfo = dongRuanService.getDongRuanUserInfoByIdAndName(idCard, userName);
                    if (null != dongRuanUserInfo) {
                        ac01DAO1.setAAB001(dongRuanUserInfo.getAAB001());
                        ac01DAO1.setAAB004(dongRuanUserInfo.getAAB004());
                        int a = cardService.updateAC01Status(ac01DAO1);
                        if (a > 0) {
                            success++;
                            logger.info("{} 卡管库 同步完成。 人员ID:{}, 姓名:{}, 单位名称: {}, 单位编号:{} ",
                                    SYNCHRO_NAME, idCard, userName, dongRuanUserInfo.getAAB004(), dongRuanUserInfo.getAAB001());
                        } else {
                            fail++;
                            logger.info("{} 卡管库 同步失败。 人员ID:{} ", SYNCHRO_NAME, idCard);
                        }
                    } else {
                        fail++;
                        logger.info("{} 人员ID:{} 在东软视图中不存在有效信息", SYNCHRO_NAME, idCard);
                    }
                } catch (Exception e) {
                    fail++;
                    notExistIdCard.add(idCard + "_" + userName);
                    logger.error("{} 卡管库同步异常。人员ID: {}, 姓名: {} ,原因: {}", SYNCHRO_NAME, idCard, userName, e);
                }
            }
        }
        String logAbsoluteFilePath = StringUtils.generateAbsoluteFileName(logFilePath, "单位名称 单位编号更新日志", Constants.TXT_SUFFIX);
        notExistIdCard.add("此文件夹为单位名称 单位编号同步异常数据, 人数为" + notExistIdCard.size() + " 此线程同步总人数为: " + total + "成功人数:" + success + "失败人数: " + fail);
        try {
            TxtUtil.writeTxt(new File(logAbsoluteFilePath), "UTF-8", notExistIdCard);
        } catch (IOException e) {
            logger.error("[0214 单位名称、单位编号更新出错,原因: {}", e);
            result.setStateCode(0);
            result.setMsg("[0214 单位名称、单位编号更新出错,原因:" + e);
            return result;
        }

        result.setStateCode(200);
        result.setMsg("成功人数: " + success + ", 失败人数: " + fail + "生成日志文件路径为: " + logAbsoluteFilePath);
        return result;
    }

    @Override
    public Boolean checkCollectUserInfo(BasicPersonInfo userinfo) {
        GongAnInfoVO collectUserInfo = new GongAnInfoVO();
        collectUserInfo.setXM(userinfo.getName());
        collectUserInfo.setSFZH(userinfo.getCertNum());
        collectUserInfo.setXB(userinfo.getSex());
        collectUserInfo.setMZ(userinfo.getNation());
        // collectUserInfo.setCSRQ(userinfo.getBirthday());
        return null;
    }

    /**
     * 依据身份证号码调用公安接口获取用户信息
     *
     * @param idCard
     * @return
     */
    @Override
    public GongAnInfoVO getUserInfoFromGongAnByIdCard(String idCard) throws Exception {
        if (null == idCard) {
            throw new NullPointerException("[0214 通过公安接口获取用户信息异常: 身份证不能为空,不允许查询");
        }
        // 1、从配置文件获取基本信息
        String GONG_AN_ROOT_URL = PropertyUtils.get("GONG_AN_ROOT_URL");
        String serviceId        = PropertyUtils.get("SERVICE_ID_01");
        String senderId         = PropertyUtils.get("SENDER_ID_06");
        String method           = "Query";
        String authorizeInfo    = PropertyUtils.get("SENDER_AUTHORIZE");

        // 3-1、组装操作人员基本信息
        JSONObject operate = new JSONObject();
        operate.put("userId", "540000");
        operate.put("userName", "西藏自治区");
        operate.put("userDept", "540000");
        operate.put("macIp", "192.168.12.185");

        // 3-2、组装endUser
        JSONObject endUser = new JSONObject();
        endUser.put("UserCardId", "540000");
        endUser.put("UserName", "西藏自治区");
        endUser.put("UserDept", "540000");

        // 3-3、组装params(这里可以添加SQL的参数,如Condition: "XM= 'xx'
        JSONObject methodParameter = new JSONObject();
        methodParameter.put("EndUser", endUser);
        methodParameter.put("Method", "Query");
        methodParameter.put("Condition", "SFZH='" + idCard + "'");
        methodParameter.put("OrderItems", "");
        // XM:姓名，XB:性别，MZ:民族，CSRQ:出生日期,SFZH:身份证号码，证件有效期,户口所在地:HKSZD,
        methodParameter.put("RequiredItems", "XM,SFZH,BDYY,BYQK,CSD,CSDGJ,CSDXZ,CSRQ,CYM,"
                + "FWCS,HDQR,HKSZD,HYZK,JGGJ,JGSSX,MZ,QTZZSSXQ,QTZZXZ,SG,WHCD,XB,ZY,"
                + "ZZSSXQ,ZZXZ");
        methodParameter.put("RowsPerPage", "10");
        methodParameter.put("PageNum", "1");
        methodParameter.put("InfoCodeMode", "0");
        methodParameter.put("SourceDataSet", "");

        logger.info("[0214 从公安库获取用户信息] 调用公安接口 START ~~~ 。当前时间: {}", DateUtils.getNowYMDHMSWithCHN());
        logger.info("[0214 调用公安接口入参]: serviceId: {}, senderId:{}, method:{}, authorizeInfo:{}, operate: {}, methodParameter: {},GongAn_URL: {}",
                serviceId, senderId, method, authorizeInfo, JSONObject.toJSONString(operate), JSONObject.toJSONString(methodParameter), GONG_AN_ROOT_URL);
        try {
            String gongAnResult = HttpUtils.post(serviceId, senderId, method, authorizeInfo, operate.toString(), methodParameter, GONG_AN_ROOT_URL);
            logger.info("[0214 公安数据返回:{}", JSONObject.toJSONString(gongAnResult));
            logger.info("[0214 从公安库获取用户信息] 调用公安接口成功。正在解析数据");
            GongAnInfoVO resultBean = new GongAnInfoVO();
            JSONObject   jsonObject = JSONObject.parseObject(gongAnResult);
            JSONObject   payLoad    = jsonObject.getJSONObject("payLoad");
            if (Constants.GONG_AN_SUCCESS_RESULT_CODE.equals(payLoad.get("Code"))) {
                JSONArray result = payLoad.getJSONObject("Message").getJSONArray("Reuoult");
                if (result.size() > 0) {
                    JSONObject user = result.getJSONObject(0);
                    resultBean.setXM(user.getString("XM"));
                    resultBean.setCSRQ(Long.valueOf(user.getString("CSRQ")));
                    resultBean.setMZ(user.getString("MZ"));
                    resultBean.setXB(Integer.valueOf(user.getString("XB")));
                    resultBean.setHKSZD(user.getString("HKSZD"));
                    resultBean.setSFZH(user.getString("SFZH"));
                    logger.info("[0214 从公安库获取用户信息] 用户信息解析成功。用户详情{}", JSONObject.toJSONString(resultBean));
                    return resultBean;
                }
            }
        } catch (Exception e) {
            logger.error("[0214 从公安库获取用户信息] 调用公安接口异常");
            throw e;
        }
        return null;
    }

    /**
     * 和公安数据库进行比对
     *
     * @param userInfo
     * @return
     */
    @Override
    public Result compareWithGongAnUserInfo(BasicPersonInfo userInfo) throws Exception {
        // 1、获取用户身份证号码
        boolean       compareResult = true;
        String        certNum       = userInfo.getCertNum();
        StringBuilder resultSb      = new StringBuilder();
        Result        result        = new Result();
        if (null == certNum) {
            throw new NullPointerException("[0214 公安数据比对] 待对比用户身份证号不能为空");
        }
        GongAnInfoVO gongAnInfo = getUserInfoFromGongAnByIdCard(userInfo.getCertNum());
        if (null == gongAnInfo) {
            throw new NullPointerException("公安数据库获取不到此人信息");
        }

        if (!gongAnInfo.getXM().equals(userInfo.getName())) {
            resultSb.append("_姓名对比不过");
            compareResult = false;
        }
        if (!gongAnInfo.getMZ().equals(userInfo.getNation())) {
            resultSb.append("_民族比对不过");
            compareResult = false;
        }
        if (!gongAnInfo.getXB().equals(userInfo.getSex())) {
            resultSb.append("_性别比对不过");
            compareResult = false;
        }
        if (!gongAnInfo.getCSRQ().equals(userInfo.getBirthday())) {
            resultSb.append("_出生日期比对不过");
            compareResult = false;
        }
        if (compareResult) {
            result.setStateCode(Constants.SUCCESS_RESULT_CODE);
            result.setMsg("公安比对成功");
            return result;
        } else {
            result.setStateCode(Constants.FAIL_RESULT_CODE);
            result.setMsg(resultSb.toString());
            return result;
        }
    }

    /**
     * 批量获取西藏公民身份信息(批量只适用于西藏公民)
     *
     * @param idCardList
     * @return
     * @throws Exception
     */
    @Override
    public List<GongAnInfoVO> getUserInfoFromGongAnByIdCardList(List<GongAnInfoVO> userInfoList) throws Exception {
        String GONG_AN_ROOT_URL = PropertyUtils.get("GONG_AN_ROOT_URL");
        String serviceId        = PropertyUtils.get("SERVICE_ID_01");
        // 批量
        String senderId      = PropertyUtils.get("SERVICE_ID_08");
        String method        = "DataMatching";//调用的方法（固定参数）
        String authorizeInfo = PropertyUtils.get("SENDER_AUTHORIZE");
        // 1、
        JSONObject endUser = new JSONObject();
        endUser.put("UserCardId", "540000");
        endUser.put("UserName", "西藏自治区");
        endUser.put("UserDept", "540000");
        // 2、
        JSONObject methodParameter = new JSONObject();
        // 用户信息
        methodParameter.put("EndUser", endUser);
        // 调用的方法（固定参数）
        methodParameter.put("Method", method);
        // 查询条件（标准sql）
        methodParameter.put("Condition", "GMSFHM='GMSFHM'");
        // 请求字段(多个，隔开)
        methodParameter.put("RequiredItems", "XM,GMSFHM,CSRQ,MZ,XB,HKSZD");
        // 交换服务方
        methodParameter.put("ReceiveServiceID", "");

        // 3、
        JSONObject operate = new JSONObject();
        operate.put("userId", "540000");
        operate.put("userName", "西藏自治区");
        operate.put("userDept", "540000");
        operate.put("macIp", "192.168.12.185");
        JSONArray jssonArray = new JSONArray();

        for (GongAnInfoVO user : userInfoList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("GMSFHM", user.getSFZH());
            jsonObject.put("MX", user.getXM());
            jssonArray.add(jsonObject);
        }
        methodParameter.put("SourceDataSet", jssonArray);//比对数据集
        // 表码翻译模式（0:代码；1:名称；2:代码/名称）
        methodParameter.put("InfoCodeMode", "0");

        logger.info("[0214 从公安库获取用户信息] 调用公安接口 START ~~~ 。当前时间: {}", DateUtils.getNowYMDHMSWithCHN());
        logger.info("[0214 调用公安接口入参]: serviceId: {}, senderId:{}, method:{}, authorizeInfo:{}, operate: {}, methodParameter: {},GongAn_URL: {}",
                serviceId, senderId, method, authorizeInfo, JSONObject.toJSONString(operate), JSONObject.toJSONString(methodParameter), GONG_AN_ROOT_URL);
        String res = HttpUtils.post(serviceId,
                senderId, method, authorizeInfo, operate.toString(), methodParameter, GONG_AN_ROOT_URL);
        logger.info("[0214 公安接口返回数据成功。{}", res);

        JSONObject         jObject        = JSONObject.parseObject(res);
        JSONObject         payLoad        = jObject.getJSONObject("payLoad");
        List<GongAnInfoVO> resultBeanList = new ArrayList<>(userInfoList.size());
        if (Constants.GONGAN_SUCCESS_RESULT.equals(jObject.getJSONObject("Code"))) {
            JSONObject json = payLoad.getJSONObject("Message");
            if (Integer.parseInt(json.getString("TotalNum")) > 0) {
                JSONArray dataSet = json.getJSONArray("Reuoult");
                for (Object o : dataSet) {
                    JSONObject   data         = (JSONObject) o;
                    String       xm           = data.getString("MX");
                    String       sfzh         = data.getString("GMSFHM");
                    String       mz           = data.getString("MZ");
                    String       xb           = data.getString("XB");
                    String       hkszd        = data.getString("HKSZD");
                    GongAnInfoVO gongAnInfoVO = new GongAnInfoVO();
                    gongAnInfoVO.setXM(xm);
                    gongAnInfoVO.setXB(Integer.valueOf(xb));
                    gongAnInfoVO.setSFZH(sfzh);
                    gongAnInfoVO.setMZ(mz);
                    gongAnInfoVO.setHKSZD(hkszd);
                    resultBeanList.add(gongAnInfoVO);
                }
            }
        }
        return resultBeanList;
    }

    /**
     * 处理人员批量同步
     *
     * @param userList
     * @param busApplyList
     * @param basicPersonInfoList
     * @return
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public Result handleSynchroByUserListBusList(List<Ac01PO> userList, Map<String, BusApplyPO> busApplyMap, BasicPersonInfoDAO basicInfo) throws Exception {
        Result result = new Result();
        if (!ObjectUtils.notEmpty(userList)) {
            throw new NullPointerException("[0214] userList不能为空");
        }
        try {
            if (userList.size() != busApplyMap.size()) {
                result.setStateCode(Constants.FAIL_RESULT_CODE);
                result.setMsg("AC01和BUS_APPLY集合数量对应不上");
                result.setData(userList);
                return result;
            }
            // 1、根据数组大小获取相对应的 AC01id
            int        userCount  = userList.size();
            List<Long> ac01IdList = cardService.getAC01SequenceBatch(userCount);
            // 2、装配
            for (int i = 0; i < userCount; i++) {
                userList.get(i).setIdKey(ac01IdList.get(i));
                busApplyMap.get(userList.get(i).getAac147()).setPersonId(ac01IdList.get(i));
            }
            int a = cardService.insertAc01Batch(userList);

            // 3、遍历,获取BUS
            List<BusApplyPO>       busApplyPOList = new ArrayList<>(busApplyMap.size());
            Collection<BusApplyPO> values         = busApplyMap.values();
            Iterator<BusApplyPO>   iterator       = values.iterator();
            while (iterator.hasNext()) {
                busApplyPOList.add(iterator.next());
            }

            int b = cardService.insertBusApplyBatch(busApplyPOList);
            int c = collectService.updateUserInfoStatusByIdList(basicInfo.getIdList(), basicInfo.getSynchroStatus(), basicInfo.getDealStatus(), basicInfo.getDealMsg());

            result.setStateCode(Constants.SUCCESS_RESULT_CODE);
            result.setMsg("批量插入成功");
            result.setData(a);

            return result;
        } catch (Exception e) {
            logger.error("[0214 AC01表、BUS_APPLY表、BASIC_PERSON_INFO表 批量插入异常。异常原因: {}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setStateCode(Constants.EXCEPTION_RESULT_CODE);
            result.setMsg("批量插入异常" + e.getMessage());
            result.setData(userList);
            return result;
        }
    }


    /**
     * @return boolean     true：存在公安照片、false：不存在公安照片
     * @Description 判断并从数据库复制照片到指定文件夹
     * @param: idCard      IDCard
     * @param: copyImg     是否复制公安照片
     * @param: args        若复制公安，需传入目标文件路径
     * @author 0214
     * @createTime 2018-09-18 16:20
     * @updateTime
     */
    @Override
    public Boolean getImgFromDatabaseByIdCard(String idCard, boolean copyImg, String... args) throws IOException {
        boolean result       = true;
        String  distFilePath = "";
        // 1、查询公安表
        MidImgDAO midImgDAO = midService.getImgFromGONGAN(idCard);
        if (null == midImgDAO) {
            // 2、查询GAT表
            midImgDAO = midService.getImgFromGAT12(idCard);
            if (null == midImgDAO) {
                // 3、查询COLLECT_PHOTO表
                midImgDAO = midService.getImgFromCOLLECTPHOTO(idCard);
                if (null == midImgDAO) {
                    result = false;
                }
            }
        }
        if (result && copyImg) {
            // 并且需要复制照片,那就复制吧
            byte[] bytes = midImgDAO.getXp();
            distFilePath = args[0];
            StringBuilder sb = new StringBuilder(distFilePath);
            sb.append(SEPARATOR);
            sb.append(IMG_PATH_DATABASE);
            sb.append(SEPARATOR);
            sb.append(idCard);
            sb.append(IMG_SUFFIX);
            distFilePath = sb.toString();
            try {
                FileUtils.writeByteArrayToFile(new File(distFilePath), bytes);
            } catch (IOException e) {
                String msg = "";
                if (null != e.getMessage()) {
                    msg = e.getMessage();
                } else {
                    msg = "[0214 公安照片复制出错] 人员ID " + idCard;
                }
                logger.error("[0214 公安照片复制出错] 人员ID： {} " + idCard);
                throw new IOException(msg);
            }
            logger.info("[0214 公安照片复制成功] 人员: {} ,数据库存在公安照片, 复制目标文件夹路径为: {}", idCard, distFilePath);
        }
        return result;
    }


}
