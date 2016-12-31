package com.yigu.commom.result;

import java.io.Serializable;

/**
 * Created by brain on 2016/7/22.
 */
public class MapiResourceResult implements Serializable {
    private String NAME;
    private String ZD_ID;
    private String remark;
    private int version;
    private String url;
    private String poster;
    private String TIME;
    private String TYPE;
    private String PATH;

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    private String BZ;

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
        this.NAME = TIME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
        this.ZD_ID = TYPE;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public MapiResourceResult(){

    }
    public MapiResourceResult(int id,String NAME) {
        this.id = id;
        this.NAME = NAME;
    }

    public MapiResourceResult(String ZD_ID,String NAME) {
        this.ZD_ID = ZD_ID;
        this.TYPE = ZD_ID;
        this.NAME = NAME;
        this.TIME = NAME;
    }
    private boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
        this.TIME = NAME;
    }

    public String getZD_ID() {
        return ZD_ID;
    }

    public void setZD_ID(String ZD_ID) {
        this.ZD_ID = ZD_ID;
        this.TYPE = ZD_ID;
    }
}
