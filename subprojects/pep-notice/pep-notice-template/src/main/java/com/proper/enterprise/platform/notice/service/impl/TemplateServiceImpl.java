package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.entity.TemplateEntity;
import com.proper.enterprise.platform.notice.repository.TemplateRepository;
import com.proper.enterprise.platform.notice.service.TemplateService;
import com.proper.enterprise.platform.notice.util.TemplateUtil;
import com.proper.enterprise.platform.notice.vo.TemplateVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemplateServiceImpl extends AbstractJpaServiceSupport<TemplateVO, TemplateRepository, String> implements TemplateService {

    @Autowired
    TemplateRepository templateRepository;

    @Override
    public Map<String, TemplateVO> getTemplates(String code, Map<String, Object> templateParams) {
        List<TemplateEntity> list = templateRepository.findByCode(code);
        Map<String, TemplateVO> result = new HashMap<>(1);
        TemplateVO templateVO;
        for (TemplateEntity templateEntity : list) {
            templateVO = new TemplateVO();
            String title = TemplateUtil.template2Content(templateEntity.getTitle(), templateParams);
            templateVO.setTitle(title);
            String content = TemplateUtil.template2Content(templateEntity.getTemplate(), templateParams);
            templateVO.setTemplate(content);
            result.put(templateEntity.getType().getCode(), templateVO);
        }
        return result;
    }

    @Override
    public String getTips(String code) {
        TemplateEntity templateEntity = templateRepository.findFirstByCode(code);
        return templateEntity.getTemplate();
    }

    @Override
    public TemplateVO save(TemplateVO template) {
        DataDicLiteBean catelog = new DataDicLiteBean("TEMPLATE_CATELOG", template.getCatelog());
        DataDicLiteBean type = new DataDicLiteBean("TEMPLATE_TYPE", template.getType());
        List<TemplateEntity> valid = templateRepository.findByCodeAndType(template.getCode(), type);
        if (valid != null && valid.size() > 0) {
            throw new ErrMsgException("error");
        }
        TemplateEntity templateEntity = BeanUtil.convert(template, TemplateEntity.class);
        templateEntity.setCatelog(catelog);
        templateEntity.setType(type);
        templateEntity.setEnable(true);
        templateEntity = templateRepository.save(templateEntity);
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatelog(templateEntity.getCatelog().getCode());
        template.setType(templateEntity.getType().getCode());
        return template;
    }

    @Override
    public TemplateVO update(TemplateVO template) {
        DataDicLiteBean catelog = new DataDicLiteBean("TEMPLATE_CATELOG", template.getCatelog());
        DataDicLiteBean type = new DataDicLiteBean("TEMPLATE_TYPE", template.getType());
        List<TemplateEntity> valid = templateRepository.findByCodeAndType(template.getCode(), type);
        boolean isExist = valid != null && valid.size() > 0;
        if (isExist && !valid.get(0).getId().equals(template.getId())) {
            throw new ErrMsgException("error");
        }
        TemplateEntity templateEntity = BeanUtil.convert(template, TemplateEntity.class);
        templateEntity.setCatelog(catelog);
        templateEntity.setType(type);
        templateEntity.setEnable(true);
        templateEntity = templateRepository.save(templateEntity);
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatelog(templateEntity.getCatelog().getCode());
        template.setType(templateEntity.getType().getCode());
        return template;
    }


    @Override
    public TemplateVO get(String id) {
        TemplateVO template;
        TemplateEntity templateEntity = templateRepository.findById(id).<ErrMsgException>orElseThrow(() -> {
            throw new ErrMsgException("Could NOT find template entity with " + id);
        });
        template = BeanUtil.convert(templateEntity, TemplateVO.class);
        template.setCatelog(templateEntity.getCatelog().getCode());
        template.setType(templateEntity.getType().getCode());
        return template;
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> list = new ArrayList<>();
            Collections.addAll(list, idArr);
            List<TemplateEntity> collection = templateRepository.findAllById(list);
            templateRepository.deleteAll(collection);
        }
        return true;
    }

    @Override
    public DataTrunk<TemplateVO> findPagination(String code, String name, String title, String template, String description) {
        Page<TemplateEntity> page = templateRepository.findPagination(code, name, title, template, description, this.getPageRequest());
        List<TemplateVO> templateVOS = new ArrayList<>();
        for (TemplateEntity templateEntity : page.getContent()) {
            TemplateVO templateVO = BeanUtil.convert(templateEntity, TemplateVO.class);
            templateVO.setCatelog(templateEntity.getCatelog().getCode());
            templateVO.setType(templateEntity.getType().getCode());
            templateVOS.add(templateVO);
        }
        Page<TemplateVO> pageVO = new PageImpl<>(templateVOS, this.getPageRequest(), page.getTotalElements());
        return new DataTrunk<>(pageVO);
    }

    @Override
    public TemplateVO getTemplate(DataDicLiteBean business, String code, DataDicLiteBean type, Map<String, Object> templateParams) {
        TemplateEntity templateEntity = templateRepository.findByCatelogAndCodeAndType(business, code, type);
        TemplateVO templateVO = new TemplateVO();
        templateVO.setTitle(templateEntity.getTitle());
        String content = TemplateUtil.template2Content(templateEntity.getTemplate(), templateParams);
        templateVO.setTemplate(content);
        return templateVO;
    }

    @Override
    public TemplateVO getTemplateByCode(String code) {
        TemplateVO templateVO = new TemplateVO();
        TemplateEntity templateEntity = templateRepository.findFirstByCode(code);
        templateVO.setCatelog(templateEntity.getCatelog().getCode());
        templateVO.setType(templateEntity.getType().getCode());
        return templateVO;
    }

    @Override
    public TemplateRepository getRepository() {
        return templateRepository;
    }

}
