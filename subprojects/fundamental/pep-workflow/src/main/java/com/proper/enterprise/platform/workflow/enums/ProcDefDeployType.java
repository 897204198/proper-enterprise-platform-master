package com.proper.enterprise.platform.workflow.enums;

import com.proper.enterprise.platform.core.utils.StringUtil;

/**
 * 流程定义部署类型
 * 决定了系统启动时进行流程定义初始化部署的的方式
 */
public enum ProcDefDeployType {

    /**
     * 删除之前的部署后重新部署，流程定义版本始终为 1
     */
    DROP_CREATE("drop-create"),

    /**
     * 流程部署过时覆盖部署，流程定义版本+1；流程未部署过时，进行部署，流程版本为1
     */
    OVERRIDE("override"),

    /**
     * 系统启动时若已部署过则不再进行处理；若从未部署过则进行一次部署，流程版本为1
     */
    ONCE("once"),

    /**
     * 不进行任何处理
     */
    NONE("none");

    private String type;

    ProcDefDeployType(String type) {
        this.type = type;
    }

    public static ProcDefDeployType typeOf(String type) {
        return StringUtil.isBlank(type) ? NONE : valueOf(type.toUpperCase().replace("-", "_"));
    }

}
