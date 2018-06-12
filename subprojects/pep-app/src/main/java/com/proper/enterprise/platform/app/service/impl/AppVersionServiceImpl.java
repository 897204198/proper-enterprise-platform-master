package com.proper.enterprise.platform.app.service.impl;

import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.api.cache.CacheDuration;
import com.proper.enterprise.platform.app.document.AppVersionDocument;
import com.proper.enterprise.platform.app.repository.AppVersionRepository;
import com.proper.enterprise.platform.app.service.AppVersionService;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "pep.sys.AppVersion")
@CacheDuration(cacheName = "pep.sys.AppVersion")
public class AppVersionServiceImpl implements AppVersionService {

    private static final String CACHE_KEY = "'latestVer'";

    private AppVersionRepository repo;

    private UserService userService;

    @Autowired
    public AppVersionServiceImpl(AppVersionRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Override
    public AppVersionDocument saveOrUpdate(AppVersionDocument appVersion) {
        AppVersionDocument version = repo.findByVersion(appVersion.getVersion());
        if (version == null) {
            version = appVersion;
        } else {
            version.setAndroidURL(appVersion.getAndroidURL());
            version.setIosURL(appVersion.getIosURL());
            version.setNote(appVersion.getNote());
        }
        return repo.save(version);
    }

    @Override
    @CachePut(key = CACHE_KEY)
    public AppVersionDocument release(AppVersionDocument appVersionDocument) {
        AppVersionDocument version = repo.findByVersion(appVersionDocument.getVersion());
        if (version == null) {
            version = appVersionDocument;
        }
        version.setReleased(true);
        version.setPublisherId(userService.getCurrentUser().getId());
        version.setPublishTime(DateUtil.toTimestamp(new Date()));
        return repo.save(version);
    }

    @Override
    @CacheEvict(key = CACHE_KEY)
    public void delete(AppVersionDocument version) {
        repo.delete(version);
    }

    @Override
    @Cacheable(key = CACHE_KEY)
    public AppVersionDocument getLatestRelease() {
        return repo.findTopByReleasedTrueOrderByCreateTimeDesc();
    }

    @Override
    public AppVersionDocument get(String version) {
        return repo.findByVersion(version);
    }

    @Override
    public List<AppVersionDocument> list() {
        return repo.findAll(new Sort(Sort.Direction.DESC, "createTime"));
    }
}
