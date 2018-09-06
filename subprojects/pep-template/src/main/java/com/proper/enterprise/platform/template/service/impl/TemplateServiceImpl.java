package com.proper.enterprise.platform.template.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import com.proper.enterprise.platform.template.entity.TemplateEntity;
import com.proper.enterprise.platform.template.repository.TemplateRepository;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.util.TemplateUtil;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemplateServiceImpl extends AbstractJpaServiceSupport<TemplateVO, TemplateRepository, String> implements
    TemplateService {

    private TemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Map<String, TemplateVO> getTemplates(String code) {
        List<TemplateEntity> templateEntitys = templateRepository.findByCode(code);
        Map<String, TemplateVO> templates = new HashMap<>(1);
        for (TemplateEntity template : templateEntitys) {
            TemplateVO templateVO = BeanUtil.convert(template, TemplateVO.class);
            templates.put(template.getType().getCode(), templateVO);
        }
        return templates;
    }

    @Override
    public Map<String, TemplateVO> getTemplates(String code, Map<String, Object> templateParams) {
        List<TemplateEntity> templateEntitys = templateRepository.findByCode(code);
        return convertListToMap(templateEntitys, templateParams);
    }

    @Override
    public Map<String, TemplateVO> getTemplatesByCodeAndTypesWithinCatalog(String code, List<DataDicLiteBean>
        noticeTypes, Map<String, Object> templateParams) {
        List<TemplateEntity> templateEntitys = templateRepository.findByCodeAndTypeInAndCatalogIsNotNull(code,
            noticeTypes);
        return convertListToMap(templateEntitys, templateParams);
    }

    private Map<String, TemplateVO> convertListToMap(List<TemplateEntity> templateEntitys, Map<String, Object>
        templateParams) {
        if (templateEntitys == null || templateEntitys.size() == 0) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.no.templates"));
        }
        Map<String, TemplateVO> templates = new HashMap<>(1);
        for (TemplateEntity template : templateEntitys) {
            TemplateVO templateVO = BeanUtil.convert(template, TemplateVO.class);
            templateVO.setCatalog(template.getCatalog().getCode());
            templateVO.setTitle(TemplateUtil.template2Content(template.getTitle(), templateParams));
            templateVO.setTemplate(TemplateUtil.template2Content(template.getTemplate(), templateParams));
            templates.put(template.getType().getCode(), templateVO);
        }
        return templates;
    }

    @Override
    public String getTips(String code) {
        List<TemplateEntity> templateEntitys = templateRepository.findByCode(code);
        if (templateEntitys != null && templateEntitys.size() > 0) {
            return templateEntitys.get(0).getTemplate();
        }
        return "";
    }

    @Override
    public TemplateVO save(TemplateVO template) {
        TemplateEntity valid = templateRepository.findByCodeAndType(template.getCode(), template.getType());
        if (valid != null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.repeat"));
        }
        return saveOrUpdate(template);
    }

    @Override
    public TemplateVO update(TemplateVO template) {
        TemplateEntity valid = templateRepository.findByCodeAndType(template.getCode(), template.getType());
        boolean isExist = valid != null;
        if (isExist && !valid.getId().equals(template.getId())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.repeat"));
        }
        return saveOrUpdate(template);
    }

    private TemplateVO saveOrUpdate(TemplateVO template) {
        DataDicLiteBean catalog = new DataDicLiteBean("TEMPLATE_CATALOG", template.getCatalog());
        TemplateEntity templateEntity = BeanUtil.convert(template, TemplateEntity.class);
        templateEntity.setCatalog(catalog);
        templateEntity.setType(template.getType());
        templateEntity.setEnable(true);
        templateEntity = templateRepository.save(templateEntity);
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatalog(templateEntity.getCatalog().getCode());
        template.setType((DataDicLiteBean) templateEntity.getType());
        return template;
    }

    @Override
    public TemplateVO get(String id) {
        TemplateVO template;
        TemplateEntity templateEntity = templateRepository.findOne(id);
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatalog(templateEntity.getCatalog().getCode());
        return template;
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> list = new ArrayList<>();
            Collections.addAll(list, idArr);
            List<TemplateEntity> collection = templateRepository.findAll(list);
            templateRepository.delete(collection);
        }
        return true;
    }

    @Override
    public DataTrunk<TemplateVO> findPagination(String code, String name, String title, String template, String
        description) {
        Page<TemplateEntity> page = templateRepository.findPagination(code, name, title, template, description, this
            .getPageRequest());
        List<TemplateVO> templateVOS = new ArrayList<>();
        for (TemplateEntity templateEntity : page.getContent()) {
            TemplateVO templateVO = BeanUtil.convert(templateEntity, TemplateVO.class);
            templateVO.setCatalog(templateEntity.getCatalog().getCode());
            templateVOS.add(templateVO);
        }
        Page<TemplateVO> pageVO = new PageImpl<>(templateVOS, this.getPageRequest(), page.getTotalElements());
        return new DataTrunk<>(pageVO);
    }

    @Override
    public TemplateRepository getRepository() {
        return templateRepository;
    }

}
