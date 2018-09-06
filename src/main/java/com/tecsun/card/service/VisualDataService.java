package com.tecsun.card.service;

import com.tecsun.card.entity.Result;

public interface VisualDataService {
    Result getVisualDataFromCollectAndCard();

    Result getTableCollectBasic (String flush, String redisField);

    Result getTableMidUserInfo (String flush, String redisField);

    Result getTableCardUserInfo (String flush, String redisField);
}
