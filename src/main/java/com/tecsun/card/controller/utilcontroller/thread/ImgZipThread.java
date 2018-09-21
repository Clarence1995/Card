package com.tecsun.card.controller.utilcontroller.thread;

import com.tecsun.card.common.clarencezeroutils.DateUtils;
import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ImgZipThread implements Runnable {
    private final        Logger       logger          = LoggerFactory.getLogger(getClass());
    private static final String       SEPARATOR       = File.separator;
    private final        String       TSB_FILE_PREFIX = "TSB照片规整(需移交数据中心处理)";
    private final        String       threadName      = Thread.currentThread().getName();
    private              List<String> fileNames;
    private              String       targetPath;
    public               Object       object          = new Object();

    public ImgZipThread() {}

    /**
     * @param fileNames  文件绝对路径List集合
     * @param targetPath 目标路径文件夹
     */
    public ImgZipThread(List<String> fileNames, String targetPath) {
        this.fileNames = fileNames;
        this.targetPath = targetPath;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        if (!ObjectUtils.notEmpty(fileNames)) {
            throw new NullPointerException("照片压缩工具类发生异常: fileNams集合为空");
        }
        if (!ObjectUtils.notEmpty(targetPath)) {
            throw new NullPointerException("压缩目标路径为空");
        }
        try {
            String        random = RandomStringUtils.random(3, false, true);
            StringBuilder sb     = new StringBuilder(targetPath);
            sb.append(SEPARATOR);
            sb.append(TSB_FILE_PREFIX + DateUtils.getNowYMDHM() + random);
            sb.append("_" + fileNames.size());
            sb.append(".zip");
            System.out.println("目标路径为： " + sb.toString());
            MyFileUtils.listToZip(fileNames, new FileOutputStream(new File(sb.toString())));
            logger.info("[当前线程： {} 已完成照片压缩, 压缩数量为：{} ", threadName, fileNames.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("[当前线程： {} 未能完成照片压缩,原因为： ", threadName, e.getMessage());
        }
    }
}
