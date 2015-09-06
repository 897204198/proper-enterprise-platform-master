package com.proper.enterprise.platform.core.repository;

public class SearchCondition {

    public enum Operator {
        EQ, LIKE, GT, LT, GE, LE
    }

    private String field;
    private Operator operator;
    private Object value;

    public SearchCondition(String field, Object val) {
        this(field, Operator.EQ, val);
    }

    public SearchCondition(String field, Operator op, Object val) {
        this.field = field;
        this.operator = op;
        this.value = val;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
