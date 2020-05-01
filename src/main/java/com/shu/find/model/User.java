package com.shu.find.model;

public class User {
    private Integer id;

    private String account;

    private String password;

    private String token;

    private String name;

    private String avatar;

    private String bio;

    private Integer followCount;

    private Integer fansCount;

    private Long gmtCreate;

    private Long gmtModify;

    public User(Integer id, String account, String password, String token, String name, String avatar, String bio, Integer followCount, Integer fansCount, Long gmtCreate, Long gmtModify) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.token = token;
        this.name = name;
        this.avatar = avatar;
        this.bio = bio;
        this.followCount = followCount;
        this.fansCount = fansCount;
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio == null ? null : bio.trim();
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Long gmtModify) {
        this.gmtModify = gmtModify;
    }
}