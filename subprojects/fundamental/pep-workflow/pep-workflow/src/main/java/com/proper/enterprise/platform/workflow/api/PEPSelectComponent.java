package com.proper.enterprise.platform.workflow.api;

import com.proper.enterprise.platform.workflow.enums.ParserEnum;

import java.util.HashMap;
import java.util.Map;

public class PEPSelectComponent extends AbstractPEPBaseComponent {

    @Override
    public Map<String, Object> packageSelectMap() {
        Map<String, Object> stringParam = new HashMap<>(getPepVariablesChildrenModels().size());
        getPepVariablesChildrenModels().forEach(
            scope -> {
                if (getParserEnum() == ParserEnum.TONATUAL) {
                    stringParam.put(scope.getValue(), scope.getLabel());
                } else {
                    stringParam.put(scope.getLabel(), scope.getValue());
                }
            }
        );
        return stringParam;
    }

    @Override
    public String getName() {
        // 外部数据源或数据字典的下拉框/多选框/单选框键值对为空
        if (getParserEnum() == ParserEnum.TOFLOWABLE && getPepVariablesChildrenModels().size() == 0) {
            return name + "_text";
        }
        return name;
    }

    @Override
    public Boolean getSelect() {
        return true;
    }
}
