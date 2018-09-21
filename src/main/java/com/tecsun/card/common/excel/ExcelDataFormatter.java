package com.tecsun.card.common.excel;

import java.util.HashMap;
import java.util.Map;

/**
 * 格式化导入或导出数据
 * 如一个字段locked是Integer类型，0:正常，1:锁定，
 * 那么导出的时候，0和1这种数字的可读性不好，
 * 还有其他一些定义的数字化状态可读性都太差，
 * 所以这个类就是为了解决这种问题
 */
public class ExcelDataFormatter {
    private Map<String, Map<String, String>> formatter = new HashMap<>();

    public void set(String key, Map<String, String> map) {
        formatter.put(key, map);
    }

    public Map<String, String> get(String key) {
        return formatter.get(key);
    }
}
