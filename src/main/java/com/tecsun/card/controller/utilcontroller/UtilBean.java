package com.tecsun.card.controller.utilcontroller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtilBean {
    private String imgPath;      // 照片路径
    private String threadCount;  // 线程数量
    private String targetPath;   // 目标文件夹
    private String imgCount;     // 单个文件夹里面包含图片数量
    private String zipRootName;  // ZIP文件根名
}
