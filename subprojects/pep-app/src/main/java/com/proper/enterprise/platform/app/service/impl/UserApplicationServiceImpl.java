package com.proper.enterprise.platform.app.service.impl;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {

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
