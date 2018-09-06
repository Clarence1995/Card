package com.tecsun.card.common.clarencezeroutils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * @author 0214
 */
public class MyFileUtils {
    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 获取路径下的所有文件名
     * @param filePath
     * @return
     */
    public static List<String> getAllFileNameNoSuffix (String filePath) {
        List<String> result = new ArrayList<>();
        File dir = new File(filePath);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    continue;
                } else if (fileName.endsWith(".jpg")) {
                    result.add(fileName.substring(0, fileName.lastIndexOf(".")));
                }
            }
        }
        return result;
    }

    /**
     * 指定一个目录并压缩
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void dirToZip (String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            if (!sourceFile.exists()) {
                throw new FileNotFoundException(sourceFile + "文件路径不存在");
            }
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 压缩一个List集合,这个List包含文件的绝对路径
     * @param srcFiles
     * @param out
     * @throws RuntimeException
     */
    public static void listToZip (List<String> srcFiles, OutputStream out) throws RuntimeException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (String srcFile : srcFiles) {
                File file = new File(srcFile);
                if (!file.exists()) {
                    throw new FileNotFoundException(srcFile + " 文件路径不存在");
                }
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(file.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("[listToZip] 压缩出现问题", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress (File sourceFile, ZipOutputStream zos, String name,
                                  boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 非递归获取文件夹内容
     *
     * @param filePath    文件夹路径
     * @param getAbsolute 是否需要绝对路径
     * @param regular     自定义获取文件后缀名
     * @return
     * @throws IOException
     */
    public static List<String> getAllFileNameWithSuffix (String filePath, boolean getAbsolute, String regular) throws IOException {
        List<String> scanFilesName = new ArrayList<>();
        LinkedList<File> queueFiles = new LinkedList<>();
        File directory = new File(filePath);
        if (!directory.isDirectory()) {
            throw new IOException("目标文件: '" + filePath + "' 存在,但是它不是一个目录");
        } else {
            // 首先将第一层目录扫描一遍
            File[] files = directory.listFiles();
            // 遍历扫出的文件数组，如果是文件夹，将其放入到linkedList中稍后处理
            addScanFiles(getAbsolute, regular, scanFilesName, queueFiles, files);
        }

        // 如果linkedList非空遍历linkedList
        while (!queueFiles.isEmpty()) {
            //移出linkedList中的第一个
            File headDirectory = queueFiles.removeFirst();
            File[] currentFiles = headDirectory.listFiles();
            addScanFiles(getAbsolute, regular, scanFilesName, queueFiles, currentFiles);
        }
        return scanFilesName;
    }

    private static void addScanFiles (boolean getAbsolute, String regular, List<String> scanFilesName, LinkedList<File> queueFiles, File[] currentFiles) {
        for (int j = 0; j < currentFiles.length; j++) {
            if (currentFiles[j].isDirectory()) {
                //如果仍然是文件夹，将其放入linkedList中
                queueFiles.add(currentFiles[j]);
            } else {
                String fileName = currentFiles[j].getName();
                //暂时将文件名放入scanFiles中
                if (ObjectUtils.notEmpty(regular)) {
                    if (fileName.endsWith(regular)) {
                        if (getAbsolute) {
                            scanFilesName.add(currentFiles[j].getAbsolutePath());
                        } else {
                            scanFilesName.add(fileName);
                        }
                    }
                } else {
                    if (getAbsolute) {
                        scanFilesName.add(currentFiles[j].getAbsolutePath());
                    } else {
                        scanFilesName.add(fileName);
                    }
                }
            }
        }
    }
}
