package com.proper.enterprise.platform.dev.tools.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.dev.tools.document.AppVersionDocument;

public interface AppVersionService {

    AppVersionDocument save(AppVersionDocument appVersion);

    /**
     * 手动调用指定的版本为发布版, 如果指定的版本，库里有则直接改成发布版，如果没有，则先添加，然后改成发布版
     *
     * @author sunshuai
     */
    AppVersionDocument releaseAPP(AppVersionDocument appVersionDocument);

    /**
     * 更新版本信息
     *
     * @author sunshuai
     */
    AppVersionDocument updateVersionInfo(AppVersionDocument appVersionDocument);

    /**
     * 根据版本号设置版本为无效版
     *
     * @author sunshuai
     */
    AppVersionDocument inValidByVersion(long version);

    /**
     * 获取最近的发布版信息, 只包含有效版本
     *
     * @author sunshuai
     */
    AppVersionDocument getLatestReleaseVersionOnlyValid();

    /**
     * 根据版本号，获取对应版本信息，并忽略是否为有效版本
     *
     * @author sunshuai
     */
    AppVersionDocument getCertainVersion(long version);

    /**
     * 根据当前页数(pageNo)，每页显示数量(pageSize)，获取范围内的版本信息列表
     *
     * @author sunshuai
     */
    DataTrunk<AppVersionDocument> getVersionInfosByPage(Integer pageNo, Integer pageSize);

}
