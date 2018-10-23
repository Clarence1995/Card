import com.tecsun.card.common.clarencezeroutils.DateUtils;
import com.tecsun.card.common.clarencezeroutils.StringUtils;
import com.tecsun.card.common.clarencezeroutils.ValidateUtils;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.common.clarencezeroutils.ListThreadUtils;
import com.tecsun.card.entity.vo.UserInfoVO;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class ArrayTest {
    @Test
    public void testStringUtil() {
        System.out.println((File.separator + "\\aab\\aab").startsWith("\\"));
        System.out.println(File.separator);
    }
    @Test
    public void testListSort() {
        List<UserInfoVO> resultList = new ArrayList();
        UserInfoVO infoVO1 = new UserInfoVO();
        infoVO1.setName("A");
        infoVO1.setIdCard("511621199501157759");
        infoVO1.setTsbImgHas(true);
        infoVO1.setDatabaseImgHas(true);
        infoVO1.setUserInfoValid(true);
        infoVO1.setCollectHas(false);
        infoVO1.setCardHas(true);
        UserInfoVO infoVO2 = new UserInfoVO();
        infoVO2.setName("AB");
        infoVO2.setIdCard("511621199501157759");
        infoVO2.setTsbImgHas(true);
        infoVO2.setDatabaseImgHas(true);
        infoVO2.setUserInfoValid(true);
        infoVO2.setCollectHas(true);
        infoVO2.setCardHas(true);

        resultList.add(infoVO1);
        resultList.add(infoVO2);
        Collections.sort(resultList, new Comparator<UserInfoVO>() {
            @Override
            public int compare(UserInfoVO o1, UserInfoVO o2) {
                // 基础信息是否合格
                if (!o1.getUserInfoValid().equals(o2.getUserInfoValid())) {
                    return o1.getUserInfoValid().compareTo(o2.getUserInfoValid());
                }
                // 是否存在采集库
                if (!o1.getCollectHas().equals(o2.getCollectHas())) {
                    return o1.getCollectHas().compareTo(o1.getCollectHas());
                }
                // 是否存在TSB照片
                if (!o1.getTsbImgHas().equals(o2.getTsbImgHas())) {
                    return o1.getTsbImgHas().compareTo(o2.getTsbImgHas());
                }
                // 是否存在公安照片
                if (!o1.getDatabaseImgHas().equals(o2.getDatabaseImgHas())) {
                    return o1.getDatabaseImgHas().compareTo(o2.getDatabaseImgHas());
                }
                // 是否存在卡管卡库
                if (!o1.getCardHas().equals(o2.getCardHas())) {
                    return o1.getCardHas().compareTo(o2.getCardHas());
                }

                return o1.getName().compareTo(o2.getName());
            }
        });

        System.out.println(resultList);
    }
    @Test
    public void testDateFormat() {

        String time = DateUtils.dateFormat("20180917", "yyyy/MM/dd");
        System.out.println(time );
    }

    @Test
    public void test() {
        String time = DateUtils.newDateFormat1("2010/10/10", "yyyy/MM/dd");
        System.out.println(time);
        System.out.println(3600L * 24L * 365L * 6L * 1000L);
    }


    @Test
    public void testClassPathResource() {
        // 1、资源加载
        ClassPathResource res = new ClassPathResource("config/beans.xml");

        System.out.println(res.exists());
    }

    @Test
    public void testBeanFactory() throws IOException {
        ClassPathResource          res     = new ClassPathResource("config/spring.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader    reader  = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(res);
        System.out.println(res.getFilename());
        System.out.println(res.getFile().getAbsolutePath());
    }


    @Test
    public void testResource() {
        Resource resource = new ClassPathResource("D:\\161338pjsxx13lt3wkxtax.jpg");
        System.out.println(resource.getFilename());

    }

    @Test
    public void fileRename() {
        String s1             = "E:\\imgtest\\511621199501157759_dfafdsafsdaafdsaf.jpg";
        File   file           = new File(s1);
        String idCard         = file.getName().substring(0, 18);
        String parentFilePath = file.getParentFile() + File.separator;
        file.renameTo(new File(parentFilePath + idCard + ".jpg"));
        System.out.println("name: " + file.getName().substring(0, 18));
        System.out.println("filaPath: " + file.getParentFile());
    }

    @Test
    public void testGetNum() {
        String        result = "";
        long          userNo = 0;
        String        ran    = "";
        StringBuilder sb     = new StringBuilder(28);
//        String userNoStr = redisService.get("sisp:cardmanagement:user_serial");
        String userNoStr = "404";

        // redis中存在该值
//        if (null == userNoStr || "".equals(userNoStr)) {
//            userNo = this.getUserSeq();
//            redisService.set(Constants.USER_SERIAL, String.valueOf(userNo + 1));
//        } else {
//            redisService.set(Constants.USER_SERIAL, String.valueOf(Long.parseLong(userNoStr) + 1));
//        }
        userNo = Long.parseLong(userNoStr);
        String times = String.valueOf(System.currentTimeMillis());
        sb.append(times);
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
//            ran += random.nextInt(10);
            sb.append(random.nextInt(10));
        }
//        result = times + ran;
        // 补全卡号
        DecimalFormat df = new DecimalFormat("000000000");
        sb.append(df.format(userNo));
//        result += df.format(userNo);
        System.out.println("用户编号：" + sb.toString().length());
    }


    @Test
    public void testString() {
        String s1 = "D:\\hey\\test.txt";
        System.out.println(s1.substring(0, s1.lastIndexOf("\\")));
    }

    @Test
    public void getToday() {
        System.out.println(DateUtils.todayDate1());
    }

    @Test
    public void testTxtUtilFormat() throws IOException {
//        String srcFile = "C:\\Users\\0214\\Downloads\\20180907拉萨单位未制卡数据情况\\error.txt";
//        TxtUtil.textFormat(srcFile);
        String src = "C:_aaa_bbb_ccc.txt";
        System.out.println(StringUtils.stringFormatPath(src, null));
    }

    @Test
    public void testTextFormat() throws IOException {
        String       srcFile    = "C:\\Users\\0214\\Downloads\\20180907拉萨单位未制卡数据情况\\error.txt";
        List<String> stringList = TxtUtil.readLine(srcFile, "UTF-8");
        System.out.println(stringList.size());
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

        for (String s : stringList) {
            System.out.println(s);
        }
    }

    @Test
    public void ValidateTest() {
        BasicPersonInfo bean = new BasicPersonInfo();
        System.out.println(bean.getName());
    }

    @Test
    public void ThreadTest() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (threadGroup.getParent() != null) {
            threadGroup = threadGroup.getParent();
        }
        int totalThread = threadGroup.activeCount();
        System.out.println(totalThread);
    }


    @Test
    public void lombok2Test() {
        // User user = new User();
        // user.setPassword("hello");
        // user.setUsername("clarencezero");
        // System.out.println(user.getPassword());
        // System.out.println(user.getUsername());
    }

    @Test
    public void testValidate() {
        String  mobile = "18189913965";
        boolean result = ValidateUtils.isMobile(mobile);
        System.out.println(result);
    }

    @Test
    public void lombokTest() {
        BasicPersonInfo bean = new BasicPersonInfo();
        bean.setAac001("hello");

        System.out.println(bean.getAac001());
    }


    @Test
    public void ArrayTest() {
        List<String> testList = new ArrayList(300001);
        for (int i = 0; i < 300001; i++) {
            testList.add(i, "i" + i);
        }

        List<List<String>> result = ListThreadUtils.dynamicListThread(testList);
        int                sum    = 0;
        for (List list : result) {
            System.out.println(list.size());
            System.out.println(list.get(0));
            sum += list.size();
        }
        System.out.println("[总和]: " + sum);
        // System.out.println(testList.size());
        // double threadCount = 10.0;
        // // 如果数据在20w以内,则开10线程,如果数据在20-50w之前,则开20个线程,超出50w数据,开30个线程
        //
        // if (testList.size() >= 200000 && testList.size() < 500000) {
        //     threadCount = 20;
        // } else if (testList.size() >= 200000) {
        //     threadCount = 30;
        // }
        //
        // int dynamicArrayListSize = (int) Math.ceil(testList.size() / threadCount); // 动态数组大小
        //
        // System.out.println("线程数:" + threadCount + "动态数组大小:" + dynamicArrayListSize);
        // List<List> result = new ArrayList<>();
        // for (int i = 0 ; i < threadCount; i++) {
        //     List threadList = new ArrayList();
        //     for (int j = i * dynamicArrayListSize; j < testList.size(); j++) {
        //         if (i == 0 && j >= dynamicArrayListSize) {
        //             System.out.println("此时i 和 j的情况: " + i + "::" + j);
        //             break;
        //         }else if (i > 0){
        //             if (j >= (i + 1) * dynamicArrayListSize) {
        //                 break;
        //             }
        //         }
        //         threadList.add(testList.get(j));
        //     }
        //     result.add(threadList);
        // }
        // System.out.println(result.size());
        // for (int i = 0; i < result.size(); i++) {
        //     List list = result.get(i);
        //     if (list.size() == dynamicArrayListSize) {
        //         System.out.println(list.get(0) + "~" + list.get(dynamicArrayListSize - 1));
        //     } else {
        //         System.out.println(list.get(0));
        //     }
        // }
    }
}