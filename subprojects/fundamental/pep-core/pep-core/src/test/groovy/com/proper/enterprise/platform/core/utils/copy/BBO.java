package com.proper.enterprise.platform.core.utils.copy;

import com.proper.enterprise.platform.core.utils.copy.api.B;
import com.proper.enterprise.platform.core.utils.JSONUtil;

public class BBO implements B {

    public String toString() {
        String pepBBOStr = "";
        pepBBOStr = JSONUtil.toJSONIgnoreException(this);
        return pepBBOStr;
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
