package com.tecsun.card.common.txt;


import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class TxtUtil<E> {
    private static final Logger logger = LoggerFactory.getLogger(TxtUtil.class);

    public static void main(String[] args) throws IOException {
        File file = new File("E:\\MyTest\\txt\\mylove.txt");


        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("你好,张汶沣,请多指教~ "+i);
        }

        appendTxt(file, list);
    }

    private E e;
    public static final int BUFSIZE = 1024 * 8;
    public TxtUtil(E e) {
        this.e = e;
    }

    @SuppressWarnings("unchecked")
    public E get() throws InstantiationException, IllegalAccessException {
        return (E) e.getClass().newInstance();
    }

    /**
     * 按行并以一定编码读取TXT文本,
     * @param path  TXT路径
     * @param code  编码格式
     * @return
     * @throws IOException
     */
    public static List<String> readLine(String path, String code) throws IOException {
        File file = new File(path);
        List<String> readLines = Files.readLines(file,Charset.forName(code));
        return readLines;
    }

    public static  void writeTxt(File file, String code, Collection<?> list) throws IOException {
        FileUtils.writeLines(file, code, list);
    }


    public static void textFormat(String srcFile) throws IOException {
        if (!new File(srcFile).exists()) {
            throw new FileNotFoundException("[文本格式化] 源文件不存在");
        }
        List<String> stringList = TxtUtil.readLine(srcFile, "UTF-8");
        Collections.sort(stringList, new Comparator<String>() {
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
        TxtUtil.writeTxt(new File(srcFile), "UTF-8", stringList);
        logger.info("[文本格式化] 成功写入文件, 路径为: {}", srcFile );
    }

    /**
     * Txt文件合并,有一个问题就是换行符,
     * 如果文件末尾没有换行符,则会直接加到上一个文件内容后面
     *
     * @param disPath   目标路径
     * @param srcFiles  源文件源
     * @param code      文件编码
     */
    public static void mergeFiles(String disPath, String[] srcFiles, String code) {
        FileChannel outChannel = null;
        FileChannel fc = null;
        try {
            // 开启FileChannel,这个必须被打开
            outChannel = new FileOutputStream(disPath).getChannel();
            for(String f : srcFiles){
                // 设定字符集
                Charset charset = Charset.forName(code);
                CharsetDecoder chdecoder = charset.newDecoder();
                CharsetEncoder chencoder = charset.newEncoder();
                fc = new FileInputStream(f).getChannel();
                // 创建一个1024 * 8 大小的缓冲对象
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                // decode解码
                CharBuffer charBuffer = chdecoder.decode(bb);
                // encode编码
                ByteBuffer nbuBuffer = chencoder.encode(charBuffer);
                // 将文件管道流读入ByteBuffer
                while (fc.read(nbuBuffer) != -1){
                    // 从头再来
                    bb.flip();
                    // 类似flush()函数功能,将buffer里面的数据刷新进去
                    nbuBuffer.flip();
                    outChannel.write(nbuBuffer);
                    bb.clear();
                    nbuBuffer.clear();
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }


    /**
     * txt添加集合,
     *
     * @param file
     * @param list
     */
    public static void appendTxt(File file, Collection<?> list) throws FileNotFoundException, UnsupportedEncodingException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
            for (Object o : list) {
                writer.write(o.toString());
                writer.write(System.getProperty("line.separator"));
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取Txt并按照注解装配Bean,并返回List集合
     *
     * @param file     Txt文件
     * @param code     Txt文件编码格式
     * @param pattern  Txt文件拆分格式
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public List<E> readBean(File file, String code, String pattern)
            throws IOException, IllegalAccessException, InstantiationException {
        List<E> list = new ArrayList<>();
        Map<Integer,String> annotation = new HashMap<>();
        Field[] fields = e.getClass().getDeclaredFields();
        Txt txt = null;
        for (Field field : fields) {
            txt = field.getAnnotation(Txt.class);
            if (txt == null || txt.skip() == true) {
                continue;
            }
            annotation.put(txt.index(),field.getName());
        }

        E e = null;
        List<String> readLines = Files.readLines(file, Charset.forName(code));
        for (String line : readLines) {
            e = get();
            String[] strings = line.split(pattern);
            for (int i = 0; i < strings.length; i++) {
                String key = annotation.get(i);
                String s = strings[i];
                readField(key, s, fields, e);
            }
            list.add(e);
        }

        return list;

    }

    /**
     * 判断是否存在key,如何存在key则插入Bean里面
     *
     * @param key     字段名
     * @param line    待插入的内容
     * @param fields  字段名
     * @param e       待插入的Bean
     */
    private void readField(String key, String line, Field[] fields, E e) {
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(key)) {
                try {
                    field.set(e, line);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
