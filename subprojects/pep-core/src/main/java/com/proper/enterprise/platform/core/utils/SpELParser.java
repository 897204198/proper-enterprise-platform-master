package com.proper.enterprise.platform.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class SpELParser {

    @Autowired
    WebApplicationContext wac;

    private ExpressionParser parser;
    private StandardEvaluationContext context;
    private ParserContext parserContext;

    @PostConstruct
    public void init() {
        parser = new SpelExpressionParser();
        context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(wac));
        parserContext = new TemplateParserContext();
    }

    public String parse(String spEL) {
        return parse(spEL, null, true);
    }

    public String parse(String spEL, Map<String, Object> vars, boolean isExpTpl) {
        // 不使用 Expression template 时，过滤掉表达式中的单行注释内容
        spEL = isExpTpl ? spEL : spEL.replaceAll("//.*", "");
        if (vars != null) {
            context.setVariables(vars);
        }
        Expression expression = isExpTpl ? parser.parseExpression(spEL, parserContext) : parser.parseExpression(spEL);
        Object result = expression.getValue(context);
        return result == null ? "" : result.toString();
    }

}
