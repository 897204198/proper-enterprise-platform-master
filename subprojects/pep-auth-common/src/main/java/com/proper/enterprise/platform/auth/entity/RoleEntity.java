package com.proper.enterprise.platform.auth.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.enums.UseStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "pep_auth_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.proper.enterprise.platform.auth.entity.RoleEntity")
public class RoleEntity extends BaseEntity {

    private static final long serialVersionUID = 619340355704563195L;
    
    public RoleEntity(String name) {
        this.name = name;
    }
    
    /**
     * 名称
     */
    @Column(nullable = false, unique = true)
    private String name;
    
    /**
     * code
     */
    private String code;
    
    /**
     * 使用状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UseStatus useStatus = UseStatus.STOP;

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

    public UseStatus getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(UseStatus useStatus) {
        this.useStatus = useStatus;
    }
    
}
