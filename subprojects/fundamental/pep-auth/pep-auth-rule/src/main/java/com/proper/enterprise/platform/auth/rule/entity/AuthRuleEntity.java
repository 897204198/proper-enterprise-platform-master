package com.proper.enterprise.platform.auth.rule.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;

import javax.persistence.*;

@Entity
@Table(name = "PEP_AUTH_RULE")
public class AuthRuleEntity extends BaseEntity {

    public AuthRuleEntity(){}

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Convert(converter = DataDicLiteConverter.class)
    @Column(nullable = false)
    private DataDicLite type;

    @Column(nullable = false, length = 10)
    private Integer sort;

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

    public DataDicLite getType() {
        return type;
    }

    public void setType(DataDicLite type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
