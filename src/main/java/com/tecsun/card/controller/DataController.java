package com.tecsun.card.controller;

import com.tecsun.card.common.ThreadPoolUtil;
import com.tecsun.card.common.clarencezeroutils.ListThreadUtil;
import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.clarencezeroutils.StringUtils;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.controller.utilcontroller.thread.ImgZipThread;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.DataHandleService;
import com.tecsun.card.threadtask.ImgSortoutZipThreadTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @createTime 2018/9/6
 * @description
 */
@RequestMapping("data")
@Api("数据处理接口")
@RestController
public class DataController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DataHandleService dataHandleService;
    @Autowired
    private CardService cardService;

    private static final String SEPARATOR = File.separator;

    /**
     *@Description 接口测试
     *@params
     *@return
     *@author  0214
     *@createTime  2018/9/7
     *@updateTime
     */
    @ApiOperation("接口测试")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test() {
        Result result = new Result();
        result.setStateCode(200);
        result.setMsg("测试成功");
        return result;
    }

    /**
     *@Description 日志规整
     *@params  filePath 日志文件路径
     *@return
     *@author  0214
     *@createTime  2018/9/7
     *@updateTime
     */
    @ApiOperation("日志格式化")
    @RequestMapping(value = "/logFormat/{filePath}", method = RequestMethod.POST)
    public Result logFormat(@PathVariable("filePath") String filePath) {
        Result result = new Result();
        try {
            filePath = StringUtils.stringFormatPath(filePath, ".txt");
            TxtUtil.textFormat(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            result.setStateCode(0);
            result.setMsg("日志规整错误: " + e.getMessage());
            return result;
        }
        result.setStateCode(0);
        result.setMsg("日志规整成功");
        return result;
    }

    @ApiOperation("日志规整")
    @RequestMapping(value = "/logSortOut/{filePath}", method = RequestMethod.POST)
    public Result logSortOut(@PathVariable("filePath") String filePath) {
        Result result = new Result();
        if (ObjectUtils.isEmpty(filePath)) {
            result.setStateCode(0);
            result.setMsg("filePath不能为空");
            logger.error("[{}] filePath不能为空", "logSortOut");
            return result;
        }
        filePath = StringUtils.stringFormatPath(filePath, null);
        // 遍历文件夹,读取文件名
//        String rootFilePath = filePath.substring(0, filePath.lastIndexOf("//"));
        String[] srcFiles = null;
        try {
            List<String> listFilePath = MyFileUtils.getAllFileNameWithSuffix(filePath, true, "txt");
            srcFiles = new String[listFilePath.size()];
            for (int i = 0; i < listFilePath.size(); i++) {
                srcFiles[i] = listFilePath.get(i);
            }
            TxtUtil.mergeFiles(filePath + "logFileAll.txt", srcFiles, "UTF-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setStateCode(0);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    @ApiOperation("人员数据详情")
    @RequestMapping(value = "/infoFromtxt/{filePath}/{logPath}/{imgPath}", method = RequestMethod.POST)
    public Result getInfoFromTxt(@PathVariable("filePath") String filePath, @PathVariable("logPath") String logPath, @PathVariable("imgPath") String imgPath) {
        Result result = new Result();
        if (ObjectUtils.isEmpty(filePath)) {
            result.setStateCode(0);
            result.setMsg("FilePath不能为空");
            logger.error("FilePath不能为空");
        }
        filePath = StringUtils.stringFormatPath(filePath,".txt");
        logPath = StringUtils.stringFormatPath(logPath, null);
        imgPath = StringUtils.stringFormatPath(imgPath, null);
        if (ObjectUtils.isEmpty(logPath)) {
            // 使用默认日志文件位置
            logPath = "E://clarencezero//log//";
        }
        logger.info("[0214] 人员数据详情正在处理,文件路径为:{},日志路径为:{}, 照片路径为:{}", filePath, logPath, imgPath);
        return dataHandleService.handleUserInfo(filePath, logPath,imgPath );
    }

    @ApiOperation("申领状态更新为01")
    @RequestMapping(value = "/updateApplyStatus/{filePath}", method = RequestMethod.GET)
    public Result updateApplyStatus(@PathVariable("filePath") String filePath) throws IOException {
        Result result = new Result();
        filePath = StringUtils.stringFormatPath(filePath,".txt");
        List<String> stringList = TxtUtil.readLine(filePath, "UTF-8");
        logger.info("[0214] 获取人员数为{}", stringList.size());
        int total = stringList.size();
        int success = 0;
        int fail = 0;
        for (String s : stringList) {
            // 1、判断人员是否存在
            boolean userexit = cardService.userExistInCard(s, null);
            if (userexit) {
                // 更新申领状态
                Ac01DAO bean = new Ac01DAO();
                bean.setAAC147(s);
                bean.setApplyType("01");
                cardService.updateAC01Status(bean);
                success++;
                logger.info("[0214] 人员:{} 申领类型更新成功", s);
            }
            logger.info("[0214] 人员不存在");
            fail++;
        }
        String resultMsg = "总人数: " + total + ", 成功人数:" + success + ",失败人数: " + fail;
        result.setData(resultMsg);
        return result;
    }

    @ApiOperation("TSB照片分类并压缩")
    @RequestMapping(value = "/TSBPhotoSplit/{imgPath}/{disPath}", method = RequestMethod.POST)
    public Result tsbPhotoSplit(@PathVariable("imgPath") String imgPath, @PathVariable("disPath") String disPath) {
        Result result = new Result();
        if (ObjectUtils.isEmpty(imgPath)) {
            result.setStateCode(0);
            result.setMsg("imgPath不能为空");
            return result;
        }
        if (ObjectUtils.isEmpty(disPath)) {
            result.setStateCode(0);
            result.setMsg("disPath不能为空");
            return result;
        }
        imgPath = StringUtils.stringFormatPath(imgPath, null);
        disPath = StringUtils.stringFormatPath(disPath, null);
        try {
            // 获取照片绝对路径
            List<String> stringList = MyFileUtils.getAllFileNameWithSuffix(imgPath, true, ".jpg");
            // 分线程,每个线程
            List<List<String>> resultList = ListThreadUtil.dynamicListThread(stringList, 10);
            // 开启线程
            int i = 0;
            for (List<String> strings : resultList) {
//                ThreadPoolUtil.getThreadPool().execute(new ImgSortoutZipThreadTask(strings, disPath + i));
                ThreadPoolUtil.getThreadPool()
                        .execute(new ImgZipThread(strings, disPath + i));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @ApiOperation("文件重命名")
    @RequestMapping(value = "/fileRename/{filePath}/{position}", method = RequestMethod.POST)
    public Result fileRename(@PathVariable("filePath") String filePath, @PathVariable("position") String position) {
        int positionLength = 2;
        String imgSuffix = ".jpg";
        Result result = new Result();
        if (ObjectUtils.isEmpty(filePath)) {
            result.setStateCode(0);
            result.setMsg("filePath 不能为空");
            return  result;
        }

        filePath = StringUtils.stringFormatPath(filePath, null);
        // 1、遍历文件夹
        try {
            List<String> stringList = MyFileUtils.getAllFileNameWithSuffix(filePath, true, ".jpg");
            int i = 0;
            String[] indexs = position.split("_");
            if (positionLength != indexs.length) {
                result.setStateCode(0);
                result.setMsg("位置长度不符合要求,请重新输入");
                return result;
            }
            for (String s : stringList) {
                StringBuilder sb = new StringBuilder();
                File file = new File(s);
                String fileParentPath = file.getParentFile() + File.separator;
                String idCard = file.getName().substring(Integer.parseInt(indexs[0]), Integer.parseInt(indexs[1]));
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

}
