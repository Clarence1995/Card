package com.tecsun.card.controller;

import com.tecsun.card.common.ThreadPoolUtil;
import com.tecsun.card.common.clarencezeroutils.*;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.controller.utilcontroller.thread.ImgZipThread;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.DataHandleService;
import com.tecsun.card.service.MidService;
import com.tecsun.card.service.threadrunnable.DataSynchroRunnable;
import com.tecsun.card.threadtask.ImgSortoutZipThreadTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author 0214
 * @Description 采集库数据处理Controller类
 * @createTime 2018-09-17 14:07
 * @updateTime
 */
@RequestMapping("data")
@Api("[0214] 数据处理接口")
@RestController
public class DataController {
    private final Logger logger       = LoggerFactory.getLogger(getClass());
    private final String TXTSUFFIX    = ".txt";
    private final String SEPARATOR    = File.separator;
    private final int    SUCCESS_CODE = 200;
    private final int    FAIL_CODE    = 0;

    /**
     * 数据处理服务类
     */
    @Autowired
    private DataHandleService dataHandleService;
    /**
     * 卡管库处理服务器
     */
    @Autowired
    private CardService       cardService;
    @Autowired
    private CollectService    collectService;
    @Autowired
    private MidService midService;

    /**
     * @return
     * @Description 接口测试
     * @params
     * @author 0214
     * @createTime 2018/9/7
     * @updateTime
     */
    @ApiOperation("[0214] 接口测试")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test() {
        Result result = new Result();
        result.setStateCode(SUCCESS_CODE);
        result.setMsg("测试成功");
        logger.info("{}{}{}", "hello", "hello", "hello");
        return result;
    }


    //~-----------------------------文件、日志处理 Controller START 20180914-------------------------------

    /**
     * @return
     * @Description 日志格式化, 依据每行长度进行排序
     * @params filePath 日志文件路径
     * @author 0214
     * @createTime 2018/9/7
     * @updateTime
     */
    @ApiOperation("[0214] 日志格式化 依据每行长度进行排序")
    @RequestMapping(value = "/logFormat/{filePath}", method = RequestMethod.POST)
    public Result logFormat(@ApiParam(name = "filePath", value = "文件路径 格式为 D:_file_file.txt", required = true) @PathVariable("filePath") String filePath) {
        Result result = new Result();
        try {
            filePath = StringUtils.stringFormatPath(filePath, TXTSUFFIX);
            TxtUtil.textFormat(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            result.setStateCode(FAIL_CODE);
            result.setMsg("日志规整错误: " + e.getMessage());
            return result;
        }
        result.setStateCode(FAIL_CODE);
        result.setMsg("日志规整成功");
        return result;
    }

    /**
     * @return
     * @Description 日志规整 传入日志文件夹,并把日志合并到同一文件夹内"
     * @params
     * @author 0214
     * @createTime 2018/9/14
     * @updateTime
     */
    @ApiOperation("[0214] 日志规整 传入日志文件夹,并把日志合并到同一文件夹内")
    @RequestMapping(value = "/logSortOut/{filePath}", method = RequestMethod.POST)
    public Result logSortOut(
            @ApiParam(name = "filePath", value = "文件夹路径 格式为 D:_file", required = true) @PathVariable("filePath") String filePath) {
        Result result = new Result();
        if (ObjectUtils.isEmpty(filePath)) {
            result.setStateCode(FAIL_CODE);
            result.setMsg("filePath不能为空");
            logger.error("[{}] filePath不能为空", "logSortOut");
            return result;
        }
        filePath = StringUtils.stringFormatPath(filePath, null);
        if (!new File(filePath).isDirectory()) {
            result.setStateCode(FAIL_CODE);
            result.setMsg("filePath 并不是目录");
            logger.error("[{}]filePath 不是目录", "logSortOut");
            return result;
        }
        String[] srcFiles = null;
        try {
            // 获取当前文件夹内所有TXT文件的绝对路径
            List<String> listFilePath = MyFileUtils.getAllFileNameWithSuffix(filePath, true, "txt");
            logger.info("日志规整 logSortOut 获取日志文件数为: {}", listFilePath.size());
            srcFiles = new String[listFilePath.size()];
            for (int i = 0; i < listFilePath.size(); i++) {
                srcFiles[i] = listFilePath.get(i);
            }
            // 路径为 当前文件夹/2018091410/SORTLOG.txt
            String logdistFile = filePath + SEPARATOR + DateUtils.getNowYMDHM() + SEPARATOR + "SORTLOG" + TXTSUFFIX;
            TxtUtil.mergeFiles(logdistFile, srcFiles, "UTF-8");
            result.setMsg(DateUtils.getNowYMDHM() + " 日志规整完成, 日志路径为: " + logdistFile + ", 已规整文件数量: " + listFilePath.size());
            result.setStateCode(SUCCESS_CODE);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setStateCode(FAIL_CODE);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    /**
     * [0214] TSB照片分类并压缩
     *
     * @param imgPath TSB源照片路径
     * @param disPath TSB复制照片路径
     * @return
     */
    @ApiOperation("[0214] TSB照片分类并压缩")
    @RequestMapping(value = "/TSBPhotoSplit/{imgPath}/{disPath}/{packageNum}", method = RequestMethod.POST)
    public Result tsbPhotoSplit(@ApiParam(name = "imgPath", value = "TSB照片文件夹路径", required = true) @PathVariable("imgPath") String imgPath,
                                @ApiParam(name = "disPath", value = "TSB照片目标文件夹路径", required = true) @PathVariable("disPath") String disPath,
                                @ApiParam(name = "packageNum", value = "每个目标文件夹所包含的数量", required = true) @PathVariable("packageNum") int packageNum
    ) {
        Result result = new Result();
        // 1、判断imgPath不能为空
        if (ObjectUtils.isEmpty(imgPath)) {
            result.setStateCode(0);
            result.setMsg("imgPath不能为空");
            return result;
        }
        // 2、判断disPath不能为空
        if (ObjectUtils.isEmpty(disPath)) {
            result.setStateCode(0);
            result.setMsg("disPath不能为空");
            return result;
        }
        // 3、判断目标文件夹数量不能为空
        if (!ObjectUtils.notEmpty(packageNum)) {
            result.setStateCode(0);
            result.setMsg("packageNum 不能为空");
        }
        imgPath = StringUtils.stringFormatPath(imgPath, null);
        disPath = StringUtils.stringFormatPath(disPath, null);
        try {
            // 4、获取当前文件夹所有TSB照片的绝对路径
            List<String> stringList = MyFileUtils.getAllFileNameWithSuffix(imgPath, true, ".jpg");
            // 5、分配线程
            List<List<String>> resultList = ListThreadUtil.dynamicListThreadBySize(stringList, packageNum);
            // 6、创建目标文件夹
            MyFileUtils.generateFilePath(disPath);
            // 开启线程
            int i = 0;
            for (List<String> strings : resultList) {
                if (strings.size() > 0) {
                    ThreadPoolUtil.getThreadPool().execute(new ImgZipThread(strings, disPath));
                    i++;
                }
            }
            logger.info("~-----------------------[0214] TSB照片分类并压缩, 已开启线程数量: {}, 待复制照片总数量为: {}, 前{}个线程处理数量为: {}, 最后一个(即第{}个线程) 处理数量为: {}",
                    i, stringList.size(), i - 1, resultList.get(0).size(), i, resultList.get(i - 1).size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation("[0214] 文件重命名")
    @RequestMapping(value = "/fileRename/{filePath}/{position}", method = RequestMethod.POST)
    public Result fileRename(@PathVariable("filePath") String filePath, @PathVariable("position") String position) {
        int    positionLength = 2;
        String imgSuffix      = ".jpg";
        Result result         = new Result();
        if (ObjectUtils.isEmpty(filePath)) {
            result.setStateCode(0);
            result.setMsg("filePath 不能为空");
            return result;
        }

        filePath = StringUtils.stringFormatPath(filePath, null);
        // 1、遍历文件夹
        try {
            List<String> stringList = MyFileUtils.getAllFileNameWithSuffix(filePath, true, ".jpg");
            int          i          = 0;
            String[]     indexs     = position.split("_");
            if (positionLength != indexs.length) {
                result.setStateCode(0);
                result.setMsg("位置长度不符合要求,请重新输入");
                return result;
            }
            for (String s : stringList) {
                StringBuilder sb             = new StringBuilder();
                File          file           = new File(s);
                String        fileParentPath = file.getParentFile() + File.separator;
                String        idCard         = file.getName().substring(Integer.parseInt(indexs[0]), Integer.parseInt(indexs[1]));
                sb.append(fileParentPath);
                sb.append(idCard);
                sb.append(imgSuffix);
                file.renameTo(new File(sb.toString()));
                logger.info("此文件重命名成功, 路径为: " + sb.toString());
                i++;
            }
            result.setStateCode(200);
            result.setMsg("文件夹已全部完成重命名操作, 总数为: " + i);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setStateCode(0);
            result.setMsg("文件重命名异常, 原因为: " + e.getMessage());
            return result;
        }
    }


    //~--------------------------------采集数据处理-----------------------------

    /**
     * D:_test_1   E:_GAImg20180727
     * 人员数据详情并用Excel记录
     *
     * @Description 依据身份证查询人员数据详情 包括 姓名、身份证、区域、人员基础信息校验、是否存在卡管、是否存在采集库、是否有公安照片、是否有TSB照片、是否人员重复
     * @params [filePath, imgPath, copyImg]
     * @author 0214
     * @createTime 2018-09-14 12:37
     * @updateTime
     */
    @ApiOperation("[0214] 依据身份证查询人员数据详情 包括 姓名、身份证、区域、人员基础信息校验、是否存在卡管、是否存在采集库、是否有公安照片、是否有TSB照片、是否人员重复 生成Excel文件")
    @RequestMapping(value = "/infoFromtxt/{filePath}/{copyImg}/{updateDatabase}", method = RequestMethod.POST)

    public Result getInfoFromTxt(@ApiParam(name = "filePath", value = "文件路径", required = true) @PathVariable("filePath") String filePath,
                                 @ApiParam(name = "copyImg", value = "是否需要复制照片,包含TSB照片和公安数据库照片", required = true) @PathVariable("copyImg") String copyImg
    ) {
        Result result = new Result();
        if (ObjectUtils.isEmpty(filePath)) {
            result.setStateCode(FAIL_CODE);
            result.setMsg("FilePath 不能为空");
            logger.error("FilePath 不能为空");
            return result;
        }
        filePath = StringUtils.stringFormatPath(filePath, ".txt");
        boolean copy        = Boolean.parseBoolean(copyImg);
        String  logRootPath = StringUtils.getRootPath(filePath);
        logger.info("[0214 人员数据详情正] 文件路径为:{},日志文件根路径为:{} ", filePath, logRootPath);
        // 数据处理
        return dataHandleService.handleUserInfo(filePath, logRootPath, copy);
    }


    /**
     * @return com.tecsun.card.entity.Result
     * @Description 采集人员同步到卡管库
     * @param: imgFilePath 数据中心处理过的图片
     * @param: logFilePath  日志文件夹路径
     * @param: threadCount  线程数量
     * @author 0214
     * @createTime 2018-09-20 13:57
     * @updateTime
     */
    @ApiOperation("采集人员同步到卡管库,使用数据中心处理过的照片来进行同步")
    @RequestMapping(value = "/synchro/{imgFilePath}/{logFilePath}/{threadCount}/{eValidateUserInfo}", method = RequestMethod.POST)
    public Result synchro(@ApiParam(name = "imgFilePath", value = "照片文件夹路径", required = true) @PathVariable("imgFilePath") String imgFilePath,
                          @ApiParam(name = "logFilePath", value = "日志文件夹路径", required = true) @PathVariable("logFilePath") String logFilePath,
                          @ApiParam(name = "eValidateUserInfo", value = "是否开启基本信息校验", required = true) @PathVariable("eValidateUserInfo") String eValidateUserInfo,
                          @ApiParam(name = "threadCount", value = "线程数量", required = true) @PathVariable("threadCount") Integer threadCount
    ) {
        Result result = new Result();
        // 1、获取照片文件夹身份证ID,并分配线程
        if (ObjectUtils.isEmpty(imgFilePath)) {
            result.setStateCode(Constants.FAIL_RESULT_CODE);
            logger.error("[0214 采集同步] 照片文件夹不能为空");
            result.setMsg("[0214 采集同步] 照片文件夹不能为空");
            return result;
        }
        if (ObjectUtils.isEmpty(logFilePath)) {
            result.setStateCode(Constants.FAIL_RESULT_CODE);
            result.setMsg("[0214 采集同步] 日志文件夹不能为空");
            logger.error("[0214 采集同步] 日志文件夹不能为空");
            return result;
        }
        if (!ObjectUtils.notEmpty(threadCount)) {
            result.setStateCode(Constants.FAIL_RESULT_CODE);
            result.setMsg("[0214 采集同步] 线程数量不能为空");
            logger.error("[0214 采集同步] 线程数量不能为空");
            return result;
        }
        imgFilePath = StringUtils.stringFormatPath(imgFilePath, null);
        logFilePath = StringUtils.stringFormatPath(logFilePath, null);
        boolean eValidate = Boolean.parseBoolean(eValidateUserInfo);
        List<String> idCardList = MyFileUtils.getAllFileNameNoSuffix(imgFilePath);
        List<List<String>> idCardThreadList = ListThreadUtil.dynamicListThread(idCardList, threadCount);
        for (List<String> stringList : idCardThreadList) {
            ThreadPoolUtil.getThreadPool().execute(
                    new DataSynchroRunnable(collectService, null, cardService, midService,
                            stringList, imgFilePath, logFilePath, false,eValidate, true));
        }

        result.setStateCode(Constants.SUCCESS_RESULT_CODE);
        result.setMsg("[0214 采集同步] 本次同步的人员数据共有:"
                + idCardList.size() + "人,分为" + threadCount
                + "个线程,每个线程处理数量为: "
                + idCardThreadList.get(0).size() );
        return result;
    }


    @ApiOperation("[0214] 处理采集库人员数据重复 人员重复数据同步状态置为9: 表示可删除,卡管已同步置为1,未同步置为0")
    @RequestMapping(value = "/handleCollectDateRepeat/{logFilePath}/{threadCount}", method = RequestMethod.POST)
    public Result handleCollectDateRepeat(@ApiParam(name = "logFilePath", value = "日志文件夹路径") @PathVariable String logFilePath,
                                          @ApiParam(name = "threadCount", value = "线程数量") @PathVariable Integer threadCount) {
        return dataHandleService.handleCollectDateRepeat(logFilePath, threadCount);
    }


    @ApiOperation("[0214] 申领状态更新为01,可以读取TXT,并把卡置为注销,重新生成补换申领")
    @RequestMapping(value = "/updateApplyStatus/{filePath}", method = RequestMethod.GET)
    public Result updateApplyStatus(@PathVariable("filePath") String filePath) throws IOException {
        Result result = new Result();
        filePath = StringUtils.stringFormatPath(filePath, ".txt");
        List<String> stringList = TxtUtil.readLine(filePath, "UTF-8");
        logger.info("[0214] 获取人员数为{}", stringList.size());
        int total   = stringList.size();
        int success = 0;
        int fail    = 0;
        for (String s : stringList) {
            // 1、判断人员是否存在
            // boolean userexit = cardService.userExistInCard(s, null);
            // if (userexit) {
            //     // 更新申领状态
            //     Ac01DAO bean = new Ac01DAO();
            //     bean.setAAC147(s);
            //     bean.setApplyType("01");
            //     cardService.updateAC01Status(bean);
            //     success++;
            //     logger.info("[0214] 人员:{} 申领类型更新成功", s);
            // }
            logger.info("[0214] 人员不存在");
            fail++;
        }
        String resultMsg = "总人数: " + total + ", 成功人数:" + success + ",失败人数: " + fail;
        result.setData(resultMsg);
        return result;
    }


}
