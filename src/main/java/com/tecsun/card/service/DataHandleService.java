package com.tecsun.card.service;

import com.tecsun.card.entity.Result;

/**
 * @author 0214
 * @createTime 2018/9/7
 * @description 数据处理Service
 */
public interface DataHandleService {
    Result handleUserInfo(String filePath, String logPath, String imgPath);
}
