package com.proper.enterprise.platform.workflow.factory;

import com.proper.enterprise.platform.workflow.api.AbstractPEPBaseComponent;
import com.proper.enterprise.platform.workflow.api.PEPSelectComponent;
import com.proper.enterprise.platform.workflow.api.PEPTextComponent;
import com.proper.enterprise.platform.workflow.enums.ComponentKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PEPComponentFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PEPComponentFactory.class);


    private PEPComponentFactory() {
    }

    public static AbstractPEPBaseComponent product(ComponentKeyEnum componentKeyEnum) {
        switch (componentKeyEnum) {
            case SELECT:
            case RADIOGROUP:
            case CHECKBOXGROUP:
                return new PEPSelectComponent();
            case DATEPICKER:
            case OOPSYSTEMCURRENT:
            case OOPORGEMPPICKER:
            case OOPGROUPUSERPICKER:
                return new PEPTextComponent();
            default:
                return new AbstractPEPBaseComponent();
        }
    }
}
