package com.yigu.commom.util;

/**
 * Created by brain on 2016/7/25.
 */
public interface RequestPageCallback<T> {
    void success(Integer isNext, T success);
}
