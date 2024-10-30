package com.java.andesite.vo;

public class PageVO {
    private int nowPage;
    private int nowBlock;
    private int totalRecord;
    private int numPerPage;
    private int pagePerBlock;
    private int totalPage;
    private int totalBlock;
    private int beginPerPage;
    private int endPerPage;

    public PageVO() {
        this.nowPage = 1;
        this.numPerPage = 5;
    }

    public int getNowPage() {
        return nowPage;
    }

    public int getNowBlock() {
        return nowBlock;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public int getNumPerPage() {
        return numPerPage;
    }

    public int getPagePerBlock() {
        return pagePerBlock;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalBlock() {
        return totalBlock;
    }

    public int getBeginPerPage() {
        return beginPerPage;
    }

    public int getEndPerPage() {
        return endPerPage;
    }

    public void setEndPerPage(int endPerPage) {
        this.endPerPage = endPerPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public void setNowBlock(int nowBlock) {
        this.nowBlock = nowBlock;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public void setPagePerBlock(int pagePerBlock) {
        this.pagePerBlock = pagePerBlock;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalBlock(int totalBlock) {
        this.totalBlock = totalBlock;
    }

    public void setBeginPerPage(int beginPerPage) {
        this.beginPerPage = beginPerPage;
    }
}
