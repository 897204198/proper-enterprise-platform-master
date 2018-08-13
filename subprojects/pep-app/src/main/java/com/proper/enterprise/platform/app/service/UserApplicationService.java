package com.proper.enterprise.platform.app.service;

import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.app.vo.UserApplicationVO;

import java.util.List;

public interface UserApplicationService {

    /**
     * 用户获取的应用集合
     * @return 应用的集合
     */
    List<ApplicationVO> findUserApplications();

    /**
     * 更新用户获取的应用集合
     * @param ids appId
     * @return 应用的集合
     */
    UserApplicationVO saveOrUpdateUserApplications(String ids);

    /**
     * 用户获取的应用，包括类别
     * @return 应用和类别的集合
     */
    List<AppCatalogVO> findCatalogAndApplications();

    /**
     * 获取默认应用
     * @return 应用的集合
     */
    List<ApplicationVO> findDefaultApplication();
}
