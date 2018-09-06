package com.tecsun.card.common;

import java.util.concurrent.*;

/**
 * ClassName: ThreadPoolUtil
 * Description:线程池用来处理业务分析子系统数据
 * Author： 张清洁
 * CreateTime： 2015年07月31日 15时:20分
 */
public class ThreadPoolUtil {

    private static ThreadPoolExecutor threadPool = null;

    private static boolean isInit = false;

    public static final void init() {
        if (!isInit) {
            initThreadPool();
            isInit = true;
        }
    }

    /**
     *  CachedThreadPool 执行线程不固定，
     *  好处：可以把新增任务全部缓存在一起，
     *  坏处：只能用在短时间完成的任务（占用时间较长的操作可以导致线程数无限增大，系统资源耗尽）
     *  int corePoolSize,
     *  int maximumPoolSize,
     *  long keepAliveTime, 60L 为60s
     *  TimeUnit unit,
     *  BlockingQueue<Runnable> workQueue)
     *  构造一个缓冲功能的线程池，
     *  配置corePoolSize=0，maximumPoolSize=Integer.MAX_VALUE，keepAliveTime=60s,以及一个无容量的阻塞队列 SynchronousQueue，
     *  因此任务提交之后，将会创建新的线程执行；线程空闲超过60s将会销毁
     */
    private static void initThreadPool() {
        // threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        //         60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
                // new SynchronousQueue<Runnable>());
        threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public static ThreadPoolExecutor getThreadPool() {
        if (threadPool == null) {
            initThreadPool();
        }
        return threadPool;
    }

}
