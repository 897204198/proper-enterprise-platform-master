package com.proper.enterprise.platform.core.utils.copy;

import com.proper.enterprise.platform.core.utils.copy.api.B;
import com.proper.enterprise.platform.core.utils.JSONUtil;

public class BVO implements B {

    public String toString() {
        String pepBVOStr = "";
        pepBVOStr = JSONUtil.toJSONIgnoreException(this);
        return pepBVOStr;
    }

    private String sex;

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public void setSex(String sex) {
        this.sex = sex;
    }
}
