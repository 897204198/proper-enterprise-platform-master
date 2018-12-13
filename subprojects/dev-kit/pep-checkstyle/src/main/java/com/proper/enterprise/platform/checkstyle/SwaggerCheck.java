package com.proper.enterprise.platform.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationLocationCheck;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class SwaggerCheck extends AbstractCheck {
    @Autowired
    AnnotationLocationCheck annotationLocationCheck;

    private static final String SWAGGER_ANNOTATION = "ApiOperation";
    private String anno = SWAGGER_ANNOTATION;

    public void setAnno(String anno) {
        this.anno = anno;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.METHOD_DEF};
    }

    @Override
    public void visitToken(DetailAST ast) {
        FileContents fileContents = getFileContents();
        String str = fileContents.getFileName();
        if (str.endsWith("Controller.java")) {
            if (AnnotationUtil.containsAnnotation(ast, anno)) {
                return;
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
