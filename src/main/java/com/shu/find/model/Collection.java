package com.shu.find.model;

public class Collection {
    private Integer id;

    private Integer userId;

    private Integer questionId;

    private Long gmtCreate;

    public Collection(Integer id, Integer userId, Integer questionId, Long gmtCreate) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.gmtCreate = gmtCreate;
    }

    public Collection() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}