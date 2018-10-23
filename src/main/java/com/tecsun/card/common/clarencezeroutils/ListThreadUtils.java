package com.tecsun.card.common.clarencezeroutils;


import java.util.ArrayList;
import java.util.List;

/**
 * 数组线程类
 * 这个类主要用于为多线程提供数组拆分
 * 用于动态拆分数组,可动态分配每个线程所拥有的数组大小、线程数量
 * 如果设置了数组大小,则动态线程数量设置无效
 */
public class ListThreadUtils {
    /**
     * 默认线程数量
     */
    private static final Double  DEFAULT_THREAD_COUNT    = 10.0;
    private static final Double  MORE_THREAD_COUNT       = 20.0;
    private static final Integer DEFAULT_ARRAYLIST_COUNT = 0;

    public static <T> List<List<T>> dynamicListThread(List<T> list) {
        return dynamicListThread(list, 0, 0);
    }

    public static <T> List<List<T>> dynamicListThread(List<T> list, int threadCountCustom) {
        return dynamicListThread(list, threadCountCustom, 0);
    }

    public static <T> List<List<T>> dynamicListThreadBySize(List<T> list, int dynamicArraySize) {
        return dynamicListThread(list, null, dynamicArraySize);
    }



    public static <T> List<List<T>> dynamicListThread(List<T> list, Integer threadNum, Integer dynamicArraySize) {
        List<List<T>> resultList = new ArrayList<>();
        // 默认线程数量为10
        double threadCount = 0.0;
        int    midCount    = 300000;

        int dynamicArrayListSize = DEFAULT_ARRAYLIST_COUNT;
        //
        if (null == threadNum && null == dynamicArraySize) {
            //如果再者都为null,则使用默认值,即只按线程数量来分配
            if (midCount < list.size()) {
                threadCount = MORE_THREAD_COUNT;
            }
            // 动态数组大小确定
            dynamicArrayListSize = (int) Math.ceil(list.size() / threadCount);
            threadCount = DEFAULT_THREAD_COUNT;
        }

        if (null != threadNum) {
            // 如果用户传入线程数量,则使用用户的
            threadCount = threadNum;
            dynamicArrayListSize = (int) Math.ceil(list.size() / threadCount);
        }

        if (null == threadNum && null != dynamicArraySize) {
            // 如果用户没有传入线程数量,并且传入每个线程拥有的List大小,
            threadCount = (int) Math.ceil((double)list.size() / (double)dynamicArraySize);
            dynamicArrayListSize = dynamicArraySize;
        }

        // if (threadCountCustom == 0 && 0 == dynamicArraySize) {
        //     if (200000 <= list.size() && 500000 > list.size()) {
        //         // 如果list集合长度为20w到50w之间,则线程数量为20
        //         threadCount = 20.0;
        //     } else if (500000 <= list.size()) {
        //         // 如果list集合长度为50w以上,则线程数量为30
        //         threadCount = 30;
        //     }
        //     // 动态数组大小
        //     dynamicArrayListSize = (int) Math.ceil(list.size() / threadCount);
        // } else if (0 != threadCountCustom) {
        //     threadCount = threadCountCustom;
        //     // 动态数组大小
        //     dynamicArrayListSize = (int) Math.ceil(list.size() / threadCount);
        // } else if (0 != dynamicArraySize) {
        //     dynamicArrayListSize = dynamicArraySize;
        //     threadCount = (int) Math.ceil(dynamicArrayListSize / threadCount);
        // }

        for (int i = 0; i < threadCount; i++) {
            List<T> threadList = new ArrayList<>();
            // List threadList = new ArrayList();
            for (int j = i * dynamicArrayListSize; j < list.size(); j++) {
                if (i == 0 && j >= dynamicArrayListSize) {
                    break;
                } else if (i > 0) {
                    if (j >= (i + 1) * dynamicArrayListSize) {
                        break;
                    }
                }
                threadList.add(list.get(j));
            }
            resultList.add(threadList);
        }
        return resultList;
    }
}
