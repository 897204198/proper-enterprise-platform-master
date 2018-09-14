package com.proper.enterprise.platform.notice.server.app.dao.service.impl;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.app.dao.entity.AppEntity;
import com.proper.enterprise.platform.notice.server.app.dao.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class AppDaoServiceImpl implements AppDaoService {

    private AppRepository appRepository;

    private AccessTokenService accessTokenService;

    @Autowired
    public AppDaoServiceImpl(AppRepository appRepository, AccessTokenService accessTokenService) {
        this.appRepository = appRepository;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public App save(App app) {
        return appRepository.save(BeanUtil.convert(app, AppEntity.class));
    }

    @Override
    public App updateAppToken(String appKey) {
        AppEntity appEntity = appRepository.findByAppKey(appKey);
        accessTokenService.deleteByToken(appEntity.getAppToken());
        AccessToken accessToken = accessTokenService.saveOrUpdate(buildToken(appKey));
        appEntity.setAppToken(accessToken.getToken());
        return appRepository.updateForSelective(appEntity);
    }

    @Override
    public App updateAppStatus(String appKey, boolean enable) {
        AppEntity appEntity = appRepository.findByAppKey(appKey);
        appEntity.setEnable(enable);
        return appRepository.updateForSelective(appEntity);
    }

    @Override
    public App updateAppName(String appKey, String appName) {
        AppEntity appEntity = appRepository.findByAppKey(appKey);
        appEntity.setAppName(appName);
        return appRepository.updateForSelective(appEntity);
    }

    @Override
    public App updateAppDescribe(String appKey, String describe) {
        AppEntity appEntity = appRepository.findByAppKey(appKey);
        appEntity.setDescribe(describe);
        return appRepository.updateForSelective(appEntity);
    }

    /**
     * 根据appKey 构造token
     *
     * @param appKey 应用唯一标识
     * @return token
     */
    private AppToken buildToken(String appKey) {
        AppToken appToken = new AppToken();
        appToken.setUserId(appKey);
        appToken.setName(appKey);
        appToken.setResourcesDescription(appKey + "token");
        appToken.setToken(UUID.randomUUID().toString());
        return appToken;
    }

    /**
     * token模型
     */
    public class AppToken extends BaseVO implements AccessToken {
        private String userId;
        private String name;
        private String token;
        private String resourcesDescription;

        @Override
        public String getUserId() {
            return userId;
        }

        @Override
        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getToken() {
            return token;
        }

        @Override
        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String getResourcesDescription() {
            return resourcesDescription;
        }

        @Override
        public void setResourcesDescription(String resourcesDescription) {
            this.resourcesDescription = resourcesDescription;
        }
    }
}
