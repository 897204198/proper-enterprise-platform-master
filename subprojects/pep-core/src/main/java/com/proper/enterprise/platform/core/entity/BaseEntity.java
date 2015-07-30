package com.proper.enterprise.platform.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 8769611929269353212L;
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;
    
    @Column(name="create_user_id", updatable=false)
    protected String createUserId;
    
    @Column(name="create_time", updatable=false)
    protected String createTime = "";
    
    @Column(name="last_modify_user_id")
    protected String lastModifyUserId;
    
    @Column(name="last_modify_time")
    protected String lastModifyTime = "";

}
