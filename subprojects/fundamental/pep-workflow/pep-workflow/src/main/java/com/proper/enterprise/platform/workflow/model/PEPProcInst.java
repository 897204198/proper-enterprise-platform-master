package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.utils.*;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.core.utils.TmplUtil;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import org.flowable.bpmn.model.ValuedDataObject;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

public class PEPProcInst {

    public PEPProcInst(ProcessInstance processInstance) {
        this.setProcInstId(processInstance.getId());
        this.setCreateTime(DateUtil.toString(DateUtil.toLocalDateTime(processInstance.getStartTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat()));
        this.setEnded(processInstance.isEnded());
        this.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        this.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        this.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        this.setStartUserId(processInstance.getStartUserId());
        this.setProcessTitle(buildProcessTitle(processInstance.getProcessVariables()));
    }

    public PEPProcInst(HistoricProcessInstance historicProcessInstance) {
        this.setCreateTime(DateUtil.toString(DateUtil.toLocalDateTime(historicProcessInstance.getStartTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat()));
        this.setEnded(null != historicProcessInstance.getEndTime());
        this.setProcInstId(historicProcessInstance.getId());
        this.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        this.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
        this.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
        this.setStartUserId(historicProcessInstance.getStartUserId());
        this.setEndTime(this.getEnded()
            ? DateUtil.toString(DateUtil.toLocalDateTime(historicProcessInstance.getEndTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat())
            : null);
        this.setProcessTitle(buildProcessTitle(historicProcessInstance.getProcessVariables()));
    }

    /**
     * ????????????Id
     */
    private String procInstId;

    /**
     * ????????????Id
     */
    private String processDefinitionId;

    /**
     * ????????????key
     */
    private String processDefinitionKey;

    /**
     * ??????????????????
     */
    private String processDefinitionName;

    /**
     * ????????????
     */
    private String processTitle;

    /**
     * ??????????????????
     */
    private String createTime;

    /**
     * ??????????????????
     */
    private String endTime;

    /**
     * ???????????????Id
     */
    private String startUserId;

    /**
     * ?????????????????????
     */
    private String startUserName;

    /**
     * ????????????  true??? false???
     */
    private Boolean ended;

    /**
     * ????????????
     */
    private String stateCode;
    /**
     * ????????????
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

    public String getProcessTitle() {
        return processTitle;
    }

    public void setProcessTitle(String processTitle) {
        this.processTitle = processTitle;
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
        User user = PEPApplicationContext.getApplicationContext().getBean(UserDao.class).findById(this.getStartUserId());
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

    private static String buildProcessTitle(Map<String, Object> globalVariables) {
        return (String) globalVariables.get(WorkFlowConstants.PROCESS_TITLE);
    }

    public static String buildProcessTitle(List<ValuedDataObject> datas, Map<String, Object> globalVariables) {
        String processTitleTmpl = "";
        String defaultTitleTmpl = I18NUtil.getMessage("workflow.default.process.title");
        if (CollectionUtil.isEmpty(datas)) {
            return TmplUtil.resolveTmpl(defaultTitleTmpl, globalVariables);
        }
        for (ValuedDataObject data : datas) {
            if (WorkFlowConstants.PROCESS_TITLE.equals(data.getName())) {
                processTitleTmpl = (String) data.getValue();
            }
        }
        if (StringUtil.isEmpty(processTitleTmpl)) {
            return TmplUtil.resolveTmpl(defaultTitleTmpl, globalVariables);
        }
        return TmplUtil.resolveTmpl(processTitleTmpl, globalVariables);
    }

}
