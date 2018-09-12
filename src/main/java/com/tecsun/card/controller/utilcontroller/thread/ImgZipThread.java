package com.tecsun.card.controller.utilcontroller.thread;

import com.tecsun.card.common.clarencezeroutils.DateUtils;
import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ImgZipThread implements Runnable {
    private static final String SEPARATOR = File.separator;
    private final String threadName = Thread.currentThread().getName();
    private List<String> fileNames;
    private String targetPath;

    public ImgZipThread () {}

    public ImgZipThread (List<String> fileNames, String targetPath) {
        this.fileNames = fileNames;
        this.targetPath = targetPath;
    }

    @Override
    public void run () {
        if (!ObjectUtils.notEmpty(fileNames)) {
            throw new NullPointerException("照片压缩工具类发生异常: fileNams集合为空");
        }
        if (!ObjectUtils.notEmpty(targetPath)) {
            throw new NullPointerException("压缩目标路径为空");
        }

        // 1、直接压缩
        try {
            // 创建文件的父目录
            File fileDir = new File(targetPath);
            if (!fileDir.exists() && !fileDir.isDirectory())  {
                fileDir.mkdirs();
            }
            StringBuilder sb = new StringBuilder(targetPath);
            sb.append(SEPARATOR);
            int length = targetPath.length();
            System.out.println(targetPath.substring(length - 1, length));
            sb.append("TSB照片处理" + targetPath.substring(length - 1, length));
//            sb.append("_");
//            sb.append(DateUtils.todayDate1());
            sb.append(".zip");
            MyFileUtils.listToZip(fileNames, new FileOutputStream(new File(sb.toString())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
