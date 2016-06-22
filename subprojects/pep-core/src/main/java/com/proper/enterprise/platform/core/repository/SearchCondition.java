package com.proper.enterprise.platform.core.repository;

import org.springframework.util.Assert;

/**
 * 查询条件抽象表示
 * 每一个实例代表一个查询条件，包含字段、值和操作符
 */
public class SearchCondition {

    public enum Operator {
        EQ,         // =
        LIKE,       // like
        GT,         // >
        LT,         // <
        GE,         // >=
        LE,         // <=
        IN,         // in
        NOTNULL,    // != null
        ASC,        // asc
        DESC        // desc
    }

    private String field;
    private Operator operator;
    private Object value;

    /**
     * 构造一个 field = val 的查询条件
     *
     * @param field 字段名
     * @param val   值
     */
    public SearchCondition(String field, Object val) {
        this(field, Operator.EQ, val);
    }

    /**
     * 构造一个 field op val 的查询条件
     * @param field 字段名
     * @param op    操作符
     * @param val   值
     */
    public SearchCondition(String field, Operator op, Object val) {
        this.field = field;
        this.operator = op;
        this.value = val;
    }

    /**
     * 构造一个单值条件，如升降序、不为空
     *
     * @param field 字段名
     * @param op    操作符
     */
    public SearchCondition(String field, Operator op) {
        Assert.isTrue(op.equals(Operator.ASC)
                        || op.equals(Operator.DESC)
                        || op.equals(Operator.NOTNULL),
                      "This constructor only supports ASC or DESC or NOTNULL operator!");
        this.field = field;
        this.operator = op;
    }

    /**
     * 取得当前查询条件中的“值”
     *
     * @return 当前查询条件中的“值”
     */
    public Object getValue() {
        return value;
    }

    /**
     * 取得当前查询条件中的“字段名”
     *
     * @return 当前查询条件中的“字段名”
     */
    public String getField() {
        return field;
    }

    /**
     * 取得当前查询条件中的“操作符”
     *
     * @return 当前查询条件中的“操作符”
     */
    public Operator getOperator() {
        return operator;
    }

}
