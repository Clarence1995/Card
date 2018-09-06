package com.tecsun.card.thread;

import com.tecsun.card.common.clarencezeroutils.xml.DomUtil;
import com.tecsun.card.common.clarencezeroutils.xml.NodeParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从配置文件（/biz/threadpool4j.xml）读取配置信息并存储在内存中。
 */
public class ThreadPoolConfig implements ILifeCycle {

    public final static String DEFAULT_CONFIG_FILE = "threadPool.xml";
    protected String configFile =DEFAULT_CONFIG_FILE;
    
    /**
     * key为线程池名称，value为{@link ThreadPoolInfo}实例。
     */
    protected Map<String, ThreadPoolInfo> multiThreadPoolInfo = new HashMap<>();
    
    /** 线程池_状态收集开关 */
    protected boolean threadPoolStateSwitch = false;
    protected int threadPoolStateInterval = 60;   // 单位：秒
    
    /** 线程_状态收集开关 */
    protected boolean threadStateSwitch = false;
    protected int threadStateInterval = 60;   // 单位：秒
    
    /** 线程堆栈_收集开关 */
    protected boolean threadStackSwitch = false;
    protected int threadStackInterval = 60;   // 单位：秒
    
    @Override
    public void init() {
        initConfig();
    }

    /**
     * 配置初始化: 从配置文件获取各个参数
     */
    private void initConfig() {
        Document document = DomUtil.createDocument(configFile);
        Element root = document.getDocumentElement();
        NodeParser rootParser = new NodeParser(root);
        List<Node> nodeList = rootParser.getChildNodes();
        for (Node node : nodeList) {
            NodeParser nodeParser = new NodeParser(node);
            if ( "pool".equals(node.getNodeName()) ) {
                ThreadPoolInfo info = new ThreadPoolInfo();
                info.setName(nodeParser.getAttributeValue("name"));
                info.setCoreSize(Integer.parseInt(nodeParser.getChildNodeValue("corePoolSize")));
                info.setMaxSize(Integer.parseInt(nodeParser.getChildNodeValue("maxPoolSize")));
                info.setThreadKeepAliveTime(Long.parseLong(nodeParser.getChildNodeValue("keepAliveTime")));
                info.setQueueSize(Integer.parseInt(nodeParser.getChildNodeValue("workQueueSize")));
                
                multiThreadPoolInfo.put(info.getName(), info);
            } else if ( "threadpoolstate".equals(node.getNodeName()) ) {
                threadPoolStateSwitch = computeSwitchValue(nodeParser);
                threadPoolStateInterval = computeIntervalValue(nodeParser);
            } else if ( "threadstate".equals(node.getNodeName()) ) {
                threadStateSwitch = computeSwitchValue(nodeParser);
                threadStateInterval = computeIntervalValue(nodeParser);
            } else if ( "threadstack".equals(node.getNodeName()) ) {
                threadStackSwitch = computeSwitchValue(nodeParser);
                threadStackInterval = computeIntervalValue(nodeParser);
            }
        }
    }
    
    private boolean computeSwitchValue(NodeParser nodeParser) {
        return "on".equalsIgnoreCase(
                nodeParser.getAttributeValue("switch"));
    }
    
    private int computeIntervalValue(NodeParser nodeParser) {
        return Integer.parseInt(nodeParser.getAttributeValue("interval"));
    }
    
    /**
     * 指定名称的线程池的配置是否存在。
     * 
     * @return 如果指定名称的线程池的配置存在返回true，如果不存在返回false；如果传入的线程池名称为null也返回false。
     */
    public boolean containsPool(String poolName) {
        if (null == poolName || null == multiThreadPoolInfo || multiThreadPoolInfo.isEmpty()) {
            return false;
        }
        
        return multiThreadPoolInfo.containsKey(poolName);
    }
    
    /**
     * 获取指定线程池的配置信息。
     * 
     * @param threadpoolName 线程池名称
     * @return 线程池配置信息（{@link ThreadPoolInfo}）
     */
    public ThreadPoolInfo getThreadPoolConfig(String threadpoolName) {
        return multiThreadPoolInfo.get(threadpoolName);
    }
    
    /**
     * 获取所有线程池的配置信息。
     * 
     * @return 线程池配置信息（{@link ThreadPoolInfo}）集合
     */
    public Collection<ThreadPoolInfo> getThreadPoolConfig() {
        return multiThreadPoolInfo.values();
    }
    
    /**
     * @return 输出各个线程池状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadPoolStateSwitch() {
        return threadPoolStateSwitch;
    }
    
    /**
     * @return 线程池状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadPoolStateInterval() {
        return threadPoolStateInterval;
    }
    
    /**
     * @return 输出各个线程组中线程状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadStateSwitch() {
        return threadStateSwitch;
    }
    
    /**
     * @return 线程状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadStateInterval() {
        return threadStateInterval;
    }
    
    /**
     * @return 输出所有线程堆栈的开关，true表示开，false表示关
     */
    public boolean getThreadStackSwitch() {
        return threadStackSwitch;
    }
    
    /**
     * @return 线程堆栈信息输出的间隔时间（单位：秒）
     */
    public int getThreadStackInterval() {
        return threadStackInterval;
    }
    
    @Override
    public void destroy() {
        threadPoolStateSwitch = false;
        threadStateSwitch = false;
        multiThreadPoolInfo.clear();
    }

}
