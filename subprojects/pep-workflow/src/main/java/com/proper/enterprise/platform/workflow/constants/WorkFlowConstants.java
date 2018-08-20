package com.proper.enterprise.platform.workflow.constants;


/**
 * 流程常量
 */
public class WorkFlowConstants {

    /**
     * 流程启动者
     */
    public static final String INITIATOR = "initiator";

    /**
     * 流程启动者名称
     */
    public static final String INITIATOR_NAME = "initiatorName";

    /**
     * 经办人
     */
    public static final String ASSIGNEE = "assignee";

    /**
     * 流程标题
     */
    public static final String PROCESS_TITLE = "processTitle";


    /**
     * 流程定义名称
     */
    public static final String PROCESS_DEFINITION_NAME = "processDefinitionName";

    /**
     * 流程发起时间
     */
    public static final String PROCESS_CREATE_TIME = "processCreateTime";

    /**
     * 是否启用skip
     */
    public static final String SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";

    /**
     * 流程启动表单key
     */
    public static final String START_FORM_DATA = "start_form_data";

    /**
     * 审核结果key
     */
    public static final String APPROVE_RESULT = "passOrNot";

    /**
     * 审核通过
     */
    public static final String APPROVE_PASS = "1";
    /**
     * 相同经办人未跳过记录Key
     */
    public static final String NO_SAME_ASSIGNEE_SKIP_REMARK = "noSameAssigneeSkipSign";

}
