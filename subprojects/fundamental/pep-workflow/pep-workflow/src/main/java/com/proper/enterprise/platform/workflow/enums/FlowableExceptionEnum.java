package com.proper.enterprise.platform.workflow.enums;

import com.proper.enterprise.platform.core.i18n.I18NUtil;
import org.flowable.common.engine.api.FlowableException;

public enum FlowableExceptionEnum {

    /**
     * 流程没有发现流出连线
     */
    NO_OUTGOING_SEQUENCE_FLOW("No outgoing sequence flow of the exclusive gateway ", "workflow.task.no.outgoing.sequence.flow");

    private String message;

    private String i18nCode;

    public String getMessage() {
        return message;
    }

    public String getI18nCode() {
        return i18nCode;
    }

    FlowableExceptionEnum(String message, String i18nCode) {
        this.message = message;
        this.i18nCode = i18nCode;
    }

    public static String convertFlowableException(FlowableException flowableException) {
        if (flowableException == null || flowableException.getMessage() == null) {
            return I18NUtil.getMessage("workflow.task.complete.error");
        }
        for (FlowableExceptionEnum flowableExceptionEnum : FlowableExceptionEnum.values()) {
            if (flowableException.getMessage().contains(flowableExceptionEnum.getMessage())) {
                return I18NUtil.getMessage(flowableExceptionEnum.getI18nCode());
            }
        }
        return I18NUtil.getMessage("workflow.task.complete.error");
    }
}
