package com.proper.enterprise.platform.app.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.app.entity.AppCatalogEntity;
import com.proper.enterprise.platform.app.entity.ApplicationEntity;
import com.proper.enterprise.platform.app.repository.AppCatalogRepository;
import com.proper.enterprise.platform.app.repository.ApplicationRepository;
import com.proper.enterprise.platform.app.service.ApplicationService;
import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);


    @Autowired
    AppCatalogRepository appCatalogRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Override
    public ApplicationVO updateApplication(String appId, ApplicationVO applicationVO) {
        ApplicationEntity applicationDoc = applicationRepository.findById(appId).<ErrMsgException>orElseThrow(() -> {
            throw new ErrMsgException("Could not find application with " + appId);
        });
        BeanUtils.copyProperties(applicationDoc, applicationVO);
        applicationDoc = applicationRepository.save(applicationDoc);
        applicationVO.setId(applicationDoc.getId());
        applicationVO.setCode(applicationDoc.getCode());
        applicationVO.setName(applicationDoc.getName());
        applicationVO.setPage(applicationDoc.getPage());
        applicationVO.setStyle(applicationDoc.getStyle());
        applicationVO.setDefaultValue(applicationVO.getDefaultValue());
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
        ApplicationVO applicationVO = new ApplicationVO();
        ApplicationEntity applicationEntity = applicationRepository.findById(appId).<ErrMsgException>orElseThrow(() -> {
            throw new ErrMsgException("Could not find application by " + appId);
        });
        BeanUtils.copyProperties(applicationEntity, applicationVO);
        String data = applicationEntity.getData();
        Map<String, String> map = getDataMap(data);
        applicationVO.setData(map);
        return applicationVO;
    }

    @Override
    public List<ApplicationVO> getApplicationByCode(String code) {
        List<ApplicationEntity> applicationEntities = applicationRepository.findAllByCode(code);
        List<ApplicationVO> applicationVOS = new ArrayList<>();
        for (ApplicationEntity applicationEntity : applicationEntities) {
            ApplicationVO application = new ApplicationVO();
            BeanUtils.copyProperties(applicationEntity, application);
            String data = applicationEntity.getData();
            Map<String, String> map = getDataMap(data);
            application.setData(map);
            applicationVOS.add(application);
        }
        return applicationVOS;
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
        appCatalogRepository.deleteByCode(code);
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

    @Override
    public Map<String, String> getDataMap(String data) {
        Map<String, String> map = new HashMap<>(16);
        if (StringUtil.isNotBlank(data)) {
            try {
                JsonNode node = JSONUtil.parse(data, JsonNode.class);
                Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
                while (iterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = iterator.next();
                    map.put(entry.getKey(), entry.getValue().textValue());
                }
            } catch (IOException e) {
                LOGGER.warn("Data of json parse exception! data is " + data, e);
            }
        }
        return map;
    }

}
