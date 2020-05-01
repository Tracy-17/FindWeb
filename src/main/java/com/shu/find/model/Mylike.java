package com.shu.find.model;

public class Mylike {
    private Integer id;

    private Integer userId;

    private Integer type;

    private Integer contentId;

    private Long gmtCreate;

    public Mylike(Integer id, Integer userId, Integer type, Integer contentId, Long gmtCreate) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.contentId = contentId;
        this.gmtCreate = gmtCreate;
    }

    public Mylike() {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}