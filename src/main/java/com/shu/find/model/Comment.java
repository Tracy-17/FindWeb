package com.shu.find.model;

public class Comment {
    private Integer id;

    private Integer parentId;

    private Integer type;

    private Integer commentator;

    private Integer commentCount;

    private Integer likeCount;

    private String content;

    private Long gmtCreate;

    private Long gmtModify;

    public Comment(Integer id, Integer parentId, Integer type, Integer commentator, Integer commentCount, Integer likeCount, String content, Long gmtCreate, Long gmtModify) {
        this.id = id;
        this.parentId = parentId;
        this.type = type;
        this.commentator = commentator;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.content = content;
        this.gmtCreate = gmtCreate;
        this.gmtModify = gmtModify;
    }

    public Comment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCommentator() {
        return commentator;
    }

    public void setCommentator(Integer commentator) {
        this.commentator = commentator;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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