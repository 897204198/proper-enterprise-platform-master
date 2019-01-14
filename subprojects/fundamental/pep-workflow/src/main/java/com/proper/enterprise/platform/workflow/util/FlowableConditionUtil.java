package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.core.utils.TmplUtil;
import org.flowable.common.engine.impl.de.odysseus.el.ObjectValueExpression;
import org.flowable.common.engine.impl.de.odysseus.el.misc.TypeConverter;
import org.flowable.common.engine.impl.de.odysseus.el.tree.Bindings;
import org.flowable.common.engine.impl.de.odysseus.el.tree.Tree;
import org.flowable.common.engine.impl.de.odysseus.el.tree.impl.Builder;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FlowableConditionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowableConditionUtil.class);

    private static final String FLOWABLE_VARIABLE_FLAG = "<var>";

    FlowableConditionUtil() {}

    public static String toNatural(String condition, Map<String, Object> params) {
        String parseCondition = "${" + TmplUtil.resolveTmpl(condition, params) + "}";
        toFlowableCondition(parseCondition, params);
        return parseCondition;
    }

    public static String toFlowableCondition(String condition, Map<String, Object> params) {
        try {
            Builder builder = new Builder();
            Tree tree = builder.build(condition);
            SimpleContext simpleContext = new SimpleContext();
            params.forEach((k, v) -> {
                ObjectValueExpression objectValueExpression = new ObjectValueExpression(TypeConverter.DEFAULT, "${" + k + "}", String.class);
                simpleContext.setVariable(String.valueOf(v), objectValueExpression);
            });
            Bindings bindings = tree.bind(null, simpleContext.getVariableMapper());
            String conditionAfter = tree.getRoot().getStructuralId(bindings);
            int count = 0;
            while (conditionAfter.contains(FLOWABLE_VARIABLE_FLAG)) {
                int index = conditionAfter.indexOf(FLOWABLE_VARIABLE_FLAG);
                StringBuilder tempBuilder = new StringBuilder(conditionAfter);
                tempBuilder.replace(index, index + 5, (String) bindings.getVariable(count).getValue(null));
                conditionAfter = tempBuilder.toString();
                count++;
            }
            return conditionAfter.substring(2, conditionAfter.length() - 1);
        } catch (Exception e) {
            LOGGER.error("The condition parse cause an error : {}", e.getMessage());
            throw new ErrMsgException(I18NUtil.getMessage("workflow.condition.parse.error"));
        }
    }
}
