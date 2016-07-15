package com.proper.enterprise.platform.auth.common

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.expression.BeanFactoryResolver
import org.springframework.expression.ExpressionParser
import org.springframework.expression.common.TemplateParserContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.web.context.WebApplicationContext

class SpelTest extends AbstractTest {

    @Autowired
    WebApplicationContext wac

    @Test
    public void getBeanInSpEL() {
        mockUser('id', 'name', 'pwdInSpEl')
        ExpressionParser parser = new SpelExpressionParser()
        StandardEvaluationContext context = new StandardEvaluationContext()
        context.setBeanResolver(new BeanFactoryResolver(wac))

        String tpl = '{usergroup: {$in: [#{@mockUserService.currentUser.password}]}}'
        def val = parser.parseExpression(tpl, new TemplateParserContext()).getValue(context)

        assert val == '{usergroup: {$in: [pwdInSpEl]}}'
    }

}
