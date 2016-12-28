package com.proper.enterprise.platform.core.entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 数据仓库
 * 用来存放结果集数据及集合相关基本信息
 *
 * @param <T> 存放的数据类型
 */
public class DataTrunk<T> implements Serializable {

    /**
     * 结果集数据
     */
    private Collection<T> data;

    /**
     * 结果集数据总数
     */
    private long total;

    public DataTrunk() { }

    public DataTrunk(Collection<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
