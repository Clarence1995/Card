package com.tecsun.card.common;

import java.util.ArrayList;
import java.util.List;
 
 
public class PageBean {
    private Integer totalCount;//总记录数
    private Integer pageLine;//每页查询记录数
    private Integer currentPage;//当前页码
    private Integer pageNum;//总页数
    private Integer start;//查询起始点
    private Integer end;//查询结束点
     
    private List<Object> list;//查询的结果封装集合
    PageBean(){
    }
    public PageBean(Integer totalCount,Integer pageLine,Integer currentPage){
        this.totalCount = totalCount;
        this.pageLine = ((null == pageLine) ? 15 : pageLine);//(默认15条记录)
        this.pageNum = this.countPageNum(this.getTotalCount(),this.getPageLine());
        this.currentPage = ((null == currentPage) ? 1 : ((currentPage > this.getPageNum()) ? this.getPageNum() : currentPage));//默认第1页,如果大于了最大页数，那么就展示末页
        this.start = this.countStart(this.getCurrentPage(),this.getPageLine());
        this.end = this.countEnd(this.getCurrentPage(),this.getPageLine());
    }
    /**
     * 计算分页总数
     * @param totalCount --总记录数
     * @param pageLine --每页记录数
     * @return
     */
    public Integer countPageNum(Integer totalCount,Integer pageLine){
        int pageNum;
        if(totalCount%pageLine > 0){
            pageNum = totalCount/pageLine + 1;
        }else{
            pageNum = totalCount/pageLine;
        }
        return pageNum;
    }
    /**
     * 计算查询起始点
     * @param currentPage --当前页码
     * @param pageLine --每页记录数
     * @return
     */
    public Integer countStart(Integer currentPage,Integer pageLine){
        int start = (currentPage - 1)*pageLine + 1;
        return start;
    }
    /**
     * 计算查询结束点
     * @param currentPage --当前页码
     * @param pageLine --每页记录数
     * @return
     */
    public Integer countEnd(Integer currentPage,Integer pageLine){
        //非末页时的结束查询点
        int end = currentPage * pageLine;
        if(currentPage == this.getPageNum()){
            int remainder = this.getTotalCount() % pageLine;
            if(remainder > 0){
                //最后一页剩余的记录数（包括只有1页的情况）
                end = (currentPage - 1) * pageLine + remainder;
            }
        }
        return end;
    }
    public Integer getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    public Integer getPageLine() {
        return pageLine;
    }
    public void setPageLine(Integer pageLine) {
        this.pageLine = pageLine;
    }
    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }
    public Integer getEnd() {
        return end;
    }
    public void setEnd(Integer end) {
        this.end = end;
    }
    public List<Object> getList() {
        return list;
    }
    public void setList(List<Object> list) {
        this.list = list;
    }
    @Override
    public String toString() {
        String rn = "\r\n";
        String sp = ":";
        String sp1 = " ";
        //打印结果
        String info = "记录如下"+sp+rn;
        if(null == list){
            this.setList(new ArrayList<Object>());
        }
        for(int x = 0;x<list.size();x++){
            info += "记录"+x + sp + list.get(x).toString() + rn;
        }  
        info += "总记录数："+this.getTotalCount()+ sp1;
        info += "总页数："+this.getPageNum()+ rn;
        info += "当前页："+this.getCurrentPage()+ sp1;
        info += "每页数："+this.getPageLine()+ rn;
        info += "start:"+this.getStart()+ sp1;
        info += "end:"+this.getEnd();
        return info;
    }
     
}