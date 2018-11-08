package com.proper.enterprise.platform.app.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;

import java.util.List;

public class AppCatalogVO extends BaseVO {

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    public AppCatalogVO() {
    }

    public interface Single extends BaseView {

    }

    /**
     * 应用类别编码
     */
    @JsonView(value = {Single.class})
    private String code;
    /**
     * 应用类别名称
     */
    @JsonView(value = {Single.class})
    private String typeName;

    /**
     * 应用顺序
     */
    @JsonView(value = {Single.class})
    private String sort;

    private List<ApplicationVO> apps;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<ApplicationVO> getApps() {
        return apps;
    }

    public void setApps(List<ApplicationVO> apps) {
        this.apps = apps;
    }
}
