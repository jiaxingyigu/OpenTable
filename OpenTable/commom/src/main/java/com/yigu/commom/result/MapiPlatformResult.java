package com.yigu.commom.result;

/**
 * Created by brain on 2017/1/12.
 */
public class MapiPlatformResult extends MapiBaseResult{

    private String title;
    private String created;
    private String adopt;
    private String PATH;
    private String type;
    private String url;

    public String getAdopt() {
        return adopt;
    }

    public void setAdopt(String adopt) {
        this.adopt = adopt;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
