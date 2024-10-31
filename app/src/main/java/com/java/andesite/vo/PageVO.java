package com.java.andesite.vo;

public class PageVO {
    private int nowPage;
    private int totalRecord;
    private int numPerPage;
    private int totalPage;
    private int beginPerPage;
    private int endPerPage;
    private String dateOrder;
    private int doneOrder;
    private String priorityOrder;
    public PageVO() {
        this.nowPage = 1;
        this.numPerPage = 5;
        this.dateOrder = "";
        this.doneOrder = 0;
        this.priorityOrder = "";
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public int getDoneOrder() {
        return doneOrder;
    }

    public void setDoneOrder(int doneOrder) {
        this.doneOrder = doneOrder;
    }

    public String getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(String priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public int getNowPage() {
        return nowPage;
    }



    public int getTotalRecord() {
        return totalRecord;
    }

    public int getNumPerPage() {
        return numPerPage;
    }



    public int getTotalPage() {
        return totalPage;
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


    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }


    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


    public void setBeginPerPage(int beginPerPage) {
        this.beginPerPage = beginPerPage;
    }
}
