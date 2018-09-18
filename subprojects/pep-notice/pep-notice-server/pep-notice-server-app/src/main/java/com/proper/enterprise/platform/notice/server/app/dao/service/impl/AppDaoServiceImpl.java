package com.proper.enterprise.platform.notice.server.app.dao.service.impl;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.app.dao.entity.AppEntity;
import com.proper.enterprise.platform.notice.server.app.dao.repository.AppRepository;
import com.proper.enterprise.platform.notice.server.api.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AppDaoServiceImpl implements AppDaoService {

    private AppRepository appRepository;

    private AccessTokenService accessTokenService;

    /**
     * 应用拥有的权限
     */
    private static final String RESOURCE_DESC = "POST:/notice/server/send,"
        + "POST:/notice/server/config,"
        + "PUT:/notice/server/config,"
        + "DELETE:/notice/server/config,"
        + "GET:/notice/server/config";

    @Autowired
    public AppDaoServiceImpl(AppRepository appRepository, @Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.appRepository = appRepository;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public App get(String appKey) {
        return BeanUtil.convert(appRepository.findByAppKey(appKey), AppVO.class);
    }

    @Override
    public DataTrunk<App> findAll(String appKey, String appName, String describe, Boolean enable, PageRequest pageRequest) {
        DataTrunk<App> data = new DataTrunk<>();
        Page<AppEntity> page = appRepository.findAll(appKey, appName, describe, enable, pageRequest);
        data.setData(new ArrayList<>(BeanUtil.convert(page.getContent(), AppVO.class)));
        data.setCount(page.getTotalElements());
        return data;
    }


    @Override
    public App save(App app) {
        if (null == app.getEnable()) {
            app.setEnable(true);
        }
        accessTokenService.saveOrUpdate(buildToken(app.getAppKey(), app.getAppToken()));
        return BeanUtil.convert(appRepository.save(BeanUtil.convert(app, AppEntity.class)), AppVO.class);
    }

    @Override
    public App updateApp(App app) {
        App oldApp = appRepository.findOne(app.getId());
        if (!oldApp.getAppKey().equals(app.getAppKey())) {
            throw new ErrMsgException("appKey can't change");
        }
        if (!oldApp.getAppToken().equals(app.getAppToken())) {
            accessTokenService.deleteByToken(oldApp.getAppToken());
            accessTokenService.saveOrUpdate(buildToken(app.getAppKey(), app.getAppToken()));
        }
        return BeanUtil.convert(appRepository.updateForSelective(BeanUtil.convert(app, AppEntity.class)), AppVO.class);
    }

    @Override
    public boolean delete(String appId) {
        App app = appRepository.findOne(appId);
        if (null == app) {
            return false;
        }
        accessTokenService.deleteByToken(app.getAppToken());
        return appRepository.deleteById(appId);
    }

    @Override
    public void updateAppsEnable(String appIds, boolean enable) {
        if (StringUtil.isEmpty(appIds)) {
            return;
        }
        String[] ids = appIds.split(",");
        List<AppEntity> apps = appRepository.findAll(Arrays.asList(ids));
        for (AppEntity appEntity : apps) {
            appEntity.setEnable(enable);
            appRepository.updateForSelective(appEntity);
        }
    }

    @Override
    public boolean isEnable(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            return false;
        }
        AppEntity appEntity = appRepository.findByAppKey(appKey);
        if (null == appEntity) {
            return false;
        }
        return appEntity.getEnable();
    }

    /**
     * 根据appKey 构造token
     *
     * @param appKey 应用唯一标识
     * @param token  token
     * @return token
     */
    private AppToken buildToken(String appKey, String token) {
        AppToken appToken = new AppToken();
        appToken.setUserId(appKey);
        appToken.setName(appKey);
        appToken.setResourcesDescription(RESOURCE_DESC);
        appToken.setToken(token);
        return appToken;
    }

    /**
     * token模型
     */
    public static class AppToken extends BaseVO implements AccessToken {
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
