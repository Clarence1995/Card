package com.tecsun.card.common.clarencezeroutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchartUtils {
    public static List<Map<String, String>> elementUITitle(List<String> listTitle) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (String s : listTitle) {
            Map<String, String> resultMap = new HashMap<>();
            String[] strings = s.split("_");
            if (strings.length != 2) {
                throw new NullPointerException("[elementUITitle]数组长度不对");
            }
            resultMap.put("name", strings[0]);
            resultMap.put("value", strings[1]);
            resultList.add(resultMap);
        }
        return resultList;
    }
}
