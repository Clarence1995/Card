package com.tecsun.card.thread.job;

import com.tecsun.card.thread.ThreadStateInfo;
import com.tecsun.card.thread.Thread4jUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 收集所有线程组中所有线程的状态信息，统计并输出汇总信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadStateJob extends AbstractJob {

    private static Logger logger = LoggerFactory.getLogger(ThreadStateJob.class);
    
    public ThreadStateJob(int interval) {
        super.interval = interval;
    }

    @Override
    protected void execute() {
        Map<String, ThreadStateInfo> statMap = Thread4jUtil.statAllGroupThreadState();
        
        for (Entry<String, ThreadStateInfo> entry : statMap.entrySet()) {
            ThreadStateInfo stateInfo = entry.getValue();
            logger.info("ThreadGroup:{}, New:{},  Runnable:{}, Blocked:{}, Waiting:{}, TimedWaiting:{}, Terminated:{}", 
                    entry.getKey(), stateInfo.getNewCount(), stateInfo.getRunnableCount(), stateInfo.getBlockedCount(),
                    stateInfo.getWaitingCount(), stateInfo.getTimedWaitingCount(), stateInfo.getTerminatedCount());
        }
        
        super.sleep();
    }

}
