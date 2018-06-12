package com.proper.enterprise.platform.app.service;

import com.proper.enterprise.platform.app.document.AppVersionDocument;

import java.util.List;

public interface AppVersionService {

    AppVersionDocument saveOrUpdate(AppVersionDocument appVersion);

    /**
     * 保存并发布版本
     *
     * @param appVersionDocument 版本信息
     */
    AppVersionDocument release(AppVersionDocument appVersionDocument);

    /**
     * 删除一个版本
     *
     * @param version 版本号
     */
    void delete(AppVersionDocument version);

    /**
     * 获取最新的有效发布版本
     */
    AppVersionDocument getLatestRelease();

    /**
     * 根据版本号，获取对应版本信息，并忽略是否为有效版本
     *
     * @param  version 版本号
     * @return 版本信息
     */
    AppVersionDocument get(String version);

    List<AppVersionDocument> list();

}
