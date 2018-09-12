package com.tecsun.card.service.impl;

import com.tecsun.card.common.clarencezeroutils.DateUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 0214
 * @createTime 2018/9/7
 * @description
 */
@Service("dataHandleService")
public class DataHandleServiceImpl implements DataHandleService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String         TXTSUFFIX = ".txt";
    private static final String SEPARATOR = File.separator;
    @Autowired
    private              CardService    cardService;
    @Autowired
    private              MidService     midService;
    @Autowired
    private              CollectService collectService;

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = Exception.class)
    public Result handleUserInfo(String filePath, String logPath, String imgPath) {
        Result result = new Result();
        try {
            String parentPath = filePath.trim().substring(0, filePath.lastIndexOf("\\") + 1);
            List<String> cardList = TxtUtil.readLine(filePath, "UTF-8");
            logger.info("[0214] 从TXT文件获取人数为: {}" , cardList.size());
            ArrayList<String> errorList   = new ArrayList<>();
            ArrayList<String> successList = new ArrayList<>();
            for (String card : cardList) {
                // 判断是否包含身份证和姓名
                String name = "";
                String idCard = "";
                String[] strings = card.split("_");
                if (strings.length > 1) {
                    if (strings[0].length() == 18) {
                        idCard = strings[0];
                        name = strings[1];
                    } else {
                        idCard = strings[1];
                        name = strings[0];
                    }
                } else {
                    idCard = card;
                }

                // 用做日志记录
                StringBuilder str = new StringBuilder();
                str.append(idCard + "_" + name);
                boolean photoResult = true;
                // 1、判断卡管库是否存在此人
                boolean exit = cardService.userExistInCard(idCard, name);
                if (exit) {
                    successList.add(idCard + "_" + name + "_卡管已存在");
                    continue;
                }

                // 2、判断采集库是否存在
                BasicPersonInfoPO collectUser = collectService.getBasicInfoByIDCard(idCard);
                if (!ObjectUtils.notEmpty(collectUser)) {
                    errorList.add(idCard + "_" + name + "_采集库不存在");
                    continue;
                }

                // 3、判断人员基础信息 采集库存在、卡管库不存在
                boolean validateResult = collectService.validateBasicPersonInfo(collectUser);
                if (validateResult) {
                    str.append("_信息校验成功");
                } else {
                    photoResult = false;
                    str.append("_" + collectUser.getDealMsg());
                }
                // 4、判断数据库是否存在照片
                MidImgDAO midImgDAO = midService.getImgFromGONGAN(idCard);
                if (null == midImgDAO) {
                    // ②如果为空,则查询表 COLLECT_PHOTO
                    midImgDAO = midService.getImgFromGAT12(idCard);
                    if (null == midImgDAO) {
                        // ③如果为空,则查询表 GAT12
                        midImgDAO = midService.getImgFromCOLLECTPHOTO(idCard);
                        if (null == midImgDAO) {
                            str.append("_不存在公安照片");
                            photoResult = false;
                        }
                    }
                }
                // 数据库照片写入指定文件夹
                if (null != midImgDAO) {
                    byte[] bytes = midImgDAO.getXp();
                    StringBuilder sb = new StringBuilder(parentPath);
                    sb.append(SEPARATOR);
                    sb.append("database");
                    sb.append(SEPARATOR);
                    sb.append(idCard);
                    sb.append(".jpg");
                    FileUtils.writeByteArrayToFile(new File(sb.toString()), bytes);
                    str.append("_照片成功写入文件夹");
                } else {
                    // 查找TSB照片
                    String imgSrcPath = imgPath + SEPARATOR  + idCard + ".jpg";
                    if (new File(imgSrcPath).exists()) {
                        StringBuilder sb = new StringBuilder(parentPath);
                        sb.append(SEPARATOR);
                        sb.append("TSB");
                        sb.append(SEPARATOR);
                        sb.append(idCard);
                        sb.append(".jpg");
                        FileUtils.copyFile(new File(imgSrcPath), new File(sb.toString()));
                        str.append("_存在TSB照片 并复制成功");
                    } else {
                        photoResult = false;
                        str.append("_不存在TSB照片不存在");
                    }
                }
                if (photoResult) {
                    successList.add(str.toString());
                } else {
                    errorList.add(str.toString());
                }

            }
            StringBuilder sb = new StringBuilder(logPath);
            sb.append(SEPARATOR);
            sb.append("log");
            sb.append(SEPARATOR);
            TxtUtil.writeTxt(new File(sb.toString() + "success.txt"), "UTF-8", successList);
            TxtUtil.writeTxt(new File(sb.toString() + "fail.txt"), "UTF-8", errorList);
            result.setStateCode(200);
            result.setData("成功人数: " + successList.size() + " 失败人数: " + errorList.size());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setStateCode(0);
            result.setMsg("文本读取错误: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
    }

}
