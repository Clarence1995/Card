package com.tecsun.card.common.clarencezeroutils;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 0214
 * @createTime 2018/10/25
 * @description
 */
public class GuavaTest {
    @Test
    public void testNormalEqualsTime() {
        Long start = System.currentTimeMillis();
        List<String> idCard = new ArrayList<>(10000);
        for (int i = 10; i < 100000000; i++) {
            idCard.add(String.valueOf(i));
        }
        List<String> resultList = new ArrayList<>();
        for (String s : idCard) {
            if (s.substring(0,2).equals("54")) {
                resultList.add(s);
            }
        }

        Long end = System.currentTimeMillis();
        System.out.println("花费时间: " + (end - start)/1000 + "秒" + ",获取结果大小为: " + resultList.size());
    }

    @Test
    public void testFilterTime() {
        Long start = System.currentTimeMillis();
        List<String> idCard = new ArrayList<>(10000);
        for (int i = 0; i < 100000000; i++) {
            idCard.add(String.valueOf(i));
        }

        Iterable<String> result    = Iterables.filter(idCard, Predicates.containsPattern("^(54)"));
        List<String> resultList = new ArrayList<>();
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            resultList.add((String) iterator.next());
        }
        Long end = System.currentTimeMillis();
        System.out.println("花费时间: " + (end - start)/1000 + "秒" + ",获取结果大小为: " + resultList.size());
    }
    @Test
    public void testCollectFilter() {
        List<String>     names     = Lists.newArrayList("54162119950115775", "511541199501155459", "Adam", "Tom");
        // Iterable<String> result    = Iterables.filter(names, Predicates.containsPattern("^(?!54)"));
        Iterable<String> result    = Iterables.filter(names, Predicates.containsPattern("^(?!54)"));

        Iterator         iterables = result.iterator();
        while (iterables.hasNext()) {
            System.out.println(iterables.next());
        }
    }
}
