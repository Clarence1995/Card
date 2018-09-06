package com.tecsun.card.controller.utilcontroller;

import com.tecsun.card.common.ThreadPoolUtil;
import com.tecsun.card.common.clarencezeroutils.ListThreadUtil;
import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.controller.utilcontroller.thread.ImgZipThread;
import com.tecsun.card.entity.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * 20180813 这是一个工具Controller类,主要负责一些图片的整理,人员数据处理等被项目经理俗称"小程序"
 */
@Controller
@RequestMapping(value = "utils")
public class UtilController {


    /**
     * 20180813 整理照片
     * 1、照片路径
     * 2、每个压缩包大小
     * 思路: 1、把文件夹里面的照片读取到一个List集合
     * 2、判断这个List就可以进行同步
     * 难点: 1、数据日志的记录问题,需要保证里面的每条记录都需要进行同步这一个动作,不然数据可能会丢失
     * 2、日志记录格式
     *
     * @return
     */
    @RequestMapping(value = "zipImg", method = RequestMethod.POST)
    @ResponseBody
    public Result zipImg (@RequestBody UtilBean utilBean) {
        Result result = new Result();
        if (!ObjectUtils.notEmpty(utilBean.getImgPath())) {
            result.fail("照片路径为空");
            return result;
        }
        if (!ObjectUtils.notEmpty(utilBean.getTargetPath())) {
            result.fail("目标路径为空");
            return result;
        }
        if (!ObjectUtils.notEmpty(utilBean.getImgCount())) {
            result.fail("单个文件夹数量没有指定");
            return result;
        }

        try {
            // 1、遍历文件夹,获取所有图片路径
            List<String> fileNames = MyFileUtils.getAllFileNameWithSuffix(utilBean.getImgPath(), true, ".jpg");
            // 2、分线程复制,一个线程数量
            List<List<String>> threadList = ListThreadUtil.dynamicListThread(fileNames, Integer.parseInt(utilBean.getImgCount()));
            // 3、遍历集合,开启线程
            for (int i = 0; i < threadList.size(); i++) {
                ThreadPoolUtil.getThreadPool()
                        .execute(new ImgZipThread(threadList.get(i),
                                utilBean.getTargetPath() + File.separator + utilBean.getZipRootName() + "_" + i + ".zip"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
