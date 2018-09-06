package test;

import com.tecsun.card.common.clarencezeroutils.MyFileUtils;
import com.tecsun.card.entity.beandao.visualdata.ColumnDAO;
import com.tecsun.card.entity.beandao.visualdata.TableDAO;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ApacheCommonsTest {

    @Test
    public void jsonTest() {
        // 字段
        ColumnDAO id = new ColumnDAO();
        id.setColumnName("ID");
//        id.setComments("主键ID");
//        id.setDataType("VARCHAR2");
//        id.setDataLength("255");
//
//        ColumnDAO AAC147 = new ColumnDAO();
//        AAC147.setColumnName("AAC147");
//        AAC147.setComments("人员身份证号");
//        AAC147.setDataType("VARCHAR2");
//        AAC147.setDataType("18");
//        List<ColumnDAO> columnDAOList = new ArrayList<>();
//        columnDAOList.add(id);
//        columnDAOList.add(AAC147);
        // 字段准备完成
        // 表
        TableDAO ac01 = new TableDAO();
//        ac01.setColumnDAOList(columnDAOList);
//        ac01.setTableName("AC01");
//        List<TableDAO> tableDAOList = new ArrayList<>();
//        tableDAOList.add(ac01);
//        // 表准备完成
//
//        UserDAO userDAO = new UserDAO();
//        userDAO.setOwner("SISP_CARD");
//        userDAO.setTableDAOList(tableDAOList);
//        System.out.println(userDAO);
//
//        JSONObject jsonObject = new JSONObject();
//        // 子 表 SYS_LOG
//        List<TableDAO> sonTableDAO = userDAO.getTableDAOList();
//        for (TableDAO tableDAO : sonTableDAO) {
//            // 表中的所有列
//            List<ColumnDAO> columnDAOList1 = tableDAO.getColumnDAOList();
//        }
        // 组装字段
        List<ColumnDAO> jsonColumnList = new ArrayList<>();
        HashMap<String, Object> grandsonson = new HashMap<>();
        grandsonson.put("title", "Comment: 主键ID");
        HashMap<String, Object> grandson = new HashMap<>();
        grandson.put("title", "ID");
        grandson.put("Children", grandsonson);
        HashMap<String, Object> son = new HashMap<>();
        son.put("title", "SYS_LOG");
        son.put("children", grandson);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", "SISP_CARD");
        hashMap.put("children", son);
//        jsonObject.put("data", hashMap);
//        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void subStringTest() {
        String str = "542100D156000005E7C186659CA5D4B2";
        System.out.println(str.substring(0,24));
        System.out.println(str.substring(0,24).length());
    }

    @Test
    public void zipExceptionTest() throws FileNotFoundException {
        // String src = "aaa";
        // String dis = "E:\\tecsun\\photo\\synchro\\target.zip";
        // List<String> list = new ArrayList<>();
        // list.add(src);
        // MyFileUtils.listToZip(list, new FileOutputStream(new File(dis)));
    }

    @Test
    public void zipFileTest() throws IOException {
        String src = "E:\\tecsun\\photo\\synchro";
        String dis = "E:\\tecsun\\photo\\synchro\\target.zip";
        MyFileUtils.dirToZip(src, new FileOutputStream(new File(dis)),false);
        List<String> result = MyFileUtils.getAllFileNameWithSuffix(src, true, ".jpg");
        System.out.println("文件夹大小:" + result.size());
        MyFileUtils.listToZip(result, new FileOutputStream(new File(dis)));
    }


    @Test
    public void myFileUtilTest () throws IOException {
        String filePath = "E:\\tecsun\\photo\\synchro";
        List<String> list = MyFileUtils.getAllFileNameWithSuffix(filePath,true,".png");
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void copyDirectoryTest () {
        /**linkedList实现**/
        ArrayList<Object> scanFiles = new ArrayList<Object>();
        LinkedList<File> queueFiles = new LinkedList<>();
        String folderPath = "E:\\tecsun\\photo";
        File directory = new File(folderPath);
        if (!directory.isDirectory()) {
            // throw new ScanFilesException('"' + folderPath + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");

        } else {
            // 首先将第一层目录扫描一遍
            File[] files = directory.listFiles();
            // 遍历扫出的文件数组，如果是文件夹，将其放入到linkedList中稍后处理
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    queueFiles.add(files[i]);
                } else {
                    //暂时将文件名放入scanFiles中
                    scanFiles.add(files[i].getAbsolutePath());
                }
            }

            // 如果linkedList非空遍历linkedList
            while (!queueFiles.isEmpty()) {
                //移出linkedList中的第一个
                File headDirectory = queueFiles.removeFirst();
                File[] currentFiles = headDirectory.listFiles();
                for (int j = 0; j < currentFiles.length; j++) {
                    if (currentFiles[j].isDirectory()) {
                        //如果仍然是文件夹，将其放入linkedList中
                        queueFiles.add(currentFiles[j]);
                    } else {
                        scanFiles.add(currentFiles[j].getAbsolutePath());
                    }
                }
            }

            for (Object scanFile : scanFiles) {
                System.out.println(scanFile);
            }
        }

    }

}
