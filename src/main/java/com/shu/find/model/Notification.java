package com.shu.find.model;

public class Notification {
    private Integer id;

    private Integer notifier;

    private String notifierName;

    private Integer outerId;

    private String outerTitle;

    private Integer receiver;

    private Integer type;

    private Integer status;

    private Long gmtCreate;

    public Notification(Integer id, Integer notifier, String notifierName, Integer outerId, String outerTitle, Integer receiver, Integer type, Integer status, Long gmtCreate) {
        this.id = id;
        this.notifier = notifier;
        this.notifierName = notifierName;
        this.outerId = outerId;
        this.outerTitle = outerTitle;
        this.receiver = receiver;
        this.type = type;
        this.status = status;
        this.gmtCreate = gmtCreate;
    }

    public Notification() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNotifier() {
        return notifier;
    }

    public void setNotifier(Integer notifier) {
        this.notifier = notifier;
    }

    public String getNotifierName() {
        return notifierName;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName == null ? null : notifierName.trim();
    }

    public Integer getOuterId() {
        return outerId;
    }

    public void setOuterId(Integer outerId) {
        this.outerId = outerId;
    }

    public String getOuterTitle() {
        return outerTitle;
    }

    public void setOuterTitle(String outerTitle) {
        this.outerTitle = outerTitle == null ? null : outerTitle.trim();
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}