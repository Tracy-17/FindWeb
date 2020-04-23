package com.shu.find.model;

public class Question {
    private Integer id;

    private String title;

    private String tag;

    private Integer creator;

    private Integer viewCount;

    private Integer commentCount;

    private Integer likeCount;

    private Integer collCount;

    private Long gmtCreate;

    private Long gmtModify;

    private String description;

    public Question(Integer id, String title, String tag, Integer creator, Integer viewCount, Integer commentCount, Integer likeCount, Integer collCount, Long gmtCreate, Long gmtModify) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.creator = creator;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.collCount = collCount;
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
    }

    public Question(Integer id, String title, String tag, Integer creator, Integer viewCount, Integer commentCount, Integer likeCount, Integer collCount, Long gmtCreate, Long gmtModify, String description) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.creator = creator;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.collCount = collCount;
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
        this.description = description;
    }

    public Question() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCollCount() {
        return collCount;
    }

    public void setCollCount(Integer collCount) {
        this.collCount = collCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}