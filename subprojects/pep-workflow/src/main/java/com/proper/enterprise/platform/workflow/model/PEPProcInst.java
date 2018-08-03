package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;

import static com.proper.enterprise.platform.core.PEPConstants.DEFAULT_DATETIME_FORMAT;

public class PEPProcInst {

    public PEPProcInst(ProcessInstance processInstance) {
        this.setProcInstId(processInstance.getId());
        this.setCreateTime(DateUtil.toString(processInstance.getStartTime(), DEFAULT_DATETIME_FORMAT));
        this.setEnded(processInstance.isEnded());
        this.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        this.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        this.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        this.setStartUserId(processInstance.getStartUserId());
    }

    public PEPProcInst(HistoricProcessInstance historicProcessInstance) {
        this.setCreateTime(DateUtil.toString(historicProcessInstance.getStartTime(), DEFAULT_DATETIME_FORMAT));
        this.setEnded(null != historicProcessInstance.getEndTime());
        this.setProcInstId(historicProcessInstance.getId());
        this.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        this.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
        this.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
        this.setStartUserId(historicProcessInstance.getStartUserId());
        this.setEndTime(this.getEnded()
            ? DateUtil.toString(historicProcessInstance.getEndTime(), DEFAULT_DATETIME_FORMAT)
            : null);
    }

    /**
     * 流程实例Id
     */
    private String procInstId;

    /**
     * 流程定义Id
     */
    private String processDefinitionId;

    /**
     * 流程定义key
     */
    private String processDefinitionKey;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程启动时间
     */
    private String createTime;

    /**
     * 流程结束时间
     */
    private String endTime;

    /**
     * 流程启动者Id
     */
    private String startUserId;

    /**
     * 流程启动者名称
     */
    private String startUserName;

    /**
     * 是否结束  true是 false否
     */
    private Boolean ended;

    /**
     * 流程状态
     */
    private String stateCode;
    /**
     * 流程状态
     */
    private String stateValue;


    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartUserName() {
        if (StringUtil.isNotEmpty(this.startUserName)) {
            return this.startUserName;
        }
        if (StringUtil.isEmpty(this.getStartUserId())) {
            return null;
        }
        User user = PEPApplicationContext.getApplicationContext().getBean(UserDao.class).findOne(this.getStartUserId());
        if (null == user) {
            return null;
        }
        return user.getName();
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public Boolean getEnded() {
        return ended;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    public String getStateCode() {
        if (StringUtil.isNotEmpty(this.stateCode)) {
            return this.stateCode;
        }
        return this.getEnded() ? PEPProcInstStateEnum.DONE.name() : PEPProcInstStateEnum.DOING.name();
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateValue() {
        if (StringUtil.isNotEmpty(this.stateValue)) {
            return this.stateValue;
        }
        return this.getEnded()
            ? DataDicUtil.get(PEPProcInstStateEnum.DONE).getName()
            : DataDicUtil.get(PEPProcInstStateEnum.DOING).getName();
    }

    public void setStateValue(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }


    public PEPProcInstVO convert() {
        return BeanUtil.convert(this, PEPProcInstVO.class);
    }
}
