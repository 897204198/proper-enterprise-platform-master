package com.proper.enterprise.platform.workflow.enums;

import java.beans.PropertyEditorSupport;

public class ProcDefDeployTypeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(ProcDefDeployType.typeOf(text));
    }

    @Override
    public String getAsText() {
        return ((ProcDefDeployType) getValue()).name().toLowerCase().replace("_", "-");
    }

}
