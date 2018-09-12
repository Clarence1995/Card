package com.tecsun.card.threadtask;

import com.tecsun.card.common.clarencezeroutils.DateUtils;
import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.txt.TxtUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 0214
 * @createTime 2018/9/10
 * @description
 */
public class ImgSortoutZipThreadTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String threadName = Thread.currentThread().getName();
    /**
     * 图片绝对路径
     */
    private List<String> imgList;

    /**
     * 图片目标路径
     */
    private String disFilePath;


    public ImgSortoutZipThreadTask(List<String> imgList, String disFilePath) {
        this.disFilePath = disFilePath;
        this.imgList = imgList;
    }

    @Override
    public void run() {
        if (!ObjectUtils.notEmpty(imgList)) {
            throw new NullPointerException("[ImgSortoutZipThreadTask] imgList 不能为空值");
        }
        if (!ObjectUtils.notEmpty(disFilePath)) {
            throw new NullPointerException("[ImgSortoutZipThreadTask] disFilePath 不能为空值");
        }
        logger.info("[{}] {} 照片规整开始处理,待处理照片数量为: {}, 目标文件夹为: {}", threadName, DateUtils.todayDatemm(), imgList.size(), disFilePath);
        for (String s : imgList) {
            // 1、复制照片到指定位置
            String idCard = s.substring(s.lastIndexOf("\\"));
            try {
                // 复制
                FileUtils.copyFile(new File(s), new File(disFilePath + idCard));

                // 压缩

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> result = new ArrayList<>(1);
        result.add("成功人数为: " + imgList.size());
        try {
            TxtUtil.writeTxt(new File(disFilePath + "num.txt"), "UTF-8", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
