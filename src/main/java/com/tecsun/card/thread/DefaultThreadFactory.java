package com.tecsun.card.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultThreadFactory implements ThreadFactory {
    private AtomicLong count;
    private static final String DEFAULT_THREAD_NAME_PRIFIX = "clarencezero";
    private ThreadGroup group;
    private String threadNamePrefix;
    public DefaultThreadFactory () {
        this(DEFAULT_THREAD_NAME_PRIFIX);
    }

    public DefaultThreadFactory (String threadNamePrefix) {
        this.count = new AtomicLong(1L);
        this.threadNamePrefix = threadNamePrefix;
        ThreadGroup root = this.getRootThreadGroup();
        this.group = new ThreadGroup(root, this.threadNamePrefix);
    }

    public Thread newThread (Runnable r) {
        Thread thread = new Thread(this.group, r);
        thread.setName(this.threadNamePrefix + "-" + this.count.getAndIncrement());
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (5 != thread.getPriority()) {
            thread.setPriority(5);
        }
        return thread;
    }

    private ThreadGroup getRootThreadGroup () {
        ThreadGroup threadGroup;
        for (threadGroup = Thread.currentThread().getThreadGroup(); null != threadGroup.getParent(); threadGroup = threadGroup.getParent()) {
            ;
        }
        return threadGroup;
    }
}
