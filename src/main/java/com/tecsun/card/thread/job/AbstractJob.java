package com.tecsun.card.thread.job;


import com.tecsun.card.thread.ILifeCycle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象job类。
 * 
 */
public abstract class AbstractJob implements Runnable, ILifeCycle {

    protected String lineSeparator = System.getProperty("line.separator"); 
    
    /** 运行状态：true表示正在运行；false表示已停止 */
    protected volatile AtomicBoolean run = new AtomicBoolean(true);
    
    /** 线程休眠时间（单位：秒） */
    protected int interval = 60;

    @Override
    public void init() {
        run.set(true);
    }

    @Override
    public void run() {
        while (run.get()) {
            execute();
        }
    }
    
    protected abstract void execute();
    
    /**
     * 休眠<code>interval</code>指定的时间。
     */
    protected void sleep() {
        try {
            Thread.sleep(interval * 1000);
        } catch (InterruptedException e) {
            // nothing
        }
    }
    
    /**
     * @return 返回"yyyy-MM-dd HH:mm:ss"格式的当前日期时间字符串
     */
    protected String currentTime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        return format.format(date);
    }
    
    @Override
    public void destroy() {
        run.set(false);
    }

}
