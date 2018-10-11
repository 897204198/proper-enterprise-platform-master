package com.proper.enterprise.platform.template.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.template.vo.TemplateVO;

import java.util.Map;

public interface TemplateService {

    /**
     * 查询指定业务的模板集合
     *
     * @param code           关键字
     * @param templateParams 模板参数
     * @return 模板集合
     */
    TemplateVO getTemplate(String code, Map<String, Object> templateParams);

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
     * @param code         关键字
     * @param name         名称
     * @param description  解释
     * @param catalog      类别
     * @param enable       启用停用
     * @param muti         是否多模板
     * @return 模板分页信息
     */
    DataTrunk<TemplateVO> findPagination(String code,
                                         String name,
                                         String description,
                                         String catalog,
                                         String enable,
                                         Boolean muti);
}
