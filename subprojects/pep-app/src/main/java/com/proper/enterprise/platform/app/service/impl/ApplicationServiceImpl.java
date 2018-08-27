package com.proper.enterprise.platform.app.service.impl;

import com.proper.enterprise.platform.app.entity.AppCatalogEntity;
import com.proper.enterprise.platform.app.entity.ApplicationEntity;
import com.proper.enterprise.platform.app.repository.AppCatalogRepository;
import com.proper.enterprise.platform.app.repository.ApplicationRepository;
import com.proper.enterprise.platform.app.service.ApplicationService;
import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ApplicationServiceImpl extends AbstractJpaServiceSupport<ApplicationVO, ApplicationRepository, String>
        implements ApplicationService {

    private AppCatalogRepository appCatalogRepository;
    private ApplicationRepository applicationRepository;
    private I18NService i18NService;

    @Autowired
    public ApplicationServiceImpl(AppCatalogRepository appCatalogRepository, ApplicationRepository applicationRepository, I18NService i18NService) {
        this.appCatalogRepository = appCatalogRepository;
        this.applicationRepository = applicationRepository;
        this.i18NService = i18NService;
    }

    @Override
    public ApplicationRepository getRepository() {
        return applicationRepository;
    }

    @Override
    public ApplicationVO updateApplication(String appId, ApplicationVO applicationVO) {
        applicationVO.setId(appId);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        BeanUtils.copyProperties(applicationVO, applicationEntity);
        BeanUtils.copyProperties(applicationRepository.updateForSelective(applicationEntity), applicationVO);
        return applicationVO;
    }

    @Override
    public ApplicationVO addApplication(ApplicationVO applicationVO) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        BeanUtils.copyProperties(applicationVO, applicationEntity);
        applicationEntity = applicationRepository.save(applicationEntity);
        applicationVO.setId(applicationEntity.getId());
        return applicationVO;
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            Iterable<ApplicationEntity> list = applicationRepository.findAllById(idList);
            applicationRepository.deleteAll(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public ApplicationVO getApplication(String appId) {
        ApplicationVO applicationVO = null;
        ApplicationEntity applicationEntity = applicationRepository.findById(appId).orElse(null);
        if (applicationEntity != null) {
            applicationVO = new ApplicationVO();
            BeanUtils.copyProperties(applicationEntity, applicationVO);
        }
        return applicationVO;
    }

    @Override
    public List<ApplicationVO> getApplicationByCode(String code) {
        List<ApplicationEntity> applicationEntities = applicationRepository.findAllByCode(code);
        List<ApplicationVO> applicationVOS = new ArrayList<>();
        for (ApplicationEntity applicationEntity : applicationEntities) {
            ApplicationVO application = new ApplicationVO();
            BeanUtils.copyProperties(applicationEntity, application);
            applicationVOS.add(application);
        }
        return applicationVOS;
    }

    @Override
    public DataTrunk<ApplicationVO> findPagination(String code, String applicationName, String applicationPage) {
        Page<ApplicationEntity> page = applicationRepository.findPagination(code, applicationName, applicationPage, this.getPageRequest());
        List<ApplicationVO> applicationVOS = new ArrayList<>();
        for (ApplicationEntity applicationEntity : page.getContent()) {
            ApplicationVO application = new ApplicationVO();
            BeanUtils.copyProperties(applicationEntity, application);
            applicationVOS.add(application);
        }
        Page<ApplicationVO> pageVO = new PageImpl<>(applicationVOS, this.getPageRequest(), page.getTotalElements());
        return new DataTrunk<>(pageVO);
    }

    @Override
    public List<ApplicationVO> getAllOrApplication(String code) {
        if (code != null) {
            return getApplicationByCode(code);
        } else {
            return getApplications();
        }
    }

    @Override
    public List<AppCatalogVO> getCatalogs() {
        List<AppCatalogEntity> appCatalogEntities = appCatalogRepository.findAll(new Sort(Sort.Direction.DESC, "createTime"));
        List<AppCatalogVO> list = new ArrayList<>();
        for (AppCatalogEntity catalogEntity : appCatalogEntities) {
            AppCatalogVO appCatalogVO = new AppCatalogVO();
            BeanUtils.copyProperties(catalogEntity, appCatalogVO);
            list.add(appCatalogVO);
        }
        return list;
    }

    @Override
    public AppCatalogVO updateCatalog(String typeCode, String typeName) {
        AppCatalogEntity appCatalogEntity = appCatalogRepository.findByCode(typeCode);
        appCatalogEntity.setTypeName(typeName);
        appCatalogEntity = appCatalogRepository.save(appCatalogEntity);
        AppCatalogVO appCatalogVO = new AppCatalogVO();
        BeanUtils.copyProperties(appCatalogEntity, appCatalogVO);
        return appCatalogVO;
    }

    @Override
    public AppCatalogVO addCatalog(AppCatalogVO appCatalogVO) {
        AppCatalogEntity catalogEntity = new AppCatalogEntity();
        BeanUtils.copyProperties(appCatalogVO, catalogEntity);
        catalogEntity = appCatalogRepository.save(catalogEntity);
        appCatalogVO.setId(catalogEntity.getId());
        return appCatalogVO;
    }

    @Override
    public AppCatalogVO getCatalog(String code) {
        AppCatalogEntity catalogEntity = appCatalogRepository.findByCode(code);
        AppCatalogVO appCatalogVO = new AppCatalogVO();
        BeanUtils.copyProperties(catalogEntity, appCatalogVO);
        return appCatalogVO;
    }

    @Override
    public void deleteByCode(String code) {
        List<ApplicationEntity> list = applicationRepository.findAllByCode(code);
        if (CollectionUtil.isEmpty(list)) {
            appCatalogRepository.deleteByCode(code);
        } else {
            throw new ErrMsgException(i18NService.getMessage("pep.app.application.delete.catalog"));
        }
    }

    @Override
    public List<ApplicationVO> getApplications() {
        List<ApplicationEntity> applicationEntities = applicationRepository.findAll();
        List<ApplicationVO> applicationVOS = new ArrayList<>();
        for (ApplicationEntity applicationEntity : applicationEntities) {
            ApplicationVO applicationVO = new ApplicationVO();
            BeanUtils.copyProperties(applicationEntity, applicationVO);
            applicationVOS.add(applicationVO);
        }
        return applicationVOS;
    }
}
