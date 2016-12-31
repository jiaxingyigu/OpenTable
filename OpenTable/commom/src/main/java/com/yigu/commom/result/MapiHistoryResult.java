package com.yigu.commom.result;

import java.io.Serializable;

/**
 * Created by brain on 2016/12/27.
 */
public class MapiHistoryResult implements Serializable{

    private String id;
    private String salesno;
    private String price;
    private String created;
    private String bz;
    private String tel;
    private String name;
    private String PATH;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalesno() {
        return salesno;
    }

    public void setSalesno(String salesno) {
        this.salesno = salesno;
    }
}
