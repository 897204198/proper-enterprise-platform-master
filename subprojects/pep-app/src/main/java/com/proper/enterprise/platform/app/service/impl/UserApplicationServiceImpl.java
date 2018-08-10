package com.proper.enterprise.platform.app.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.app.entity.ApplicationEntity;
import com.proper.enterprise.platform.app.entity.UserApplicationEntity;
import com.proper.enterprise.platform.app.repository.ApplicationRepository;
import com.proper.enterprise.platform.app.repository.UserApplicationsRepository;
import com.proper.enterprise.platform.app.service.ApplicationService;
import com.proper.enterprise.platform.app.service.UserApplicationService;
import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.app.vo.UserApplicationVO;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserApplicationServiceImpl.class);

    private static final String COMMA = ",";

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    ApplicationRepository applicationRepo;

    @Autowired
    UserApplicationsRepository userApplicationsRepo;

    @Override
    public List<ApplicationVO> findUserApplications() {
        UserApplicationEntity userApplication = userApplicationsRepo.getByUserId(Authentication.getCurrentUserId());
        if (userApplication == null) {
            return findDefaultApplication();
        }
        List<ApplicationVO> list = new ArrayList<>();
        String ids = userApplication.getAppId();
        if (ids.isEmpty()) {
            return list;
        }
        for (String appId : ids.split(COMMA)) {
            ApplicationVO applicationVO = applicationService.getApplication(appId);
            if (applicationVO == null) {
                continue;
            }
            list.add(applicationVO);
        }
        return list;
    }

    @Override
    public List<ApplicationVO> findDefaultApplication() {
        List<ApplicationVO> list = new ArrayList<>();
        List<ApplicationEntity> applications = applicationRepo.findByDefaultValueTrue();
        for (ApplicationEntity applicationEntity : applications) {
            ApplicationVO applicationVO = new ApplicationVO();
            BeanUtils.copyProperties(applicationEntity, applicationVO);
            String data = applicationEntity.getData();
            Map<String, String> map = new HashMap<>(16);
            try {
                JsonNode node = JSONUtil.parse(data, JsonNode.class);
                Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
                while (iterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = iterator.next();
                    map.put(entry.getKey(), entry.getValue().textValue());
                }
            } catch (IOException e) {
                LOGGER.warn("Data of json parse exception!", e);
            }
            applicationVO.setData(map);
            list.add(applicationVO);
        }
        return list;
    }

    @Override
    public UserApplicationVO updateUserApplications(String ids) {
        UserApplicationEntity userApplicationEntity = userApplicationsRepo.getByUserId(Authentication.getCurrentUserId());
        userApplicationEntity.setAppId(ids);
        userApplicationEntity = userApplicationsRepo.save(userApplicationEntity);
        UserApplicationVO userApplicationVO = new UserApplicationVO();
        BeanUtils.copyProperties(userApplicationEntity, userApplicationVO);
        return userApplicationVO;
    }

    @Override
    public List<AppCatalogVO> findCatalogAndApplications() {
        //catalog + application
        List<AppCatalogVO> appCatalogVOS = applicationService.getCatalogs();
        for (AppCatalogVO catalogDoc : appCatalogVOS) {
            String code = catalogDoc.getCode();
            List<ApplicationVO> applicationDoc = applicationService.getApplicationByCode(code);
            catalogDoc.setApps(applicationDoc);
        }
        return appCatalogVOS;
    }

}
