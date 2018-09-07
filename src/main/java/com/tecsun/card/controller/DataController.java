package com.tecsun.card.controller;

import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.clarencezeroutils.StringUtils;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.DataHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("处理人员地址和联系电话")
    @RequestMapping(value = "/dealData/{filePath}/{targetFile}" ,method = RequestMethod.GET)
    public Result getInformation() {
        return null;
    }

}
