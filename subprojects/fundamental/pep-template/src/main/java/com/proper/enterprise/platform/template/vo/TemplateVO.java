package com.proper.enterprise.platform.template.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.view.BaseView;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;

import java.util.List;

public class TemplateVO extends BaseVO {

    public TemplateVO() {

    }

    public interface Detail extends BaseView {

    }

    public interface Details extends BaseView {

    }

    /**
     * 标识
     */
    @JsonView(value = {Detail.class, Details.class})
    private String code;

    /**
     * 名称
     */
    @JsonView(value = {Detail.class, Details.class})
    private String name;

    /**
     * 目录
     */
    @JsonView(value = {Detail.class, Details.class})
    private String catalog;

    @JsonView(value = {Detail.class, Details.class})
    private String catalogName;

    /**
     * 解释
     */
    @JsonView(value = {Detail.class, Details.class})
    private String description;

    /**
     * 模板
     */
    @JsonView(value = {Detail.class})
    private TemplateDetailVO detail;

    /**
     * 模板
     */
    @JsonView(value = {Details.class})
    private List<TemplateDetailVO> details;

    /**
     * 是否多文案
     */
    private Boolean muti;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TemplateDetailVO getDetail() {
        return detail;
    }

    public void setDetail(TemplateDetailVO detail) {
        this.detail = detail;
    }

    public List<TemplateDetailVO> getDetails() {
        return details;
    }

    public void setDetails(List<TemplateDetailVO> details) {
        this.details = details;
    }

    public String toString() {
        return "id:" + id;
    }

    public Boolean getMuti() {
        return muti;
    }

    public void setMuti(Boolean muti) {
        this.muti = muti;
    }

    public String getCatalogName() {
        DataDic dataDic = DataDicUtil.get("NOTICE_CATALOG", catalog);
        if (dataDic == null) {
            return "";
        }
        return dataDic.getName();
    }

}
