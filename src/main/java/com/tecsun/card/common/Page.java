package com.tecsun.card.common;

import java.util.ArrayList;
import java.util.List;

public class Page<T>{
    private int pageno; // 当前页码
    private int pagesize; // 页面大小，设置为“-1”表示不进行分页（分页无效）
    private long count;// 总记录数，设置为“-1”表示不查询总数
    //private int first;// 首页索引
   // private int last;// 尾页索引
    private List<T> data = new ArrayList<T>();
//    private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc

    public Page() {

    }

    public Page(int pageno, int pagesize) {
        this(pageno, pagesize, 0);
    }

    public Page(int pageno, int pagesize, long count) {
        this(pageno, pagesize, count, new ArrayList<T>());
    }

    public Page(int pageno, int pagesize, long count, List<T> data) {
        this.count = count;
        this.pageno = pageno;
        this.pagesize = pagesize;
        this.data = data;
    }

    public void setCount(long count) {
        this.count = count;
        if (pagesize >= count) {
            pageno = 1;
        }
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public long getCount() {
        return count;
    }

//    public int getFirst() {
//        return first;
//    }
//
//    public void setFirst(int first) {
//        this.first = first;
//    }
//
//    public int getLast() {
//        return last;
//    }
//
//    public void setLast(int last) {
//        this.last = last;
//    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

//    public String getOrderBy() {
//        return orderBy;
//    }
//
//    public void setOrderBy(String orderBy) {
//        this.orderBy = orderBy;
//    }

//    public int getFirstResult() {
//        int firstResult = (getPageNo() - 1) * getPageSize();
//        if (firstResult >= getCount()) {
//            firstResult = 0;
//        }
//        return firstResult;
//    }

//    public int getMaxResults() {
//        return getPageSize();
//    }
}
