package com.proper.enterprise.platform.workflow.api;

import com.proper.enterprise.platform.workflow.enums.ParserEnum;

public class PEPTextComponent extends AbstractPEPBaseComponent {

    @Override
    public String getName() {
        if (getParserEnum() == ParserEnum.TOFLOWABLE) {
            return name + "_text";
        }
        return name;
    }

}
