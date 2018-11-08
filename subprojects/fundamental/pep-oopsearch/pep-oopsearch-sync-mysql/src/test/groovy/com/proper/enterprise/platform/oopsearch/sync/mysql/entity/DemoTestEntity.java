package com.proper.enterprise.platform.oopsearch.sync.mysql.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@IdClass(DemoTestPK.class)
@Table(name = "DEMO_TEST")
public class DemoTestEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;

    @Id
    private String pri2;

    @Column
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPri2() {
        return pri2;
    }

    public void setPri2(String pri2) {
        this.pri2 = pri2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
