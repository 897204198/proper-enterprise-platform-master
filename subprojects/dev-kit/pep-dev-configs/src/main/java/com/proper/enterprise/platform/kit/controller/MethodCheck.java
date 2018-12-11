package com.proper.enterprise.platform.kit.controller;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class MethodCheck extends AbstractCheck {

    private static final String SWAGGER_ANNOTATION = "ApiOperation";
    private int anno = Integer.valueOf(SWAGGER_ANNOTATION);


    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.METHOD_DEF, TokenTypes.ANNOTATION};
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (ast.branchContains(TokenTypes.METHOD_DEF)) {
            if (ast.branchContains(TokenTypes.ANNOTATION)) {
                if (String.valueOf(TokenTypes.ANNOTATION).contains(SWAGGER_ANNOTATION)){
                    return;
                }
            } else {
                String message = "Failed！The methods no have swagger annotation";
                log(ast.getLineNo(), message);
            }
        }
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[0];
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[0];
    }

}