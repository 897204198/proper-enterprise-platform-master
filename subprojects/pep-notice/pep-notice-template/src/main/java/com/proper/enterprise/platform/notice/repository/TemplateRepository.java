package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.notice.entity.TemplateEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateRepository extends BaseJpaRepository<TemplateEntity, String> {

    /**
     * 查询指定业务的模板列表
     *
     * @param catelog 业务
     * @param code    模板标识
     * @return 模板列表
     */
    List<TemplateEntity> findByCatelogAndCode(DataDicLiteBean catelog, String code);

    /**
     * 查询指定的模板
     *
     * @param code 模板标识
     * @return 模板
     */
    TemplateEntity findFirstByCode(String code);

    /**
     * 分页
     *
     * @param code        标识
     * @param name        name
     * @param title       标题
     * @param template    模板
     * @param description 解释
     * @param pageable    分页信息
     * @return 分页
     */
    @Query("select t from TemplateEntity t where t.code like %?1% "
                                        + "and t.name like %?2% "
                                        + "and t.title like %?3% "
                                        + "and t.template like %?4% "
                                        + "and t.description like %?5% ")
    Page<TemplateEntity> findPagination(String code,
                                        String name,
                                        String title,
                                        String template,
                                        String description,
                                        Pageable pageable);

    /**
     * 获取模板
     * @param business 业务
     * @param code 标识
     * @param type 类型
     * @return 模板
     */
    TemplateEntity findByCatelogAndCodeAndType(DataDicLiteBean business, String code, DataDicLiteBean type);
}
