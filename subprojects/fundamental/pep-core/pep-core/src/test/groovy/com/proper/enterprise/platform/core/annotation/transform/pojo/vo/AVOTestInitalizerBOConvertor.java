package com.proper.enterprise.platform.core.annotation.transform.pojo.vo;

import com.proper.enterprise.platform.core.annotation.transform.pojo.bo.ABO;
import com.proper.enterprise.platform.core.convert.handler.TargetHandler;

public class AVOTestInitalizerBOConvertor implements TargetHandler<ABO, AVO> {
    @Override
    public void target(ABO abo, AVO avo) {
        abo.setTestInitalizerBO(Integer.valueOf(avo.getTestInitalizerVO()));
    }
}
