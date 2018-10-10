package com.tecsun.card.common.clarencezeroutils;

import com.tecsun.card.entity.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class StringUtils {
    public static void main(String[] args) {
        int a = 0;

    }
    private static final String SEPARATOR = File.separator;

    public static String getRootPath(String filePath) {
        return filePath.trim().substring(0, filePath.lastIndexOf(SEPARATOR));
    }


    private String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, 0);
    }

    public static String generateAbsoluteFileName(String filePath, String rootName, String fileSuffix) {
        StringBuilder sb = new StringBuilder(filePath);
        sb.append(Constants.SEPARATOR);
        sb.append(rootName + DateUtils.getNowYMDHM() + RandomStringUtils.random(3, false, true));
        sb.append(fileSuffix);
        return sb.toString();
    }

    /**
     *@Description 字符串格式化成Windows
     *@params  
     *@return  
     *@author  0214
     *@createTime  2018/9/7
     *@updateTime
     */
    public static String stringFormatPath(String path, String subffix) {
        String result = path;
        result = path.replace(":_", ":" + SEPARATOR);
        result = path.replace("_", SEPARATOR);
        if (null == subffix) {
            return result;
        } else {
            return result + subffix;
        }
    }

    /**
     * 截取字符串
     * @param str 待截取的字符串
     * @param start 截取起始位置 （ 1 表示第一位 -1表示倒数第1位）
     * @param end 截取结束位置 （如上start）
     * @return
     */
    public static String sub(String str,int start,int end){
        String result = "";
        try {
            if(org.apache.commons.lang.StringUtils.isEmpty(str)){
                return str;
            }
            int len=str.length();
            start = start < 0 ? len+start : start-1;
            end= end < 0 ? len+end+1 :end;
            result =str.substring(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 若str字符串已tag结束则剔除tag
     * @param str 待剔除的字符串
     * @param tag 要剔除的标签
     * @return 剔除后的字符串
     * @throws Exception
     */
    public static String trimEnd(String str,String tag) throws Exception{
        String result = str;
        if(org.apache.commons.lang.StringUtils.isEmpty(str)){
            return str;
        }
        if(tag == null || tag.equals("")){
            throw  new Exception("参数tag 不能为null或‘’ ");
        }

        int tagPosition = str.lastIndexOf(tag);
        if(tagPosition+tag.length() == str.length()){
            result = str.trim().substring(0,tagPosition);
        }
        return result;
    }

    /**
     * 将字符串str的格式转为utf-8
     * @param str
     * @return
     */
    public static String toUTF_8(String str){
        String result=null;
        try {
            if(org.apache.commons.lang.StringUtils.isEmpty(str)){
                return str;
            }
            result = new String(str.getBytes("iso-8859-1"),"utf-8");
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
