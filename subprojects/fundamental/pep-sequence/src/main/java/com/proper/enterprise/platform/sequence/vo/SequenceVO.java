package com.proper.enterprise.platform.sequence.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;

import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;

public class SequenceVO extends BaseVO {

    public SequenceVO(){}

    /**
     * 流水编码
     */
    @NotBlank(message = "{sequence.sequenceCode.notblank.error}")
    private String sequenceCode;

    /**
     * 流水名称
     */
    @NotBlank(message = "{sequence.sequenceName.notblank.error}")
    private String sequenceName;

    /**
     * 规则
     */
    @NotBlank(message = "{sequence.formula.notblank.error}")
    private String formula;

    /**
     * 清零方式(不清零, 日清零, 月清零, 年清零)
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLiteBean clearType;

    /**
     * 初始值
     */
    private String initialValue;

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

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
