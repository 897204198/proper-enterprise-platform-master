package com.proper.enterprise.platform.core.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "date_time_e")
public class DateTimeEntity extends BaseEntity {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private Date date;

    public Date getDate() {
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        if (date == null) {
            this.date = null;
        } else {
            this.date = (Date) date.clone();
        }
    }
}
