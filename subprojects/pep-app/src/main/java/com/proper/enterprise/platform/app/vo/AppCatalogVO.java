package com.proper.enterprise.platform.app.vo;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;

public class AppCatalogVO extends BaseVO {

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    /**
     * 应用类别编码
     */
    private String code;
    /**
     * 应用类别名称
     */
    private String typeName;

    /**
     * 应用顺序
     */
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
