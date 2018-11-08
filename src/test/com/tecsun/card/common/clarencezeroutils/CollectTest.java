package com.tecsun.card.common.clarencezeroutils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 0214
 * @createTime 2018/10/26
 * @description
 */
public class CollectTest {
    @Test
    public void testCollect() {
        List<String> idCardList = new ArrayList<>(10000);
        for (int i = 0; i < 300; i++) {
            idCardList.add(i + "");
        }

        int batchCount = Integer.parseInt(PropertyUtils.get("GONG_AN_BATCH_COUNT"));
        int listSize = idCardList.size();
        for (int i = 0; i < idCardList.size(); i+=batchCount) {
            if (i + batchCount > listSize) {
                batchCount = listSize - i;
            }
            List newList = idCardList.subList(i, i + batchCount);
            System.out.println("数组大小: " + newList.size() +",数组起始位置:" + newList.get(0) + ",数组结束位置" + newList.get(newList.size()-1));
        }
    }
}
