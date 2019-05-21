package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.service.impl.PEPCandidateUserExtQueryImpl;

import java.util.ArrayList;
import java.util.List;

public class CandidateIdUtil {

    private CandidateIdUtil() {
    }

    private static final int PARSE_SIZE = 2;

    /**
     * 根据候选id和类型 生成保存在流程定义中的实际候选id
     *
     * @param id   候选id
     * @param type 候选类型
     * @return 实际保存在流程定义的候选id
     */
    public static String encode(String id, String type) {
        if (StringUtil.isEmpty(id)) {
            throw new ErrMsgException("CandidateId can't be empty");
        }
        if (StringUtil.isEmpty(type)) {
            throw new ErrMsgException("CandidateType can't be empty");
        }
        //用户Id flowable中不做处理
        if (PEPCandidateUserExtQueryImpl.USER_CONF_CODE.equals(type)) {
            return id;
        }
        return id + "_" + type;
    }

    /**
     * 根据候选id集合和类型 批量生成保存在流程定义中的实际候选id
     *
     * @param ids  候选id集合
     * @param type 候选类型
     * @return 实际保存在流程定义的候选id
     */
    public static List<String> encode(List<String> ids, String type) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        if (StringUtil.isEmpty(type)) {
            throw new ErrMsgException("CandidateType can't be empty");
        }
        List<String> encodeIds = new ArrayList<>();
        for (String id : ids) {
            encodeIds.add(encode(id, type));
        }
        return encodeIds;
    }


    /**
     * 根据保存在流程定义中的候选id 获得业务中的真实id
     *
     * @param wfCandidateId 保存在流程定义中的候选id
     * @return 业务中的真实id
     */
    public static CandidateId decode(String wfCandidateId) {
        if (StringUtil.isEmpty(wfCandidateId)) {
            throw new ErrMsgException("wfCandidateId is empty");
        }
        String[] parse = wfCandidateId.split("_");
        if (parse.length != PARSE_SIZE) {
            throw new ErrMsgException("wfCandidateId is not a standard format");
        }
        return new CandidateId(parse[0], parse[1]);
    }


    public static class CandidateId {

        public CandidateId(String id, String type) {

            this.id = id;
            this.type = type;
        }

        /**
         * 业务真实id
         */
        private String id;
        /**
         * 业务类型
         */
        private String type;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

    }


}
