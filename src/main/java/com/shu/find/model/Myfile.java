package com.shu.find.model;

public class Myfile {
    private Integer id;

    private String url;

    private String name;

    private String path;

    public Myfile(Integer id, String url, String name, String path) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.path = path;
    }

    public Myfile() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }
}