package com.tecsun.card.common.clarencezeroutils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 0214
 * @createTime 2018/9/19
 * @description
 */
public class CollectUtils {
    public static void arrayListSortByLength(List list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() > o2.length()) {
                    return 1;
                }
                if (o1.length() == o2.length()) {
                    return 0;
                }
                return -1;
            }
        });
    }
}
