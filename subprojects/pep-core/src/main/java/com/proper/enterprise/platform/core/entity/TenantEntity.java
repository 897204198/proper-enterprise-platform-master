package com.proper.enterprise.platform.core.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统租户
 * 将 PEP 视为一个云平台，平台上的所有数据都定义在租户下面，租户之间数据互不可见
 * 租户下定义各租户的特定结构和数据，如一个客户可视为一个租户，客户的权限模型、人事模型等都是租户范围内的
 * 系统初始化时，需先添加租户信息，之后指定租户管理员，由租户管理员创建租户下各类信息
 */
@Entity
@Table(name = "PEP_CORE_TENANT")
public class TenantEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
