package com.tecsun.card.common;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Fangyugui on 2017/7/6.
 */
public class ReadTxtUtil{

    public static final Logger logger = Logger.getLogger(ReadTxtUtil.class);

    public static ArrayList<String> readTxt(String filePath){
        ArrayList<String> fileNames = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), "utf-8");//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = "";
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    fileNames.add(lineTxt);
                }
                read.close();
                bufferedReader.close();
            } else {
                logger.error("读取文本错误:找不到指定的文件");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            String msg="";
            if (null != e.getCause()) {
                msg = e.getCause().getMessage();
            } else {
                msg = e.getMessage();
            }
            logger.error("读取文本异常:"+msg);
            return null;
        }
        return fileNames;
    }

}
