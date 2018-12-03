package com.proper.enterprise.platform.feedback.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_PROBLEM_INFO")
public class ProblemEntity extends BaseEntity {

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
    @Column(columnDefinition = "INTEGER(128) default '0'")
    private int views;

    /**
     * 赞的次数
     */
    @Column(columnDefinition = "INTEGER(128) default '0'")
    private int awesome;

    /**
     * 踩的次数
     */
    @Column(columnDefinition = "INTEGER(128) default '0'")
    private int tread;

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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getAwesome() {
        return awesome;
    }

    public void setAwesome(int awesome) {
        this.awesome = awesome;
    }

    public int getTread() {
        return tread;
    }

    public void setTread(int tread) {
        this.tread = tread;
    }
}
