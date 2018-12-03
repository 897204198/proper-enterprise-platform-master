package com.proper.enterprise.platform.feedback.vo;


public class ProblemVo {

    /**
     * 类别id
     */
    private String categoryId;

    /**
     * 问题名称
     */
    private String name;

    /**
     * 问题答案
     */
    private String answer;

    /**
     * 浏览次数
     */
    private String views;

    /**
     * 赞的次数
     */
    private String awesome;

    /**
     * 踩的次数
     */
    private String tread;

    /**
     *当前登录人对问题的评价
     */
    private String assess;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getAwesome() {
        return awesome;
    }

    public void setAwesome(String awesome) {
        this.awesome = awesome;
    }

    public String getTread() {
        return tread;
    }

    public void setTread(String tread) {
        this.tread = tread;
    }

    public String getAssess() {
        return assess;
    }

    public void setAssess(String assess) {
        this.assess = assess;
    }
}
