package com.tecsun.card.common.clarencezeroutils;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import java.util.*;

/**
 * @author 0214
 * @createTime 2018/9/19
 * @description
 */
public class CollectUtils {


    /**
     * 字符串正则过滤
     * @param srclist
     * @param regex
     * @return
     */
    public static List<String> strFilter(List<String> srclist, String regex) {
        if (!ObjectUtils.notEmpty(srclist)) {
            throw new NullPointerException("[0214] 字符串正则过滤。集合不能为空!");
        }
        if (ObjectUtils.isEmpty(regex)) {
            throw new NullPointerException("[0214] 正则表达式不能为空");
        }
        Iterable<String> result     = Iterables.filter(srclist, Predicates.containsPattern(regex));
        List<String>     resultList = new ArrayList<>();
        Iterator         iterator   = result.iterator();
        while (iterator.hasNext()) {
            resultList.add((String) iterator.next());
        }
        return resultList;
    }

    /**
     * 集合长度排序
     * @param list
     */
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
