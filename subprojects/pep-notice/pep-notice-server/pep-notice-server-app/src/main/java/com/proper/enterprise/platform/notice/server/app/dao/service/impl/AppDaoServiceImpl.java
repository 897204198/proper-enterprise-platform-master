package com.proper.enterprise.platform.notice.server.app.dao.service.impl;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.factory.NoticeConfiguratorFactory;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.app.dao.entity.AppEntity;
import com.proper.enterprise.platform.notice.server.app.dao.repository.AppRepository;
import com.proper.enterprise.platform.notice.server.app.vo.AppVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppDaoServiceImpl implements AppDaoService {

    private AppRepository appRepository;

    private AccessTokenService accessTokenService;

    @Value("${notice.server.app.resource}")
    private String resourceDesc;

    @Autowired
    public AppDaoServiceImpl(AppRepository appRepository, @Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.appRepository = appRepository;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public App get(String appKey) {
        return buildHaveConf(BeanUtil.convert(appRepository.findByAppKey(appKey), AppVO.class));
    }

    @Override
    public DataTrunk<App> findAll(String appKey, String appName, String describe, Boolean enable, PageRequest pageRequest) {
        DataTrunk<App> data = new DataTrunk<>();
        Page<AppEntity> page = appRepository.findAll(appKey, appName, describe, enable, pageRequest);
        List<AppVO> appVOs = new ArrayList<>(BeanUtil.convert(page.getContent(), AppVO.class));
        data.setData(new ArrayList<>(appVOs));
        data.setCount(page.getTotalElements());
        return data;
    }

    @Override
    public DataTrunk<App> findAllWithHaveConf(String appKey, String appName, String describe, Boolean enable, PageRequest pageRequest) {
        DataTrunk<App> apps = findAll(appKey, appName, describe, enable, pageRequest);
        for (App app : apps.getData()) {
            buildHaveConf((AppVO) app);
        }
        return apps;
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
            Optional<AccessToken> accessTokenOpt = accessTokenService.getByUserId(app.getAppKey());
            if (accessTokenOpt.isPresent()) {
                AccessToken accessToken = accessTokenOpt.get();
                accessToken.setToken(app.getAppToken());
                accessTokenService.saveOrUpdate(accessToken);
            } else {
                accessTokenService.saveOrUpdate(buildToken(app.getAppKey(), app.getAppToken()));
            }
        }
        return BeanUtil.convert(appRepository.updateForSelective(BeanUtil.convert(app, AppEntity.class)), AppVO.class);
    }

    @Override
    public boolean delete(String appIds) {
        if (StringUtil.isEmpty(appIds)) {
            return false;
        }
        String[] ids = appIds.split(",");
        for (String id : ids) {
            App app = appRepository.findOne(id);
            if (null == app) {
                continue;
            }
            accessTokenService.deleteByToken(app.getAppToken());
            appRepository.deleteById(id);
        }
        return true;
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

    @Override
    public List<App> findAppByAppKey(List<String> appKeys) {
        if (appKeys != null && appKeys.size() == 0) {
            throw new ErrMsgException("appKey is empty");
        }
        List<AppEntity> entities = appRepository.findAppsByAppKey(appKeys);
        ArrayList<App> result = new ArrayList<>();
        for (AppEntity entity : entities) {
            result.add(BeanUtil.convert(entity, AppVO.class));
        }
        return result;
    }

    @Override
    public List<App> findByApp() {
        List<AppEntity> entities = appRepository.findAll();
        ArrayList<App> result = new ArrayList<>();
        for (AppEntity entity : entities) {
            result.add(BeanUtil.convert(entity, AppVO.class));
        }
        return result;
    }

    /**
     * 判断配置详情中是否配置了 EMAIL/SMS/PUSH
     *
     * @param appVO 配置详情
     * @return 配置详情以及配置渠道
     */
    private AppVO buildHaveConf(AppVO appVO) {
        appVO.setHaveEmailConf(false);
        appVO.setHaveSMSConf(false);
        appVO.setHavePushConf(false);
        if (null != NoticeConfiguratorFactory.product(NoticeType.EMAIL).get(appVO.getAppKey(), null)) {
            appVO.setHaveEmailConf(true);
        }
        if (null != NoticeConfiguratorFactory.product(NoticeType.SMS).get(appVO.getAppKey(), null)) {
            appVO.setHaveSMSConf(true);
        }
        Map<String, Object> xiaoMiConf = new HashMap<>(16);
        xiaoMiConf.put("pushChannel", PushChannelEnum.XIAOMI.name());
        if (null != NoticeConfiguratorFactory.product(NoticeType.PUSH).get(appVO.getAppKey(), xiaoMiConf)) {
            appVO.setHavePushConf(true);
            return appVO;
        }
        Map<String, Object> huaweiConf = new HashMap<>(16);
        huaweiConf.put("pushChannel", PushChannelEnum.HUAWEI.name());
        if (null != NoticeConfiguratorFactory.product(NoticeType.PUSH).get(appVO.getAppKey(), huaweiConf)) {
            appVO.setHavePushConf(true);
            return appVO;
        }
        Map<String, Object> iosConf = new HashMap<>(16);
        iosConf.put("pushChannel", PushChannelEnum.APNS.name());
        if (null != NoticeConfiguratorFactory.product(NoticeType.PUSH).get(appVO.getAppKey(), iosConf)) {
            appVO.setHavePushConf(true);
        }
        return appVO;
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
        appToken.setResourcesDescription(resourceDesc);
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
