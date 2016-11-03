package com.yigu.commom.result;

import java.io.Serializable;

/**
 * Created by brain on 2016/7/26.
 */
public class MapiImageResult implements Serializable{
    private String ID;
    private String PATH;

    private String PICTURES_ID;



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPICTURES_ID() {
        return PICTURES_ID;
    }

    public void setPICTURES_ID(String PICTURES_ID) {
        this.PICTURES_ID = PICTURES_ID;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }
}
