package com.tecsun.card.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Fangyugui on 2017/7/6.
 */
public class LogToTxtUtil {

    public static void writerText(String path, String content) {

        File file = new File(path);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            //new FileWriter(path + "t.txt", true)  这里加入true 可以不覆盖原有TXT文件内容 续写
            BufferedWriter bw = new BufferedWriter(new FileWriter(path,true));
            bw.write(content);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
