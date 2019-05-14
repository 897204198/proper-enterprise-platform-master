package com.proper.enterprise.platform.workflow.plugin.service.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.mongo.constants.MongoConstants;
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.plugin.service.AutoArchiveService;
import org.bson.Document;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "autoArchivePupa")
public class AutoArchivePupaServiceImpl implements AutoArchiveService {

    @Autowired
    private MongoDAO mongoDAO;

    private static final String CONFIG_COLLECTION_NAME = "PEP_DEVTOOLS_CUSTOMQUERY";

    private static final String TABLE_NAME_KEY = "tableName";

    @Override
    public void archive(ExecutionEntity execution, String... forms) {
        if (null == forms) {
            return;
        }
        for (String form : forms) {
            try {
                insert(execution, form);
            } catch (Exception e) {
                throw new ErrMsgException("Pupa Archive Error" + e.getMessage());
            }
        }
    }

    private String getTableName(String form) throws Exception {
        String code = form.replace("@", "");
        List<Document> configs = mongoDAO.query(CONFIG_COLLECTION_NAME, "{code:'" + code + "'}");
        if (CollectionUtil.isEmpty(configs)) {
            throw new ErrMsgException("Pupa Archive Error,cant find config by code,code is" + code);
        }
        if (configs.size() > 1) {
            throw new ErrMsgException("Pupa Archive Error, find More than one config by code,code is" + code);
        }
        Document config = configs.get(0);
        String tableName = (String) config.get(TABLE_NAME_KEY);
        if (StringUtil.isEmpty(tableName)) {
            throw new ErrMsgException("Pupa Archive Error, tableName is Empty,code is" + code);
        }
        return tableName;
    }

    @SuppressWarnings("unchecked")
    private void insert(ExecutionEntity execution, String form) throws Exception {
        String tableName = getTableName(form);
        Map<String, Object> formData = (Map<String, Object>) execution.getVariable(form);
        if (null != formData) {
            formData.put(MongoConstants.CREATE_TIME, DateUtil.getTimestamp());
            formData.put(MongoConstants.LAST_MODIFY_TIME, DateUtil.getTimestamp());
            formData.put(MongoConstants.CREATE_USER_ID, Authentication.getCurrentUserId());
            formData.put(MongoConstants.LAST_MODIFY_USER_ID, Authentication.getCurrentUserId());
            mongoDAO.insertOne(tableName, JSONUtil.toJSONIgnoreException(formData));
        }
    }
}
