package com.tecsun.card.controller;

import com.tecsun.card.common.ThreadPoolUtil;
import com.tecsun.card.common.clarencezeroutils.ListThreadUtil;
import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.controller.threadtask.synchro.cardsynchro.CollectToCardSynchroThreadTask;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.MidService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("synchro")
public class CollectToCardUserSynchroController {
    private final Logger         logger = LoggerFactory.getLogger(CollectToCardUserSynchroController.class);
    @Autowired
    private       CollectService collectService;

    @Autowired
    private CardService cardService;

    @Autowired
    private MidService midService;



    @RequestMapping(value = "collecttocard", method = RequestMethod.POST)
    @ResponseBody
    public Result collectToCard(@RequestBody CollectVO collectVO) {
        Result result  = new Result();
        String imgPath = collectVO.getImgPath();

        int threadCount = 10;
        logger.info("[采集库 => 正式库] SYNCHRO START!");
        if (threadCount > 20) {
            result.setMsg("[采集库 => 正式库] 线程数据太多,不予开启");
        }
        if (threadCount == 0) {
            logger.info("[采集库-正式库] 使用默认线程数量: 10");
        } else {
            threadCount = collectVO.getThreadCount();
        }

        List<BasicPersonInfoPO>       qualifiedUserList = collectService.listQualifiedBasicPerson(collectVO);
        List<List<BasicPersonInfoPO>> basicPersonList   = ListThreadUtil.dynamicListThread(qualifiedUserList, threadCount);
        for (List<BasicPersonInfoPO> basicPersonInfoPOS : basicPersonList) {
            ThreadPoolUtil.getThreadPool().execute(new CollectToCardSynchroThreadTask(collectService, cardService, midService, basicPersonInfoPOS, null, imgPath, null, false));
        }
        return null;
    }

    /**
     * 用于[采集库 => 卡管库] 人员数据同步
     * 1、可以指定线程池大小
     * 2、可以指定照片路径
     * 3、如果指定从数据库获取照片的话
     *
     * @param collectVO
     * @return
     */
    @RequestMapping(value = "collectToCardImgOuter", method = RequestMethod.POST)
    @ResponseBody
    public Result collectToCardImgOuter(@RequestBody CollectVO collectVO) {
        Result result = new Result();
        result.setStateCode(200);
        // 图片文件夹路径
        String imgPath = collectVO.getImgPath();
        // 日志文件夹路径
        String logPath = collectVO.getLogPath();
        // 默认开启的线程数量
        int threadCount = 10;
        logger.info("[采集库 => 正式库] ~--------------------------SYNCHRO START!--------------------------");
        if (threadCount > 20) {
            result.setStateCode(0);
            result.setMsg("[采集库 => 正式库] 线程数据太多,不予开启,请重新输入");
            return result;
        }
        if (threadCount == 0) {
            logger.info("[采集库 => 正式库] 使用默认线程数量: 10");
        } else {
            threadCount = collectVO.getThreadCount();
        }

        // 如果图片文件夹路径为空,则通过身份证从数据库获取图片
        if (!ObjectUtils.notEmpty(imgPath)) {
            logger.info("[采集库 => 正式库] 本次同步从数据库获取人员图片");
            List<BasicPersonInfoPO>       qualifiedUserList = collectService.listQualifiedBasicPerson(collectVO);
            List<List<BasicPersonInfoPO>> basicPersonList   = ListThreadUtil.dynamicListThread(qualifiedUserList, threadCount);
            for (List<BasicPersonInfoPO> basicPersonInfoPOS : basicPersonList) {
                ThreadPoolUtil.getThreadPool().execute(new CollectToCardSynchroThreadTask(collectService, cardService, midService, basicPersonInfoPOS, null, imgPath, null, false));
            }
        } else if (null != imgPath && !("").equals(imgPath)) {
            // 否则,则文件夹获取身份证信息,进行同步
            logger.info("[采集库 => 正式库] 本次同步从数据中心处理后的文件夹获取人员图片");
            // 获取文件夹路径下的所有身份证号码
            List<String> idCardList = MyFileUtils.getAllFileNameNoSuffix(imgPath);
            // 为每个线程分配人数
            List<List<String>> idCardThreadList = ListThreadUtil.dynamicListThread(idCardList, threadCount);
            for (List<String> stringList : idCardThreadList) {
                // 交付线程执行 stringList 包含人员身份证信息
                ThreadPoolUtil.getThreadPool()
                        .execute(new CollectToCardSynchroThreadTask(collectService, cardService, midService, null, stringList, imgPath, logPath, false));
            }
        }

        return null;
    }

    @RequestMapping(value = "synchroFromExcel", method = RequestMethod.GET)
    @ResponseBody
    public Result synchroFromExcel(HttpServletRequest request) throws Exception {
        Result            result           = new Result();
        String            localImgPathRoot = "F://tempImg//";
        String            filePath         = request.getParameter("filePath");
        List<String>      idCardList       = TxtUtil.readLine(filePath, "UTF-8");
        ArrayList<String> errorList        = new ArrayList<>();
        ArrayList<String> successList      = new ArrayList<>();
        for (String idCard : idCardList) {
            StringBuilder str = new StringBuilder();
            str.append(idCard );
            boolean photoResult = true;
            // 1、判断卡管库是否存在此人
            // boolean exit = cardService.userExistInCard(idCard, null);
            // if (exit) {
            //     successList.add(idCard + "_卡管存在");
            //     continue;
            // }

            // 2、判断采集库是否存在
            BasicPersonInfoPO collectUser = collectService.getBasicInfoByIDCard(idCard, null);
            if (!ObjectUtils.notEmpty(collectUser)) {
                errorList.add(idCard + "_采集库不存在");
                continue;
            }

            // 3、判断人员基础信息 采集库存在、卡管库不存在,
            // boolean validateResult = collectService.validateBasicPersonInfo(collectUser);
            // if (validateResult) {
            //     str.append("_信息校验成功");
            // } else {
            //     photoResult = false;
            //     str.append("_" + collectUser.getDealMsg());
            // }
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
                FileUtils.writeByteArrayToFile(new File(localImgPathRoot + idCard + ".jpg"), bytes);
                str.append("_照片成功写入文件夹");
            } else {
                // 查找TSB照片
                String imgSrcPath = "E://TSBphotoPath20180723//" + idCard + ".jpg";
                if (new File(imgSrcPath).exists()) {
                    FileUtils.copyFile(new File(imgSrcPath), new File(localImgPathRoot + idCard + ".jpg"));
                    str.append("存在TSB照片并复制成功_");
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

        String successFilePath = "F://clarencezero//success.txt";
        String errorFilePath   = "F://clarencezero//error.txt";
        TxtUtil.writeTxt(new File(successFilePath), "UTF-8", successList);
        TxtUtil.writeTxt(new File(errorFilePath), "UTF-8", errorList);
        result.setStateCode(200);
        result.setData("成功人数: " + successList.size() + " 失败人数: " + errorList.size());
        return result;
    }
}
