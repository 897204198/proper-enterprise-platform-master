package com.proper.enterprise.platform.sequence.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;

import javax.persistence.*;

@Entity
@Table(name = "PEP_SEQUENCE")
public class SequenceEntity extends BaseEntity {

    public SequenceEntity(){}

    /**
     * 流水编码
     */
    @Column(unique = true, nullable = false)
    private String sequenceCode;

    /**
     * 流水名称
     */
    @Column(nullable = false)
    private String sequenceName;

    /**
     * 规则
     */
    @Column(nullable = false)
    private String formula;

    /**
     * 清零方式(不清零, 日清零, 月清零, 年清零)
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLiteBean clearType;

    public String getSequenceCode() {
        return sequenceCode;
    }

    public void setSequenceCode(String sequenceCode) {
        this.sequenceCode = sequenceCode;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public DataDicLiteBean getClearType() {
        return clearType;
    }

    public void setClearType(DataDicLiteBean clearType) {
        this.clearType = clearType;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
