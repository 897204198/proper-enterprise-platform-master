package com.proper.enterprise.platform.template.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.template.entity.TemplateEntity;
import com.proper.enterprise.platform.template.repository.TemplateRepository;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.util.TemplateUtil;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemplateServiceImpl extends AbstractJpaServiceSupport<TemplateVO, TemplateRepository, String> implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public TemplateVO getTemplates(String code, Map<String, Object> templateParams) {
        TemplateEntity templateEntity = templateRepository.findByCode(code);
        TemplateVO templateVO = new TemplateVO();
        getTemplateTitleAndContent(templateParams, templateEntity, templateVO);
        return templateVO;
    }

    private void getTemplateTitleAndContent(Map<String, Object> templateParams, TemplateEntity templateEntity, TemplateVO templateVO) {
        String title;
        String content;
        if (StringUtil.isNotNull(templateEntity.getEmailTitle()) && StringUtil.isNotNull(templateEntity.getEmailTemplate())) {
            title = TemplateUtil.template2Content(templateEntity.getEmailTitle(), templateParams);
            templateVO.setEmailTitle(title);
            content = TemplateUtil.template2Content(templateEntity.getEmailTemplate(), templateParams);
            templateVO.setEmailTemplate(content);
        }
        if (StringUtil.isNotNull(templateEntity.getPushTitle()) && StringUtil.isNotNull(templateEntity.getPushTemplate())) {
            title = TemplateUtil.template2Content(templateEntity.getPushTitle(), templateParams);
            templateVO.setPushTitle(title);
            content = TemplateUtil.template2Content(templateEntity.getPushTemplate(), templateParams);
            templateVO.setPushTemplate(content);
        }
        if (StringUtil.isNotNull(templateEntity.getSmsTitle()) && StringUtil.isNotNull(templateEntity.getSmsTemplate())) {
            title = TemplateUtil.template2Content(templateEntity.getSmsTitle(), templateParams);
            templateVO.setSmsTitle(title);
            content = TemplateUtil.template2Content(templateEntity.getSmsTemplate(), templateParams);
            templateVO.setSmsTemplate(content);
        }
    }

    @Override
    public TemplateVO getTips(String code) {
        TemplateEntity templateEntity = templateRepository.findByCode(code);
        TemplateVO templateVO = BeanUtil.convert(templateEntity, TemplateVO.class);
        return templateVO;
    }

    @Override
    public TemplateVO save(TemplateVO template) {
        DataDicLiteBean catelog = new DataDicLiteBean("TEMPLATE_CATELOG", template.getCatelog());
        TemplateEntity valid = templateRepository.findByCode(template.getCode());
        if (valid != null) {
            throw new ErrMsgException("error");
        }
        TemplateEntity templateEntity = BeanUtil.convert(template, TemplateEntity.class);
        templateEntity.setCatelog(catelog);
        templateEntity.setEnable(true);
        templateEntity = templateRepository.save(templateEntity);
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatelog(templateEntity.getCatelog().getCode());
        return template;
    }

    @Override
    public TemplateVO update(TemplateVO template) {
        DataDicLiteBean catelog = new DataDicLiteBean("TEMPLATE_CATELOG", template.getCatelog());
        TemplateEntity valid = templateRepository.findByCode(template.getCode());
        boolean isExist = valid != null;
        if (isExist && !valid.getId().equals(template.getId())) {
            throw new ErrMsgException("error");
        }
        TemplateEntity templateEntity = BeanUtil.convert(template, TemplateEntity.class);
        templateEntity.setCatelog(catelog);
        templateEntity.setEnable(true);
        templateEntity = templateRepository.save(templateEntity);
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatelog(templateEntity.getCatelog().getCode());
        return template;
    }


    @Override
    public TemplateVO get(String id) {
        TemplateVO template;
        TemplateEntity templateEntity = templateRepository.findOne(id);
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatelog(templateEntity.getCatelog().getCode());
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
        description, String type) {
        Page<TemplateEntity> page = templateRepository.findPagination(code, name, title, template, description, this.getPageRequest());
        List<TemplateVO> templateVOS = new ArrayList<>();
        for (TemplateEntity templateEntity : page.getContent()) {
            TemplateVO templateVO = BeanUtil.convert(templateEntity, TemplateVO.class);
            templateVO.setCatelog(templateEntity.getCatelog().getCode());
            templateVOS.add(templateVO);
        }
        Page<TemplateVO> pageVO = new PageImpl<>(templateVOS, this.getPageRequest(), page.getTotalElements());
        return new DataTrunk<>(pageVO);
    }

    @Override
    public TemplateVO getTemplateByCode(String code) {
        TemplateVO templateVO = new TemplateVO();
        TemplateEntity templateEntity = templateRepository.findByCode(code);
        templateVO.setCatelog(templateEntity.getCatelog().getCode());
        return templateVO;
    }

    @Override
    public TemplateRepository getRepository() {
        return templateRepository;
    }

}
