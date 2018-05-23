package com.proper.enterprise.platform.core.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.view.BaseView;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 数据仓库实体
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
    private long count;

    public DataTrunk() {
    }

    public DataTrunk(Collection<T> data, long count) {
        this.data = data;
        this.count = count;
    }

    public DataTrunk(Page<T> data) {
        this.data = data.getContent();
        this.count = data.getTotalElements();
    }

    @SuppressWarnings("unchecked")
    public DataTrunk(Collection<T> allData, int pageNo, int pageSize) {
        if (CollectionUtil.isEmpty(allData)) {
            this.data = Collections.EMPTY_LIST;
            this.count = 0;
        } else {
            Assert.isTrue(pageNo > 0, "Page Number SHOULD FROM 1!");
            int from = (pageNo - 1) * pageSize;
            int to = from + pageSize;
            if (to > allData.size()) {
                to = allData.size();
            }
            if (from > to) {
                this.data = Collections.EMPTY_LIST;
                this.count = allData.size();
            } else {
                this.data = Arrays.asList((T[]) Arrays.copyOfRange(allData.toArray(), from, to));
                this.count = allData.size();
            }
        }
    }

    @JsonView(value = {BaseView.class})
    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    @JsonView(value = {BaseView.class})
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
