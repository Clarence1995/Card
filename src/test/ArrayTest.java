import com.tecsun.card.common.clarencezeroutils.ValidateUtil;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.common.clarencezeroutils.ListThreadUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArrayTest {
    @Test
    public void ValidateTest() {
        BasicPersonInfoPO bean = new BasicPersonInfoPO();
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
        User user = new User();
        user.setPassword("hello");
        user.setUsername("clarencezero");
        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
    }

    @Test
    public void testValidate() {
        String mobile = "18189913965";
        boolean result = ValidateUtil.isMobile(mobile);
        System.out.println(result);
    }
    @Test
    public void lombokTest() {
        BasicPersonInfoPO bean = new BasicPersonInfoPO();
        bean.setAac001("hello");

        System.out.println(bean.getAac001());
    }


    @Test
    public void ArrayTest() {
        List<String> testList = new ArrayList(300001);
        for (int i = 0; i < 300001; i++) {
            testList.add(i, "i" + i);
        }

        List<List<String>> result  = ListThreadUtil.dynamicListThread(testList);
        int sum = 0;
        for (List list : result) {
            System.out.println(list.size());
            System.out.println(list.get(0));
            sum+= list.size();
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