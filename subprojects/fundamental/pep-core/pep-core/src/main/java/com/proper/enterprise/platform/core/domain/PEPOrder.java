package com.proper.enterprise.platform.core.domain;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.springframework.data.domain.Sort;

public class PEPOrder {

    public PEPOrder() {

    }

    public PEPOrder(Sort.Direction direction, String property) {
        this.direction = direction;
        this.property = property;
    }

    private Sort.Direction direction;
    private String property;

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
