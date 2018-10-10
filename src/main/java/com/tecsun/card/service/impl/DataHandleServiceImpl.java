package com.tecsun.card.service.impl;

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
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.entity.vo.UserInfoVO;
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
    public static void main(String[] args) {
        List<Long> test = new ArrayList<>(4);
        test.add(0L);
        test.add(1L);
        test.add(7L);
        test.add(2L);
        Long result = Collections.max(test);
        System.out.println(result);

        System.out.println(4L - 0L);
    }

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
    private static       int    FAIL_CODE                      = 0;
    private static       int    SUCCESS_CODE                   = 200;
    private final        int    SINGLE_NUM                     = 1;
    private final        int    MORE_NUM                       = 2;
    private final        int    NULL_NUM                       = 0;


    @Autowired
    private CardService     cardService;
    @Autowired
    private MidService      midService;
    @Autowired
    private CollectService  collectService;
    @Autowired
    private DongRuanService dongRuanService;

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
                    BasicPersonInfoPO collectUser = collectService.getBasicInfoByIDCard(idCard, name);
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
            result.setStateCode(FAIL_CODE);
            result.setMsg("文本读取错误: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setStateCode(FAIL_CODE);
            result.setMsg("Excel写入错误: " + e.getMessage());
            return result;
        }
    }


    /**
     * @return com.tecsun.card.entity.Result
     * @Description 处理采集库人员数据重复 人员重复数据同步状态置为9: 表示可删除,卡管已同步置为1,未同步置为0。
     * 如果卡管库的和采集库的人员信息不等,则需要卡管库里面的人员状态是否为异常,如果为异常,则更新信息,并重新申领
     * @param: logFilePath
     * @param: threadCount
     * @author 0214
     * @createTime 2018-09-21 15:28
     * @updateTime
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public Result handleCollectDateRepeat(String logFilePath, Integer threadCount) throws IOException {
        List<String> errorIds = new ArrayList<>();
        // 1、查询重复IDCard
        // 46933
        List<String> idCardList = collectService.getUserRepeatIdCard();
        logger.info("[0214 采集库人员重复处理] 当前获取到重复人员数量为: {} 人", idCardList.size());
        // 日志记录
        List<String> errorLogsList = new ArrayList<>();
        // 人员重复,卡管库与采集库信息对比全都不过,且状态为19的人员ID,这批人是需要重新更新或删除新增、
        List<String> userExceptionList = new ArrayList<>();
        // 2、使用单线程处理数据
        for (String idCard : idCardList) {
            try {
                // 将会被更新状态为0/1的ID
                Long    successId  = 0L;
                boolean hadSynchro = false;
                // 将会被更新状态为9的Id集合
                List<Long>         errorIdList         = new ArrayList<>();
                StringBuilder      sb                  = new StringBuilder(idCard);
                BasicPersonInfoDAO updateUserInCollect = new BasicPersonInfoDAO();
                updateUserInCollect.setCertNum(idCard);
                // ①、获取人员详情
                List<BasicPersonInfoPO> currentUserList = collectService.getUserInfoWithRepeat(idCard, null);
                // ②、获取卡管库中的人员详情
                Ac01PO userInCard = cardService.getAC01DetailByIdCardAndName(idCard, null);
                if (null != userInCard) {
                    // ③、如果有,则遍历对比
                    hadSynchro = true;
                    for (BasicPersonInfoPO basicPersonInfoPO : currentUserList) {
                        boolean result = true;
                        // 姓名比对
                        if (!basicPersonInfoPO.getName().equals(userInCard.getAac003())) {
                            sb.append("_姓名不匹配");
                            result = false;
                        }
                        // 身份证号比对
                        if (!basicPersonInfoPO.getCertNum().equals(userInCard.getAac147())) {
                            sb.append("_身份证号不匹配");
                            result = false;
                        }
                        // 手机号比对
                        if (!basicPersonInfoPO.getMobile().equals(userInCard.getAac067())) {
                            sb.append("_手机号不匹配");
                            result = false;
                        }
                        // 区域ID比对
                        if (!basicPersonInfoPO.getRegionalCode().equals(userInCard.getAac301b())) {
                            sb.append("_区域ID不匹配");
                            result = false;
                        }
                        if (!result) {
                            errorIdList.add(basicPersonInfoPO.getId());
                            continue;
                        }
                        if (basicPersonInfoPO.getId() > successId) {
                            if (!(NULL_NUM == successId)) {
                                errorIdList.add(successId);
                            }
                            successId = basicPersonInfoPO.getId();
                        } else {
                            errorIdList.add(basicPersonInfoPO.getId());
                        }
                    }

                    // 卡管库存在,但是所有信息对比不过,则需要判断人员是否为异常状态
                    if (NULL_NUM == successId) {
                        if (Constants.USER_STATUS_EXCEPTION.equals(userInCard.getStatus())) {
                            // 记录数据
                            userExceptionList.add(idCard + "_所有信息比对失败,且人员状态为异常");
                            // 我们就需要在采集库中保留最大的ID,并标注
                            successId = Collections.max(errorIdList);
                            errorIdList.remove(successId);
                        } else {
                            // 我们就需要在采集库中保留最大的ID,并标注
                            successId = Collections.max(errorIdList);
                            errorIdList.remove(successId);
                            if (NULL_NUM == successId) {
                                errorLogsList.add(idCard + "_找不到能更新状态为0/1的人员ID");
                                logger.error("人员ID: {}, 找不到能更新状态为0/1的人员ID", idCard);
                                continue;
                            }
                        }
                    }

                } else {
                    // ②、更新人员状态ID最大的那一个
                    for (BasicPersonInfoPO basic : currentUserList) {
                        if (successId < basic.getId()) {
                            if (!(NULL_NUM == successId)) {
                                errorIdList.add(successId);
                            }
                            successId = basic.getId();
                        } else {
                            errorIdList.add(basic.getId());
                        }
                    }
                }

                // ④ 更新数据库
                List<Long> successIdList = new ArrayList<>(1);
                successIdList.add(successId);
                try {
                    if (hadSynchro) {
                        // 已同步 更新采集库同步状态为1,处理状态为01
                        int successResult = collectService.updateUserInfoStatusByIdList(successIdList, Constants.COLLECT_HAD_SYNCHRO, Constants.COLLECT_QUALIFIED, "合格");
                    } else {
                        // 没有同步, 同步状态为 0
                        int successResult = collectService.updateUserInfoStatusByIdList(successIdList, Constants.COLLECT_NO_SYNCHRO, null, null);
                    }
                    int failResult = collectService.updateUserInfoStatusByIdList(errorIdList, Constants.COLLECT_USER_REPEAT, null, null);
                    logger.info("[0214 采集库重复人员处理] 人员ID: {}, 成功ID: {}, 需要置为9的记录条数: {} 条,分别为:{}", idCard, successId, errorIdList.size(), errorIdList.size(), JSONObject.toJSONString(errorIdList));
                } catch (Exception e) {
                    logger.error("[0214 采集库重复人员处理出错: {}", e);
                    errorLogsList.add(idCard + "_同步异常");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } catch (Exception e) {
                logger.error("[0214 采集库重复人员处理出错] 原因为: {}", e);
                errorLogsList.add(idCard + "_同步异常");
            }
        }
        // 错误日志记录
        TxtUtil.writeTxt(new File(logFilePath + Constants.SEPARATOR + "[采集库重复人员处理] 异常数据日志" + Constants.TXT_SUFFIX), "UTF-8", errorLogsList);
        TxtUtil.writeTxt(new File(logFilePath + Constants.SEPARATOR + "[采集库重复人员处理] 人员重复 卡管采集数据比对全部失败 人员状态异常(19) " + Constants.TXT_SUFFIX), "UTF-8", userExceptionList);
        Result result = new Result();
        result.setStateCode(200);
        result.setMsg("[0214 采集重复人员处理完成,处理人员总数为: " + idCardList.size() + "失败总数为: " + errorLogsList.size());
        return result;
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
    public boolean getImgFromTSBByIdCard(String idCard, boolean copyImg, String... args) throws IOException {
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
            collectList = ListThreadUtil.dynamicListThread(idCardList, threadCount);
        } else if (databaseName.toUpperCase().equals(CARD_DATABASE_NAME)) {
            // 获取卡管库人员表
            List<Ac01DAO> idCardList = cardService.listAllUserIdCard();
            totalCount = idCardList.size();
            cardList = ListThreadUtil.dynamicListThread(idCardList, threadCount);
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
                    // DongRuanUserInfoDAO dongRuanUserInfo = dongRuanService.getDongRuanUserInfoByIdAndName(idCard, userName);
                    DongRuanUserInfoDAO dongRuanUserInfo = new DongRuanUserInfoDAO();
                    dongRuanUserInfo.setAAB001("test");
                    dongRuanUserInfo.setAAB004("hello");
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
    public boolean getImgFromDatabaseByIdCard(String idCard, boolean copyImg, String... args) throws IOException {
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
