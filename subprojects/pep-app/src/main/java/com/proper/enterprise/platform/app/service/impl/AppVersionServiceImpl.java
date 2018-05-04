package com.proper.enterprise.platform.app.service.impl;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import com.proper.enterprise.platform.app.document.AppVersionDocument;
import com.proper.enterprise.platform.app.repository.AppVersionRepository;
import com.proper.enterprise.platform.app.service.AppVersionService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "pep.sys.AppVersion")
@CacheDuration(cacheName = "pep.sys.AppVersion")
public class AppVersionServiceImpl implements AppVersionService {

    private static final String CACHE_KEY = "'latestVer'";

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Override
    public AppVersionDocument save(AppVersionDocument appVersion) {
        return appVersionRepository.save(appVersion);
    }

    @Override
    @CachePut(key = CACHE_KEY)
    public AppVersionDocument releaseAPP(AppVersionDocument appVersionDocument) {
        AppVersionDocument app = appVersionRepository.findByVer(appVersionDocument.getVer());
        if (app != null) {
            return app;
        } else {
            return appVersionRepository.save(appVersionDocument);
        }
    }

    @Override
    public AppVersionDocument updateVersionInfo(AppVersionDocument appVersionDocument) {
        AppVersionDocument app = this.getCertainVersion(appVersionDocument.getVer());
        if (app != null) {
            app.setNote(appVersionDocument.getNote());
            app.setVer(appVersionDocument.getVer());
            app.setUrl(appVersionDocument.getUrl());
            app.setLastModifyUserId(appVersionDocument.getLastModifyUserId());
            app.setLastModifyTime(appVersionDocument.getLastModifyTime());
            return appVersionRepository.save(app);
        } else {
            return null;
        }
    }

    @Override
    public AppVersionDocument inValidByVersion(long version) {
        AppVersionDocument app = appVersionRepository.findByVer(version);
        if (app.isValid()) {
            app.setValid(false);
            app = appVersionRepository.save(app);
        }
        return app;
    }

    @Override
    @Cacheable(key = CACHE_KEY)
    public AppVersionDocument getLatestReleaseVersionOnlyValid() {
        AppVersionDocument version = appVersionRepository.findTopByValidTrueOrderByVerDesc();
        return version;
    }

    @Override
    public AppVersionDocument getCertainVersion(long version) {
        return appVersionRepository.findByVer(version);
    }

    @Override
    public DataTrunk<AppVersionDocument> getVersionInfosByPage(Integer pageNo, Integer pageSize) {
        DataTrunk<AppVersionDocument> retObj = new DataTrunk<>();
        Sort sort = new Sort(Sort.Direction.DESC, "ver");
        if (pageNo == null || pageSize == null) {
            retObj.setCount(appVersionRepository.countByValidTrue());
            retObj.setData(appVersionRepository.findAllByValidTrue(sort));
        } else {
            Pageable pageable = new PageRequest(pageNo - 1, pageSize, sort);
            Page<AppVersionDocument> pageApps = appVersionRepository.findAllByValidTrue(pageable);
            retObj.setCount(appVersionRepository.countByValidTrue());
            retObj.setData(pageApps.getContent());
        }
        return retObj;
    }

}
