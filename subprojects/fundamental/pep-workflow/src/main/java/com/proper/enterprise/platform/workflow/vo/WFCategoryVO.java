package com.proper.enterprise.platform.workflow.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

public class WFCategoryVO extends BaseVO {

    /**
     * 流程类别名称
     */
    @NotBlank(message = "{workflow.category.name.notBlank}")
    private String name;

    /**
     * 流程类别编码
     */
    @NotBlank(message = "{workflow.category.code.notBlank}")
    private String code;

    /**
     * 类别同级排序号
     */
    @NotNull(message = "{workflow.category.sort.notBlank}")
    private Integer sort;

    /**
     * 描述说明
     */
    private String description;

    /**
     * 父级类别
     */
    private WFCategoryVO parent;

    /**
     * 父级类别Id
     */
    private String parentId;

    /**
     * 父级类别名称
     */
    private String parentName;

    /**
     * 父级类别编码
     */
    private String parentCode;

    /**
     * 子类别
     */
    @JsonIgnore
    private Collection<WFCategoryVO> children;

    private Integer leafCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WFCategoryVO getParent() {
        return parent;
    }

    public void setParent(WFCategoryVO parent) {
        this.parent = parent;
    }

    public Collection<WFCategoryVO> getChildren() {
        return children;
    }

    public void setChildren(Collection<WFCategoryVO> children) {
        this.children = children;
    }

    public void addParent(WFCategoryVO parent) {
        if (null == parent) {
            this.parent = null;
            return;
        }
        this.parent = parent;
    }

    public void addChild(WFCategoryVO wfCatagoryEntity) {
        if (null == children) {
            children = new ArrayList<>();
        }
        children.add(wfCatagoryEntity);
    }

    public void removeChild(WFCategoryVO wfCatagoryEntity) {
        if (null == children) {
            return;
        }
        children.remove(wfCatagoryEntity);
    }

    public String getParentId() {
        if (StringUtil.isNotEmpty(this.parentId)) {
            return this.parentId;
        }
        if (null == this.getParent()) {
            return null;
        }
        return this.getParent().getId();
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        if (StringUtil.isNotEmpty(this.parentName)) {
            return this.parentName;
        }
        if (null == this.getParent()) {
            return null;
        }
        return this.getParent().getName();
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentCode() {
        if (StringUtil.isNotEmpty(this.parentCode)) {
            return this.parentCode;
        }
        if (null == this.getParent()) {
            return null;
        }
        return this.getParent().getCode();
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getLeafCount() {
        if (this.leafCount == null) {
            return -1;
        }
        return leafCount;
    }

    public void setLeafCount(Integer leafCount) {
        this.leafCount = leafCount;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
