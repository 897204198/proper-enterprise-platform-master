package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.service.impl.types.SerializableType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VariableConvert {

    /**
     * 将变量通过Id进行分组
     *
     * @param tasksVariables 任务节点变量集合
     * @return 分组后的任务节点变量集合
     */
    public static Map<String, Map<String, Object>> groupVariables(List<HistoricVariableInstance> tasksVariables,
                                                                  Function<HistoricVariableInstance, String> function) {
        Map<String, Map<String, Object>> formDataByTaskId  = new HashMap<>(tasksVariables.size());
        if (CollectionUtil.isEmpty(tasksVariables)) {
            return formDataByTaskId;
        }
        formDataByTaskId =
            tasksVariables.stream().collect(
                Collectors.groupingBy(
                    function,
                    Collectors.toMap(
                        HistoricVariableInstance::getVariableName,
                        historicVariableInstance -> convertVariableValue(historicVariableInstance.getValue()),
                        (oldKey, newKey) -> newKey
                    )
                )
            );
        return formDataByTaskId;
    }

    /**
     * 处理变量值(byte[]变量需要反序列化)
     *
     * @param value 变量值
     * @return 处理后变量值
     */
    private static Object convertVariableValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof byte[]) {
            SerializableType serializableType = new SerializableType();
            return serializableType.deserialize((byte[]) value, null);
        }
        return value;
    }

}
