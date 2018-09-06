package threadTest;

import com.tecsun.card.thread.ThreadStateInfo;
import com.tecsun.card.thread.Thread4jUtil;



public class For10Task implements Runnable{

    @Override
    public void run () {
        String name = Thread.currentThread().getName();
        ThreadGroup threadGroup = Thread4jUtil.getRootThreadGroup();
        ThreadStateInfo info = Thread4jUtil.statSingleGroupThreadState(threadGroup);
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程名: name   " + info);
        }
    }
}
