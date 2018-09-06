package com.proper.enterprise.platform.template.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.template.vo.TemplateVO;

import java.util.List;
import java.util.Map;

public interface TemplateService extends BaseJpaService<TemplateVO, String> {

    /**
     * 查询指定业务的模板集合
     *
     * @param code           关键字
     * @param templateParams 模板参数
     * @return 模板集合
     */
    Map<String, TemplateVO> getTemplates(String code, Map<String, Object> templateParams);

    /**
     * 查询指定业务的模板集合
     *
     * @param code 关键字
     * @return 模板集合
     */
    Map<String, TemplateVO> getTemplates(String code);

    /**
     * 查询指定类型的模板集合
     *
     * @param code           关键字
     * @param noticeTypes    类型
     * @param templateParams 模板参数
     * @return 模板集合
     */
    Map<String, TemplateVO> getTemplatesByCodeAndTypesWithinCatalog(String code, List<DataDicLiteBean> noticeTypes,
                                                                    Map<String, Object> templateParams);

    /**
     * 获得纯文本类型的正文
     *
     * @param code 关键字
     * @return 正文
     */
    String getTips(String code);

    /**
     * 保存模板
     *
     * @param template 保存的模板
     * @return 已保存的模板
     */
    TemplateVO save(TemplateVO template);

    /**
     * 修改模板
     *
     * @param template 修改的模板
     * @return 已修改的模板
     */
    TemplateVO update(TemplateVO template);

    /**
     * 查询指定模板
     *
     * @param id 主键
     * @return 模板
     */
    TemplateVO get(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return 成功
     */
    boolean deleteByIds(String ids);

    /**
     * 获得模板分页信息
     *
     * @param code        关键字
     * @param name        名称
     * @param title       标题
     * @param template    模板
     * @param description 解释
     * @return 模板分页信息
     */
    DataTrunk<TemplateVO> findPagination(String code, String name, String title, String template, String description);

}
