package com.tecsun.card.service.impl;

import com.tecsun.card.common.ThreadPoolUtil;
import com.tecsun.card.common.clarencezeroutils.*;
import com.tecsun.card.common.excel.ExcelDataFormatter;
import com.tecsun.card.common.excel.ExcelUtil;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.po.AZ01PO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.UserInfoVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.DataHandleService;
import com.tecsun.card.service.MidService;
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
    private final        String LOG_ROOT_FILE_NAME_EXCEL       = "数据处理结果（EXCEL）";
    private static       int    FAIL_CODE                      = 0;
    private static       int    SUCCESS_CODE                   = 200;
    private final        int    SINGLE_NUM                     = 1;
    private final        int    MORE_NUM                       = 2;
    private final        int    NULL_NUM                       = 0;


    @Autowired
    private CardService    cardService;
    @Autowired
    private MidService     midService;
    @Autowired
    private CollectService collectService;

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
     * @Description  处理采集库人员数据重复 人员重复数据同步状态置为9: 表示可删除,卡管已同步置为1,未同步置为0
     * @param: logFilePath
    * @param: threadCount
     * @return  com.tecsun.card.entity.Result
     * @author  0214
     * @createTime 2018-09-21 15:28
     * @updateTime
     */
    @Override
    public Result handleCollectDateRepeat(String logFilePath, Integer threadCount) {
        // 1、查询重复IDCard
        // 46933
        List<String> idCardList = collectService.getUserRepeatIdCard();
        // 2、使用单线程处理数据
        List<String> errorStrList = new ArrayList<>();
        for (String idCard : idCardList) {
            Long       successId   = 0L;
            boolean hadSynchro = false;
            List<Long> errorIdList = new ArrayList<>();
            BasicPersonInfoDAO updateUserInCollect = new BasicPersonInfoDAO();
            updateUserInCollect.setCertNum(idCard);
            // ①、获取人员详情
            List<BasicPersonInfoPO> currentUserList = collectService.getUserInfoWithRepeat(idCard, null);
            // ②、获取卡管库中的人员详情
            Ac01PO userInCard = cardService.getAC01DetailByIdCardAndName(idCard, null);
            // ③、如果有,则遍历对比,没有,则更新状态
            if (null != userInCard) {
                hadSynchro = true;
                for (BasicPersonInfoPO basicPersonInfoPO : currentUserList) {
                    boolean result = true;
                    // 比对 姓名、身份证号、采集区域、手机号码、有效日期
                    // 姓名比对
                    if (!basicPersonInfoPO.getName().equals(userInCard.getAac003())) {
                        result = false;
                    }
                    if (!basicPersonInfoPO.getCertNum().equals(userInCard.getAac147())) {
                        result = false;
                    }
                    if (!basicPersonInfoPO.getMobile().equals(userInCard.getAac067())) {
                        result = false;
                    }

                    if (!result) {
                        errorIdList.add(basicPersonInfoPO.getId());
                    }
                }
            } else {
                // 更新人员状态ID最大的那一个
                for (BasicPersonInfoPO basic : currentUserList) {
                    if (successId < basic.getId()) {
                        if (successId != 0L) {
                            errorIdList.add(basic.getId());
                        }
                    }
                }
            }

            // ④ 更新数据库
            List<Long> successIdList = new ArrayList<>(1);
            successIdList.add(successId);
            if (hadSynchro) {
                // 已同步 更新采集库同步状态为1,处理状态为01
                int successResult = collectService.updateUserInfoStatusByIdList(successIdList, Constants.COLLECT_HAD_SYNCHRO, Constants.COLLECT_QUALIFIED, "合格");
            } else {
                // 没有同步, 同步状态为 0
                int successResult = collectService.updateUserInfoStatusByIdList(successIdList, Constants.COLLECT_NO_SYNCHRO, null, null);
            }
            int failResult = collectService.updateUserInfoStatusByIdList(errorIdList, Constants.COLLECT_USER_REPEAT, null, null);
            logger.info("[0214 采集库重复人员处理] 人员ID: {}, 成功ID: {}, 需要置为9的记录数量: {}", idCard, successId, errorIdList.size(), errorIdList.size());
        }

        return null;
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
