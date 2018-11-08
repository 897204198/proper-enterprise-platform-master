package com.proper.enterprise.platform.app.service;

import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;

import java.util.List;

public interface ApplicationService extends BaseJpaService<ApplicationVO, String> {
    /**
     * 更新应用
     *
     * @param appId         应用id
     * @param applicationVO 应用的信息
     * @return 更新后的应用信息
     */
    ApplicationVO updateApplication(String appId, ApplicationVO applicationVO);

    /**
     * 保存应用
     *
     * @param applicationVO 应用的信息
     * @return 新增的应用信息
     */
    ApplicationVO addApplication(ApplicationVO applicationVO);

    /**
     * 删除应用
     *
     * @param ids 应用Id列表(使用","分割)
     * @return boolean
     */
    boolean deleteByIds(String ids);

    /**
     * 根据appId获取应用信息
     *
     * @param appId 应用的id
     * @return 应用的信息
     */
    ApplicationVO getApplication(String appId);

    /**
     * 根据code获取应用信息
     *
     * @param code 应用的code
     * @return 应用的信息
     */
    List<ApplicationVO> getApplicationByCode(String code);

    /**
     * 根据code获取应用信息
     *
     * @param code            应用的code
     * @param applicationName 应用名
     * @param applicationPage 应用页
     * @return 应用的信息
     */
    DataTrunk<ApplicationVO> findPagination(String code, String applicationName, String applicationPage);

    /**
     * 获取所有应用类别
     *
     * @return 应用类别的集合
     */
    List<AppCatalogVO> getCatalogs();

    /**
     * 更新类别
     *
     * @param code     类别code
     * @param typeName 类别名称
     * @return 类别信息
     */
    AppCatalogVO updateCatalog(String code, String typeName);

    /**
     * 添加类别信息
     *
     * @param appCatalogVO 类别信息
     * @return 类别
     */
    AppCatalogVO addCatalog(AppCatalogVO appCatalogVO);

    /**
     * 根据code 获取类别信息
     *
     * @param code 类别编码
     * @return 对应的类别信息
     */
    AppCatalogVO getCatalog(String code);

    /**
     * 根据code 删除类别信息
     *
     * @param code 类别编码
     * @return boolean
     */
    void deleteByCode(String code);

    /**
     * 获取所有应用
     *
     * @return 所有应用信息
     */
    List<ApplicationVO> getApplications();

    /**
     * 获取应用（传参数根据参数取应用，不传获取所有）
     *
     * @param code 应用编码
     * @return 应用集合
     */
    List<ApplicationVO> getAllOrApplication(String code);

}
