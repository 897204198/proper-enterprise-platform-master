package com.proper.enterprise.platform.core.annotation.transform.pojo.vo;

import com.proper.enterprise.platform.core.convert.handler.FromHandler;
import com.proper.enterprise.platform.core.annotation.transform.pojo.bo.ABO;

public class AVOTestInitalizerBOInitializer implements FromHandler<AVO, ABO> {
    @Override
    public void from(AVO avo, ABO abo) {
        if (null == abo.getTestInitalizerBO()) {
            return;
        }
        avo.setTestInitalizerVO(String.valueOf(abo.getTestInitalizerBO()));
    }
}
