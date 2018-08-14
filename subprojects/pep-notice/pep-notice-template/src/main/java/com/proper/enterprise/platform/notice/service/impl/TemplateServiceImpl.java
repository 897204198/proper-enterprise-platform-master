package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.entity.TemplateEntity;
import com.proper.enterprise.platform.notice.repository.TemplateRepository;
import com.proper.enterprise.platform.notice.service.TemplateService;
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
    public Map<String, TemplateVO> getTemplates(DataDicLiteBean business, String code, Map<String, String> templateParams) {
        List<TemplateEntity> list = templateRepository.findByCatelogAndCode(business, code);
        Map<String, TemplateVO> result = new HashMap<>(1);
        TemplateVO templateVO;
        for (TemplateEntity templateEntity : list) {
            templateVO = new TemplateVO();
            templateVO.setTitle(templateEntity.getTitle());
            String content = template2Content(templateEntity.getTemplate(), templateParams);
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

    private String template2Content(String template, Map<String, String> templateParams) {
        for (Map.Entry<String, String> entry : templateParams.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }

    @Override
    public TemplateVO save(TemplateVO template) {
        DataDicLiteBean catelog = new DataDicLiteBean("TEMPLATE_CATELOG", template.getCatelog());
        DataDicLiteBean type = new DataDicLiteBean("TEMPLATE_TYPE", template.getType());
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
    public TemplateVO update(String id, TemplateVO template) {
        if (StringUtil.isNotBlank(id)) {
            DataDicLiteBean catelog = new DataDicLiteBean("TEMPLATE_CATELOG", template.getCatelog());
            DataDicLiteBean type = new DataDicLiteBean("TEMPLATE_TYPE", template.getType());
            TemplateEntity templateEntity = BeanUtil.convert(template, TemplateEntity.class);
            templateEntity.setId(id);
            templateEntity.setCatelog(catelog);
            templateEntity.setType(type);
            templateEntity.setEnable(true);
            templateEntity = templateRepository.save(templateEntity);
            template = BeanUtil.convert(templateEntity, TemplateVO.class);
            template.setCatelog(templateEntity.getCatelog().getCode());
            template.setType(templateEntity.getType().getCode());
            return template;
        }
        return null;
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
    public TemplateVO getTemplate(DataDicLiteBean business, String code, DataDicLiteBean type, Map<String, String> templateParams) {
        TemplateEntity templateEntity = templateRepository.findByCatelogAndCodeAndType(business, code, type);
        TemplateVO templateVO = new TemplateVO();
        templateVO.setTitle(templateEntity.getTitle());
        String content = template2Content(templateEntity.getTemplate(), templateParams);
        templateVO.setTemplate(content);
        return templateVO;
    }

    @Override
    public TemplateRepository getRepository() {
        return templateRepository;
    }

}
