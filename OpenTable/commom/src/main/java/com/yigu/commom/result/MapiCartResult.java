package com.yigu.commom.result;

import java.util.List;

/**
 * Created by brain on 2016/9/8.
 */
public class MapiCartResult extends MapiBaseResult{
    private String title;
    private List<MapiItemResult> items;
    private boolean isSel;

    public MapiCartResult(List<MapiItemResult> items) {
        this.items = items;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }

    public List<MapiItemResult> getItems() {
        return items;
    }

    public void setItems(List<MapiItemResult> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
