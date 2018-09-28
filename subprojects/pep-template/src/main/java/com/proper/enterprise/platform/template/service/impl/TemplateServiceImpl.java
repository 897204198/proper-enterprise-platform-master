package com.proper.enterprise.platform.template.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import com.proper.enterprise.platform.template.entity.TemplateEntity;
import com.proper.enterprise.platform.template.repository.TemplateRepository;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.util.TemplateUtil;
import com.proper.enterprise.platform.template.vo.TemplateDetailVO;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemplateServiceImpl implements TemplateService {

    private TemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public TemplateVO getTemplate(String code, Map<String, Object> templateParams) {
        TemplateEntity templateDocument = templateRepository.findByCode(code);
        if (templateDocument == null
            || !templateDocument.getEnable()
            || templateDocument.getDetails() == null
            || templateDocument.getDetails().size() == 0) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.no.templates"));
        }
        List<TemplateDetailVO> details = templateDocument.getDetails();
        TemplateVO templateVO = BeanUtil.convert(templateDocument, TemplateVO.class);
        if (templateVO.getMuti()) {
            for (TemplateDetailVO template : details) {
                template.setTitle(TemplateUtil.template2Content(template.getTitle(), templateParams));
                template.setTemplate(TemplateUtil.template2Content(template.getTemplate(), templateParams));
            }
            templateVO.setDetails(details);
        } else {
            TemplateDetailVO template = details.get(0);
            template.setTitle(TemplateUtil.template2Content(template.getTitle(), templateParams));
            template.setTemplate(TemplateUtil.template2Content(template.getTemplate(), templateParams));
            templateVO.setDetail(template);
        }
        return templateVO;
    }

    @Override
    public String getTips(String code) {
        TemplateEntity templateDocument = templateRepository.findByCode(code);
        if (templateDocument == null || !templateDocument.getEnable()) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.no.templates"));
        }
        List<TemplateDetailVO> details = templateDocument.getDetails();
        if (details != null && details.size() > 0) {
            return details.get(0).getTemplate();
        }
        return "";
    }

    @Override
    public TemplateVO save(TemplateVO template) {
        if (StringUtil.isNull(template.getCode())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.vo.code.not.null"));
        }
        TemplateEntity valid = templateRepository.findByCode(template.getCode());
        if (valid != null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.repeat"));
        }
        return this.saveOrUpdate(template);
    }

    @Override
    public TemplateVO update(TemplateVO template) {
        if (StringUtil.isNull(template.getCode())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.vo.code.not.null"));
        }
        TemplateEntity valid = templateRepository.findByCode(template.getCode());
        if (valid != null && !valid.getId().equals(template.getId())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.repeat"));
        }
        return this.saveOrUpdate(template);
    }

    private TemplateVO saveOrUpdate(TemplateVO template) {
        TemplateEntity templateDocument = BeanUtil.convert(template, TemplateEntity.class);
        if (!template.getMuti()) {
            templateDocument.setDetails(template.getDetail());
        }
        templateDocument = templateRepository.save(templateDocument);
        template = BeanUtil.convert(templateDocument, TemplateVO.class);
        if (!template.getMuti() && templateDocument.getDetails() != null && templateDocument.getDetails().size() > 0) {
            template.setDetail(templateDocument.getDetails().get(0));
        }
        return template;
    }

    @Override
    public TemplateVO get(String id) {
        TemplateEntity templateDocument = templateRepository.findOne(id);
        if (templateDocument == null || !templateDocument.getEnable()) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.template.no.templates"));
        }
        TemplateVO templateVO = BeanUtil.convert(templateDocument, TemplateVO.class);
        if (!templateVO.getMuti()
            && templateDocument.getDetails() != null
            && templateDocument.getDetails().size() > 0) {
            templateVO.setDetail(templateDocument.getDetails().get(0));
        }
        return templateVO;
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> list = new ArrayList<>();
            Collections.addAll(list, idArr);
            Iterable<TemplateEntity> collection = templateRepository.findAll(list);
            templateRepository.delete(collection);
        }
        return true;
    }

    @Override
    public DataTrunk<TemplateVO> findPagination(String query, PageRequest pageRequest) {
        Page<TemplateEntity> page = templateRepository.findPagination(query, pageRequest);
        List<TemplateEntity> list = page.getContent();
        List<TemplateVO> voList = (List<TemplateVO>) BeanUtil.convert(list, TemplateVO.class);
        DataTrunk<TemplateVO> result = new DataTrunk<>();
        result.setCount(voList.size());
        result.setData(voList);
        return result;
    }

    @Override
    public List<TemplateVO> findAll() {
        List<TemplateEntity> list = templateRepository.findByEnableTrue();
        Collection<TemplateVO> result = BeanUtil.convert(list, TemplateVO.class);
        return (List<TemplateVO>) result;
    }

}
