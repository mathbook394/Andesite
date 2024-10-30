package com.java.andesite.vo;

public class TodoVO {
    private int id;
    private int num; //실제 개수 id(삭제했을 경우 대비)
    private String date;
    private String time;
    private String title;
    private String content;
    private int priority;
    private int done;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }
}
