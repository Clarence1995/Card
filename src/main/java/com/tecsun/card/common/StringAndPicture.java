package com.tecsun.card.common;

import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * Created by Sandwich on 2015/12/13.
 */
public class StringAndPicture {

    public final static Logger logger = Logger.getLogger(StringAndPicture.class);

    /**
     * pic to base64Str
     * @param path 读取路径
     * @return
     */
    public static String GetImageStr(String path) {// voice转化成base64字符串
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(data);// 返回Base64编码过的字节数组字符串
        } catch (IOException e) {
            logger.error("图片转换base64字符串出错"+e.getMessage());
        }
        return "";
    }
    /**
     *  base64Str to pic
     * @param imgStr base64 字符串
     * @param path 存储路径
     * @return
     */
    public static boolean GenerateImage(String imgStr,String path) { // 对字节数组字符串进行Base64解码并生成voice
        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            File file = new File(path);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            OutputStream out = new FileOutputStream(new File(path));
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
