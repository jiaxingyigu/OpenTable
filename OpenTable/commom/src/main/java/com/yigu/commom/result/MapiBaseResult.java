package com.yigu.commom.result;

import java.io.Serializable;

/**
 * Created by brain on 2016/8/30.
 */
public class MapiBaseResult implements Serializable{
    protected String id;
    protected String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
