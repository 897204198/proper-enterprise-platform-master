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
    public Boolean getSelect() {
        return true;
    }
}
