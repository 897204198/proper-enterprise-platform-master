package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.notice.vo.TemplateVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;

import java.util.Map;

public interface TemplateService extends BaseJpaService<TemplateVO, String> {

    /**
     * 查询指定业务的模板集合
     *
     * @param business       业务
     * @param code           模板标识
     * @param templateParams 模板参数
     * @return 模板集合
     */
    Map<String, TemplateVO> getTemplates(DataDicLiteBean business, String code, Map<String, String> templateParams);

    /**
     * 获得纯文本类型的小贴士
     *
     * @param code 模板标识
     * @return 小贴士
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
     * @param id       主键
     * @param template 修改的模板
     * @return 已修改的模板
     */
    TemplateVO update(String id, TemplateVO template);

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
     * @param code        标识
     * @param name        名称
     * @param title       标题
     * @param template    模板
     * @param description 解释
     * @return 模板分页信息
     */
    DataTrunk<TemplateVO> findPagination(String code, String name, String title, String template, String description);

}
