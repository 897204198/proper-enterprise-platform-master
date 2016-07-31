package com.proper.enterprise.platform.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

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
        Object result = parser.parseExpression(spEL, parserContext).getValue(context);
        return result == null ? "" : result.toString();
    }

}
