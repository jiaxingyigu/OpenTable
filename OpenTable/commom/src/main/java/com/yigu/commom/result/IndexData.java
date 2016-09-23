package com.yigu.commom.result;

/**
 *Created by brain on 2016/8/30
 */
public class IndexData implements Comparable<IndexData> {

    private Integer sort = 0;

    private String type;//数据类型

    private Object data;//数据值

    public IndexData(Integer sort, String type, Object data) {
        this.sort = sort;
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexData{");
        sb.append("type='").append(type).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return sort;
    }

    @Override
    public boolean equals(Object o) {
        IndexData indexData = (IndexData) o;
        return sort == indexData.getSort();
    }

    @Override
    public int compareTo(IndexData another) {
        return this.sort - another.sort;
    }
}
