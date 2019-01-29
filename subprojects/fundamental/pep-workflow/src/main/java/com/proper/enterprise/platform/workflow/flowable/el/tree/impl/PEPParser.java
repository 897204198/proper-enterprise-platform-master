package com.proper.enterprise.platform.workflow.flowable.el.tree.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.workflow.api.AbstractPEPBaseComponent;
import com.proper.enterprise.platform.workflow.enums.ComponentKeyEnum;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.enums.ParserEnum;
import com.proper.enterprise.platform.workflow.factory.PEPComponentFactory;
import com.proper.enterprise.platform.workflow.model.PEPVariablesModel;
import org.flowable.common.engine.impl.de.odysseus.el.tree.impl.Builder;
import org.flowable.common.engine.impl.de.odysseus.el.tree.impl.Parser;
import org.flowable.common.engine.impl.de.odysseus.el.tree.impl.Scanner;
import org.flowable.common.engine.impl.de.odysseus.el.tree.impl.ast.*;

import java.util.HashMap;
import java.util.Map;

import static org.flowable.common.engine.impl.de.odysseus.el.tree.impl.Scanner.Symbol.*;

public class PEPParser extends Parser {

    /**
     * 转换的变量集合
     */
    private Map<String, PEPVariablesModel> pepVariablesModelMap;

    /**
     * 字典值变量集合
     */
    private Map<String, Object> stringParams;

    /**
     * 解析类型
     */
    private ParserEnum parserEnum;

    /**
     * 是否为下拉对象
     */
    private Boolean isSelect;

    public PEPParser(Builder context, String input) {
        super(context, input);
    }

    public Map<String, PEPVariablesModel> getPepVariablesModelMap() {
        if (pepVariablesModelMap == null) {
            return new HashMap<>(0);
        }
        return pepVariablesModelMap;
    }

    public void setPepVariablesModelMap(Map<String, PEPVariablesModel> pepVariablesModelMap) {
        this.pepVariablesModelMap = pepVariablesModelMap;
    }

    public Map<String, Object> getStringParams() {
        if (stringParams == null) {
            return new HashMap<>(0);
        }
        return stringParams;
    }

    public Boolean getSelect() {
        if (isSelect == null) {
            return false;
        }
        return isSelect;
    }

    public void setParserEnum(ParserEnum parserEnum) {
        this.parserEnum = parserEnum;
    }

    /**
     * nonliteral := &lt;IDENTIFIER&gt; | function | &lt;LPAREN&gt; expr &lt;RPAREN&gt;
     * function   := (&lt;IDENTIFIER&gt; &lt;COLON&gt;)? &lt;IDENTIFIER&gt; &lt;LPAREN&gt; list? &lt;RPAREN&gt;
     */
    @Override
    protected AstNode nonliteral() throws Scanner.ScanException, ParseException {
        AstNode v = null;
        switch (getToken().getSymbol()) {
            case IDENTIFIER:
                String name = consumeToken().getImage();
                if (getToken().getSymbol() == COLON && lookahead(0).getSymbol() == IDENTIFIER && lookahead(1).getSymbol() == LPAREN) {
                    consumeToken();
                    name += ":" + getToken().getImage();
                    consumeToken();
                }
                if (getToken().getSymbol() == LPAREN) {
                    v = function(name, params());
                } else { // identifier
                    v = createIdentifier(name);
                }
                break;
            case LPAREN:
                consumeToken();
                v = expr(true);
                consumeToken(RPAREN);
                v = new AstNested(v);
                break;
            default:
                break;
        }
        return v;
    }

    /**
     * literal := &lt;TRUE&gt; | &lt;FALSE&gt; | &lt;STRING&gt; | &lt;INTEGER&gt; | &lt;FLOAT&gt; | &lt;NULL&gt;
     */
    @Override
    protected AstNode literal() throws Scanner.ScanException, ParseException {
        AstNode v = null;
        switch (getToken().getSymbol()) {
            case TRUE:
                v = new AstBoolean(true);
                consumeToken();
                break;
            case FALSE:
                v = new AstBoolean(false);
                consumeToken();
                break;
            case STRING:
                v = new AstString(getToken().getImage());
                if (getSelect()) {
                    if (!getStringParams().isEmpty() && StringUtil.isNotEmpty((String) getStringParams().get(getToken().getImage()))) {
                        v = new AstString((String) getStringParams().get(getToken().getImage()));
                    }
                    this.isSelect = false;
                }
                consumeToken();
                break;
            case INTEGER:
                v = new AstNumber(parseInteger(getToken().getImage()));
                consumeToken();
                break;
            case FLOAT:
                v = new AstNumber(parseFloat(getToken().getImage()));
                consumeToken();
                break;
            case NULL:
                v = new AstNull();
                consumeToken();
                break;
            case EXTENSION:
                if (getExtensionHandler(getToken()).getExtensionPoint() == ExtensionPoint.LITERAL) {
                    v = getExtensionHandler(consumeToken()).createAstNode();
                    break;
                }
                break;
            default:
                break;
        }
        return v;
    }

    private AstNode createIdentifier(String name) {
        AstNode v = identifier(name);
        if (!getPepVariablesModelMap().isEmpty()) {
            PEPVariablesModel pepVariablesModel = getPepVariablesModelMap().get(name);
            String textFlag = "_text";
            if (pepVariablesModel == null && this.parserEnum == ParserEnum.TONATUAL && name.endsWith(textFlag)) {
                pepVariablesModel = getPepVariablesModelMap().get(name.substring(0, name.length() - 5));
            }
            if (pepVariablesModel == null) {
                throw new ErrMsgException(name + I18NUtil.getMessage("workflow.condition.property.notFound"));
            }
            if (pepVariablesModel != null) {
                AbstractPEPBaseComponent component =
                    PEPComponentFactory.product(ComponentKeyEnum.keyOf(pepVariablesModel.getComponentKey()));
                component.setName(name);
                component.setParserEnum(parserEnum);
                component.setPepVariablesChildrenModels(pepVariablesModel.getChildren());
                this.stringParams = component.packageSelectMap();
                this.isSelect = component.getSelect();
                v = identifier(component.getName());
                String variableName = pepVariablesModel.getName();
                if (StringUtil.isNotEmpty(variableName) && this.parserEnum == ParserEnum.TONATUAL) {
                    v = identifier("${" + variableName + "}");
                }
            }
        }
        return v;
    }
}
