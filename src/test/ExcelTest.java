// import com.tecsun.card.common.excel.ExcelDataFormatter;
// import com.tecsun.card.common.excel.ExcelUtil;
// import org.junit.Test;
//
// import java.io.*;
// import java.math.BigDecimal;
// import java.util.*;
//
//
// public class ExcelTest {
//     /**
//      * 导入Excel并装配成Bean
//      * @throws Exception
//      */
//     @Test
//     public void test02() throws Exception {
//         ExcelDataFormatter  edf = new ExcelDataFormatter();
//         Map<String, String> map = new HashMap<String, String>();
//         map.put("真", "true");
//         map.put("假", "false");
//         edf.set("locked", map);
//
//         List<User> xx = new ExcelUtil<User>(new User()).readFromFile(edf,new File("D:\\Data\\Springboot\\Excel\\x.xlsx"),null);
//         for (User user : xx) {
//             System.out.println(user.getYy());
//         }
//     }
//
//     @Test
//     public void test01() throws Exception {
//         // ①定义list集合
//         List<User> list = new ArrayList<User>();
//         User u = new User();
//
//         // ② 添加人员信息
//         u.setAge("3");
//         u.setName("fdsafdsa");
//         u.setXx(123.23D);
//         u.setYy(new Date().toString());
//         u.setLocked(false);
//         u.setDb(new BigDecimal(123));
//         list.add(u);
//
//         u = new User();
//         u.setAge("23");
//         u.setName("fdsafdsa");
//         u.setXx(123.23D);
//         u.setYy(new Date().toString());
//         u.setLocked(true);
//         u.setDb(new BigDecimal(234));
//         list.add(u);
//
//         u = new User();
//         u.setAge("123");
//         u.setName("fdsafdsa");
//         u.setXx(123.23D);
//         u.setYy(new Date().toString());
//         u.setLocked(false);
//         u.setDb(new BigDecimal(2344));
//         list.add(u);
//
//         u = new User();
//         u.setAge("22");
//         u.setName("fdsafdsa");
//         u.setXx(123.23D);
//         u.setYy(new Date().toString());
//         u.setLocked(true);
//         u.setDb(new BigDecimal(908));
//         list.add(u);
//
//         // ③ 创建格式化导出类,可以格式化输出
//         ExcelDataFormatter edf = new ExcelDataFormatter();
//         Map<String, String> map = new HashMap<String, String>();
//         map.put("true", "真");
//         map.put("false", "假");
//         edf.set("locked", map);
//
//         // ④ 写入Excel表中
//         writeToFile(list,edf, "D:\\Data\\Springboot\\Excel\\x.xlsx");
//     }
//     @Test
//     public void test03() throws Exception {
//         File file = new File("D:\\Data\\yaan\\error\\name.txt");
//         FileInputStream fis = new FileInputStream(file);
//         // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK
//         InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
//         BufferedReader buf = new BufferedReader(isr);
//         List<YaanBean> list = new ArrayList<>();
//         String line = "";
//         while ((line = buf.readLine()) != null) {
//             YaanBean yaanBean = new YaanBean();
//             yaanBean.setIdCard(line.split("_")[0]);
//             yaanBean.setRegionCode(line.split("_")[1]);
//             yaanBean.setName(line.split("_")[2]);
//             yaanBean.setReason("用户照片不存在");
//             list.add(yaanBean);
//         }
//
//         writeToFile(list, null, "D:\\Data\\yaan\\result\\result.xlsx");
//
//     }
//
// }
