package com.proper.enterprise.platform.core.jpa.handler;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.core.jpa.annotation.ConstraintViolationMessage;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConstraintViolationExceptionHandler {

    @Autowired
    private Map<String, String> entityTableMapping;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionHandler.class);

    /**
     * H2唯一索引解析索引名正则
     */
    private static final String H2_UNIQUE_INDEX_PATTERN = "(?:UK_).*?(?=_INDEX)";

    /**
     * MYSQL唯一索引解析索引名正则
     */
    private static final String MYSQL_UNIQUE_INDEX_PATTERN = "(?:UK_).*?(?=')";

    /**
     * H2外键约束解析索引名正则
     */
    private static final String H2_INTEGRITY_INDEX_PATTERN = "(?:FK_).*?(?=: )";

    /**
     * MYSQL外键约束解析索引名正则
     */
    private static final String MYSQL_INTEGRITY_INDEX_PATTERN = "(?<=CONSTRAINT `).*?(?=` FOREIGN KEY)";

    /**
     * H2外键约束表名解析正则
     */
    private static final String H2_INTEGRITY_TABLE_PATTERN = "(?<=PUBLIC.).*?(?= FOREIGN)";

    /**
     * MYSQL外键约束表名解析正则
     */
    private static final String MYSQL_INTEGRITY_TABLE_PATTERN = "(?<=\\.`).*?(?=\\`, CONSTRAINT)";


    /**
     * 获取约束名称正则
     */
    private static final Pattern INDEX_PATTERN = Pattern.compile(H2_UNIQUE_INDEX_PATTERN
        + "|" + MYSQL_UNIQUE_INDEX_PATTERN + "|" + H2_INTEGRITY_INDEX_PATTERN + "|" + MYSQL_INTEGRITY_INDEX_PATTERN);


    /**
     * 获取表名正则
     */
    private static final Pattern TABLE_PATTERN = Pattern
        .compile("(?<=into ).*?(?= \\()|(?<=update ).*?(?= set)|(?<=delete from ).*?(?= where)"
            + "|" + H2_INTEGRITY_TABLE_PATTERN + "|" + MYSQL_INTEGRITY_TABLE_PATTERN);


    public void handle(SQLException throwable) {
        String message = throwable.getMessage();
        String indexName = getIndexName(message);
        String tableName = getTableName(message).toUpperCase();
        throwErrorMsg(indexName, tableName);
    }


    private String getIndexName(String message) {
        Matcher indexNameMatcher = INDEX_PATTERN.matcher(message);
        if (indexNameMatcher.find()) {
            return indexNameMatcher.group();
        }
        return null;
    }

    private String getTableName(String message) {
        Matcher tableNameMatcher = TABLE_PATTERN.matcher(message);
        if (tableNameMatcher.find()) {
            return tableNameMatcher.group();
        }
        return null;
    }

    protected void throwErrorMsg(String indexName, String tableName) {
        if (StringUtil.isEmpty(indexName) || StringUtil.isEmpty(tableName)) {
            LOGGER.debug("indexName or tableName isEmpty {},{}", indexName, tableName);
            return;
        }
        String entityName = entityTableMapping.get(tableName);
        if (StringUtil.isEmpty(entityName)) {
            LOGGER.debug("cant find entity table name is {}", tableName);
            return;
        }
        List<Field> fields = BeanUtil.getAllFields(BeanUtil.getClassType(entityName));
        for (Field field : fields) {
            ConstraintViolationMessage constraintViolationMessage = field.getAnnotation(ConstraintViolationMessage.class);
            if (null != constraintViolationMessage && constraintViolationMessage.name().equals(indexName)) {
                throw new ErrMsgException(I18NUtil.getMessage(constraintViolationMessage.message()));
            }
        }
    }
}
