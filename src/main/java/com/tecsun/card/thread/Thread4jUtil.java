package com.tecsun.card.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个工具类的作用是获取线程组的详情
 *
 */
public class Thread4jUtil {


    /**
     * 线程组ThreadGroup表示一组线程的集合,一旦一个线程归属到一个线程组之中后，就不能再更换其所在的线程组
     * 方便统一管理，线程组可以进行复制，快速定位到一个线程，统一进行异常设置等
     *
     * @return
     */
    public static ThreadGroup getRootThreadGroup () {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (null != threadGroup.getParent()) {
            threadGroup = threadGroup.getParent();
        }
        return threadGroup;
    }

    /**
     * 获取所有线程组的状态信息
     *
     * @return
     */
    public static Map<String, ThreadStateInfo> statAllGroupThreadState () {
        ThreadGroup root = Thread4jUtil.getRootThreadGroup();
        int groupCapacity = root.activeGroupCount() * 2;
        ThreadGroup[] groupList = new ThreadGroup[groupCapacity];
        int groupNum = root.enumerate(groupList, true);

        Map<String, ThreadStateInfo> stateInfoList = new HashMap<String, ThreadStateInfo>();
        stateInfoList.put(root.getName(), statSingleGroupThreadState(root));
        for (int i = 0; i < groupNum; i++) {
            ThreadGroup threadGroup = groupList[i];
            ThreadStateInfo stateInfo = statSingleGroupThreadState(threadGroup);
            stateInfoList.put(threadGroup.getName(), stateInfo);
        }
        return stateInfoList;
    }

    /**
     * 收集指定线程组{@link ThreadGroup}中所有线程的状态信息。
     *
     * @param threadGroup 线程组实例
     * @return {@link ThreadStateInfo}实例
     */
    public static ThreadStateInfo statSingleGroupThreadState (ThreadGroup threadGroup) {
        if (null == threadGroup) {
            throw new NullPointerException("threadGroup is null");
        }

        int threadCapacity = threadGroup.activeCount() * 2;
        Thread[] threadList = new Thread[threadCapacity];
        int threadNum = threadGroup.enumerate(threadList);

        ThreadStateInfo stateInfo = new ThreadStateInfo();
        for (int j = 0; j < threadNum; j++) {
            Thread thread = threadList[j];
            switch (thread.getState()) {
                case NEW:
                    stateInfo.newCount += 1;
                    break;
                case RUNNABLE:
                    stateInfo.runnableCount += 1;
                    break;
                case BLOCKED:
                    stateInfo.blockedCount += 1;
                    break;
                case WAITING:
                    stateInfo.waitingCount += 1;
                    break;
                case TIMED_WAITING:
                    stateInfo.timedWaitingCount += 1;
                    break;
                case TERMINATED:
                    stateInfo.terminatedCount += 1;
                    break;
                default:
                    // nothing
                    break;
            }
        }
        return stateInfo;
    }
}
