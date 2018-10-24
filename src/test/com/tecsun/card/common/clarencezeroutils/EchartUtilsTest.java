package com.tecsun.card.common.clarencezeroutils;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchartUtilsTest {

    @Test
    public void elementUITitle () {
        Map<String, String> map = new HashMap<>();
        List<String> strings = new ArrayList<>();
        strings.add("同步名_synchroName");
        strings.add("人数_synchroCount");
        List<Map<String, String>> result = EchartUtils.elementUITitle(strings);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", result);
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void testValidate() {
        String idCard = "511621199501157758";
        System.out.println(ValidateUtils.isIdCard(idCard));
    }
}