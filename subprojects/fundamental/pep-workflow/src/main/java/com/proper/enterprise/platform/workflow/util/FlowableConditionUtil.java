package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.core.utils.TmplUtil;
import com.proper.enterprise.platform.workflow.enums.ParserEnum;
import com.proper.enterprise.platform.workflow.flowable.el.tree.impl.PEPParser;
import com.proper.enterprise.platform.workflow.model.PEPVariablesModel;
import org.flowable.common.engine.impl.de.odysseus.el.tree.impl.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FlowableConditionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowableConditionUtil.class);

    FlowableConditionUtil() {}

    /**
     * 转换为Flowable可用的分支条件
     *
     * @param condition          条件
     * @param pepVariablesModels 变量集合
     * @return Flowable 可用的分支条件
     */
    public static String toFlowableCondition(String condition, List<PEPVariablesModel> pepVariablesModels) {
        checkCondition(condition);
        Map<String, Object> params = new HashMap<>(pepVariablesModels.size());
        Map<String, PEPVariablesModel> pepVariablesModelMap = new HashMap<>(pepVariablesModels.size());
        pepVariablesModels.forEach(
            pepVariablesModel -> {
                params.put(pepVariablesModel.getName(), pepVariablesModel.getId());
                pepVariablesModelMap.put(pepVariablesModel.getId(), pepVariablesModel);
            }
        );
        String parseCondition = "${" + TmplUtil.resolveTmpl(condition, params) + "}";

        try {
            PEPParser pepParser = createParser(parseCondition, pepVariablesModelMap, ParserEnum.TOFLOWABLE);
            parseCondition = pepParser.tree().getRoot().getStructuralId(null);
        } catch (Exception e) {
            LOGGER.error("The condition parse cause an error : {}", e);
            throw new ErrMsgException(I18NUtil.getMessage("workflow.condition.parse.error"));
        }
        return parseCondition;
    }

    /**
     * 转换为自然语言
     *
     * @param condition          条件
     * @param pepVariablesModels 变量集合
     * @return 自然语言
     */
    public static String toNaturalCondition(String condition, List<PEPVariablesModel> pepVariablesModels) {
        try {
            Map<String, PEPVariablesModel> pepVariablesModelMap = new HashMap<>(pepVariablesModels.size());
            pepVariablesModels.forEach(
                pepVariablesModel -> pepVariablesModelMap.put(pepVariablesModel.getId(), pepVariablesModel)
            );
            PEPParser pepParser = createParser(condition, pepVariablesModelMap, ParserEnum.TONATUAL);
            String conditionAfter = pepParser.tree().getRoot().getStructuralId(null);
            return conditionAfter.substring(2, conditionAfter.length() - 1);
        } catch (Exception e) {
            LOGGER.error("The condition parse cause an error : {}", e);
            throw new ErrMsgException(I18NUtil.getMessage("workflow.condition.parse.error"));
        }
    }

    /**
     * 创建Flowable的解析
     *
     * @param condition            条件
     * @param pepVariablesModelMap 变量集合
     * @param parserEnum           解析类型
     * @return 解析对象
     */
    private static PEPParser createParser(String condition, Map<String, PEPVariablesModel> pepVariablesModelMap, ParserEnum parserEnum) {
        Builder builder = new Builder(Builder.Feature.METHOD_INVOCATIONS);
        PEPParser pepParser = new PEPParser(builder, condition);
        pepParser.setPepVariablesModelMap(pepVariablesModelMap);
        pepParser.setParserEnum(parserEnum);
        return pepParser;
    }

    private static void checkCondition(String condition) {
        Stack<Character> sc = new Stack<>();
        char[] c = condition.toCharArray();
        // 条件是否含有 ${..}
        boolean hasEval = false;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '$') {
                boolean nextIsBrace = (i + 1) <= c.length && c[i + 1] == '{';
                if (!nextIsBrace) {
                    throw new ErrMsgException(I18NUtil.getMessage("workflow.condition.notEndWithBrace"));
                }
                hasEval = true;
            }
            if (c[i] == '{') {
                if ((i - 1) < 0 || c[i - 1] != '$') {
                    throw new ErrMsgException(I18NUtil.getMessage("workflow.condition.notStartWithDollar"));
                }
                sc.push(c[i]);
            } else if (c[i] == '}' && sc.peek() == '{') {
                sc.pop();
            }
        }
        if (!sc.empty() || !hasEval) {
            throw new ErrMsgException(I18NUtil.getMessage("workflow.condition.brace.notMatch"));
        }
    }
}
