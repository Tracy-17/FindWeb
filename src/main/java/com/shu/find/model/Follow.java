package com.shu.find.model;

public class Follow {
    private Integer id;

    private Integer userId;

    private Integer follower;

    private Long gmtCreate;

    public Follow(Integer id, Integer userId, Integer follower, Long gmtCreate) {
        this.id = id;
        this.userId = userId;
        this.follower = follower;
        this.gmtCreate = gmtCreate;
    }

    public Follow() {
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

    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}