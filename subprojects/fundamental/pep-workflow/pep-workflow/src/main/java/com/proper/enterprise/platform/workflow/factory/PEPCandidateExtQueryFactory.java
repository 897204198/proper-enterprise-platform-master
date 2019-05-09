package com.proper.enterprise.platform.workflow.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;
import com.proper.enterprise.platform.workflow.service.PEPCandidateExtQuery;
import com.proper.enterprise.platform.workflow.util.WFIdmQueryConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PEPCandidateExtQueryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PEPCandidateExtQueryFactory.class);


    private PEPCandidateExtQueryFactory() {

    }


    /**
     * 根据类型 获得对应的候选查询扩展实现
     *
     * @param type 类型
     * @return 候选查询扩展实现
     */
    public static PEPCandidateExtQuery product(String type) {
        if (StringUtil.isEmpty(type)) {
            throw new ErrMsgException("PEPCandidateExtQuery can't find impl by empty type");
        }
        WFIdmQueryConfEntity wfIdmQueryConfEntity = WFIdmQueryConfUtil.findByType(type);
        if (null == wfIdmQueryConfEntity || StringUtil.isEmpty(wfIdmQueryConfEntity.getBeanName())) {
            throw new ErrMsgException("PEPCandidateExtQuery can't find impl by " + type);
        }
        try {
            return (PEPCandidateExtQuery) PEPApplicationContext.getBean(wfIdmQueryConfEntity.getBeanName());
        } catch (ErrMsgException e) {
            LOGGER.error("PEPCandidateExtQuery find error class not found", e);
            throw new ErrMsgException("PEPCandidateExtQuery query bean not found:" + wfIdmQueryConfEntity.getBeanName());
        }
    }
}
