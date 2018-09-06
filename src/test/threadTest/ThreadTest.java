package threadTest;

import com.tecsun.card.common.ThreadPoolUtil;
import com.tecsun.card.thread.ThreadPool4j;
import com.tecsun.card.thread.ThreadPool4jImpl;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

public class ThreadTest {

    @Test
    public void threadInit() throws URISyntaxException, FileNotFoundException {
        ThreadPool4j threadPool = new ThreadPool4jImpl();
        threadPool.init();
        for (int i = 0; i < 100; i++) {
            threadPool.submit(new Runnable() {
                @Override
                public void run () {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
    }
}
