package com.tecsun.card.controller.utilcontroller.thread;

import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ImgZipThread implements Runnable {

    private List<String> fileNames;
    private String targetPath;

    public ImgZipThread () {}

    public ImgZipThread (List<String> fileNames, String targetPath) {
        this.fileNames = fileNames;
        this.targetPath = targetPath;
    }

    @Override
    public void run () {
        if (ObjectUtils.notEmpty(fileNames)) {
            throw new NullPointerException("照片压缩工具类发生异常: fileNams集合为空");
        }
        if (!ObjectUtils.notEmpty(targetPath)) {
            throw new NullPointerException("压缩目标路径为空");
        }

        // 1、直接压缩
        try {
            MyFileUtils.listToZip(fileNames, new FileOutputStream(new File(targetPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
