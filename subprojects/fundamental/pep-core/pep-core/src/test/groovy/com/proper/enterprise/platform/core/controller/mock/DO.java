package com.proper.enterprise.platform.core.controller.mock;

import com.proper.enterprise.platform.core.annotation.transform.pojo.domain.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;

public class DO extends BaseEntity {

    public String toString() {
        String pepDOStr = "";
        pepDOStr = JSONUtil.toJSONIgnoreException(this);
        return pepDOStr;
    }
}
