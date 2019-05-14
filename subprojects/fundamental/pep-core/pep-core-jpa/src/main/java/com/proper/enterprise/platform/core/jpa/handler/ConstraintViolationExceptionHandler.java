package com.proper.enterprise.platform.core.jpa.handler;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.core.jpa.annotation.ConstraintViolationMessage;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConstraintViolationExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionHandler.class);

    @Autowired
    private Map<String, String> entityTableMapping;

    private static final String UNIQUE_INDEX_VIOLATION_KEY = "Unique index or primary key violation";

    private static final String REFERENTIAL_INTEGRITY_VIOLATION_KEY = "Referential integrity constraint violation";

    /**
     * 唯一约束获取约束名称正则
     */
    private static final Pattern UNIQUE_INDEX_PATTERN = Pattern.compile("(?<=" + UNIQUE_INDEX_VIOLATION_KEY + ": \").*?(?= ON)");

    /**
     * 唯一约束获取表名正则
     */
    private static final Pattern UNIQUE_TABLE_PATTERN = Pattern.compile("(?<=ON PUBLIC.).*?(?=\\()");

    /**
     * 完整约束获取约束名称正则
     */
    private static final Pattern INTEGRITY_INDEX_PATTERN = Pattern.compile("(?<=" + REFERENTIAL_INTEGRITY_VIOLATION_KEY + ": \\\").*?(?=\\:)");

    /**
     * 完整约束获取表名正则
     */
    private static final Pattern INTEGRITY_TABLE_PATTERN = Pattern.compile("(?<=PUBLIC.).*?(?= FOREIGN)");

    public void handle(ConstraintViolationException throwable) {
        String message = throwable.getSQLException().getLocalizedMessage();
        if (StringUtil.isEmpty(message)) {
            return;
        }
        String[] keys = message.split("\\:");
        if (keys.length == 0) {
            return;
        }
        String messageKey = keys[0];
        if (UNIQUE_INDEX_VIOLATION_KEY.equals(messageKey)) {
            uniqueHandle(message);
        }
        if (REFERENTIAL_INTEGRITY_VIOLATION_KEY.equals(messageKey)) {
            integrityHandle(message);
        }
    }

    private void throwErrorMsg(String indexName, String tableName) {
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

    private void uniqueHandle(String message) {
        String indexName = getUniqueIndexName(message);
        String tableName = getUniqueTableName(message).toUpperCase();
        throwErrorMsg(indexName, tableName);
    }

    private void integrityHandle(String message) {
        String indexName = getIntegrityIndexName(message);
        String tableName = getIntegrityTableName(message).toUpperCase();
        throwErrorMsg(indexName, tableName);
    }


    private String getIntegrityIndexName(String message) {
        Matcher indexNameMatcher = INTEGRITY_INDEX_PATTERN.matcher(message);
        if (indexNameMatcher.find()) {
            return indexNameMatcher.group();
        }
        return null;
    }

    private String getIntegrityTableName(String message) {
        Matcher tableNameMatcher = INTEGRITY_TABLE_PATTERN.matcher(message);
        if (tableNameMatcher.find()) {
            return tableNameMatcher.group();
        }
        return null;
    }

    private String getUniqueIndexName(String message) {
        Matcher indexNameMatcher = UNIQUE_INDEX_PATTERN.matcher(message);
        if (indexNameMatcher.find()) {
            return indexNameMatcher.group();
        }
        return null;
    }

    private String getUniqueTableName(String message) {
        Matcher tableNameMatcher = UNIQUE_TABLE_PATTERN.matcher(message);
        if (tableNameMatcher.find()) {
            return tableNameMatcher.group();
        }
        return null;
    }
}
