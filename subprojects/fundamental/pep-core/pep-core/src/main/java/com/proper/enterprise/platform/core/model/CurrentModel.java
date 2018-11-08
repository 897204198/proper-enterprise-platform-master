package com.proper.enterprise.platform.core.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;

public class CurrentModel {

    public CurrentModel() {
    }

    /**
     * id
     */
    @JsonView(BaseView.class)
    private String id;
    /**
     * 数据
     */
    @JsonView(BaseView.class)
    private Object data;

    public String getId() {
        return id;
    }

    public CurrentModel setId(String id) {
        this.id = id;
        return this;
    }

    public Object getData() {
        return data;
    }

    public CurrentModel setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
