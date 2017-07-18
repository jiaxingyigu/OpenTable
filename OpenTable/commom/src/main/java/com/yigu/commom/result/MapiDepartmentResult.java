package com.yigu.commom.result;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by brain on 2017/6/9.
 */
public class MapiDepartmentResult implements IPickerViewData {

    private String id;
    private String name;

    private List<MapiDepartmentResult> department;

    public List<MapiDepartmentResult> getDepartment() {
        return department;
    }

    public void setDepartment(List<MapiDepartmentResult> department) {
        this.department = department;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return name;
    }

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
