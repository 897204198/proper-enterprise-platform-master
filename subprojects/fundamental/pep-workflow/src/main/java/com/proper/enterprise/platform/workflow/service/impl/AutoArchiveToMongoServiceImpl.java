package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.mongo.constants.MongoConstants;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.service.AutoArchiveService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(value = "autoArchiveToMongoService")
public class AutoArchiveToMongoServiceImpl implements AutoArchiveService {

    private MongoShellService mongoShellService;

    @Autowired
    public AutoArchiveToMongoServiceImpl(MongoShellService mongoShellService) {
        this.mongoShellService = mongoShellService;
    }

    @Override
    public void archive(ExecutionEntity execution, String... forms) {
        if (null == forms) {
            return;
        }
        for (String form : forms) {
            insert(execution, form);
        }
    }

    @SuppressWarnings("unchecked")
    private void insert(ExecutionEntity execution, String formKey) {
        if (StringUtil.isEmpty(formKey)) {
            return;
        }
        Map<String, Object> formData = (Map<String, Object>) execution.getVariable(formKey);
        if (null != formData) {
            formData.put(MongoConstants.CREATE_TIME, DateUtil.getTimestamp());
            formData.put(MongoConstants.LAST_MODIFY_TIME, DateUtil.getTimestamp());
            formData.put(MongoConstants.CREATE_USER_ID, Authentication.getCurrentUserId());
            formData.put(MongoConstants.LAST_MODIFY_USER_ID, Authentication.getCurrentUserId());
            mongoShellService.insertOne(formKey, JSONUtil.toJSONIgnoreException(formData));
        }
    }
}
