package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.workflow.model.PEPProperty;
import com.proper.enterprise.platform.workflow.vo.PEPModelVO;
import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO;
import com.proper.enterprise.platform.workflow.service.PEPModelService;
import com.proper.enterprise.platform.workflow.vo.PEPPropertyVO;
import org.apache.commons.lang3.StringUtils;

import org.flowable.engine.FormService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PEPModelServiceImpl implements PEPModelService {

    private ModelRepository modelRepository;

    private RepositoryService repositoryService;

    private DataDicService dataDicService;

    private FormService formService;

    @Autowired
    PEPModelServiceImpl(ModelRepository modelRepository,
                        RepositoryService repositoryService,
                        DataDicService dataDicService,
                        FormService formService) {
        this.modelRepository = modelRepository;
        this.repositoryService = repositoryService;
        this.dataDicService = dataDicService;
        this.formService = formService;
    }

    @Override
    public ResultListDataRepresentation getModels(String filter, String sort, Integer modelType, PEPModelVO.ModelStatus modelStatus) {
        if (StringUtils.isEmpty(filter)) {
            filter = null;
        }
        if (StringUtils.isNotEmpty(filter)) {
            filter = filter.toLowerCase();
        }
        List<Model> list = modelRepository.findByModelTypeAndFilter(modelType, filter, sort);
        Set<String> modelKeys = new HashSet<>();
        List<PEPModelVO> pepModelVOS = new ArrayList<>();
        for (Model model : list) {
            pepModelVOS.add(new PEPModelVO(model));
            modelKeys.add(model.getKey());
        }
        List<PEPModelVO> returnData = packageModelAndProcess(pepModelVOS, getProcessDefinitionsByKey(modelKeys), modelStatus);
        ResultListDataRepresentation resultListDataRepresentation = new ResultListDataRepresentation();
        resultListDataRepresentation.setData(returnData);
        resultListDataRepresentation.setSize(returnData.size());
        resultListDataRepresentation.setStart(0);
        resultListDataRepresentation.setTotal((long) returnData.size());
        return resultListDataRepresentation;
    }

    @Override
    public PEPModelVO update(PEPModelVO pepModelVO) {
        Model targetModel = modelRepository.get(pepModelVO.getId());
        Model updateModel = updateModelEditJson(pepModelVO.convert(), targetModel);
        BeanUtil.copyProperties(updateModel, targetModel);
        targetModel.setLastUpdated(new Date());
        targetModel.setLastUpdatedBy(Authentication.getCurrentUserId());
        modelRepository.save(targetModel);
        return pepModelVO;
    }

    private Model updateModelEditJson(Model model, Model oldModel) {
        String oldModelEditJson = oldModel.getModelEditorJson();
        if (StringUtil.isEmpty(oldModelEditJson)) {
            return model;
        }
        model.setModelEditorJson(oldModelEditJson
            .replaceAll("\"process_id\":\"" + oldModel.getKey() + "\"",
                "\"process_id\":\"" + (StringUtil.isEmpty(model.getKey())
                    ? oldModel.getKey()
                    : model.getKey()) + "\"")
            .replaceAll("\"name\":\"" + oldModel.getName() + "\"",
                "\"name\":\"" + (StringUtil.isEmpty(model.getName())
                    ? oldModel.getName()
                    : model.getName()) + "\"")
            .replaceAll("\"documentation\":\"" + oldModel.getDescription() + "\"",
                "\"documentation\":\"" + (StringUtil.isEmpty(model.getDescription())
                    ? oldModel.getDescription()
                    : model.getDescription()) + "\"")
        );
        return model;
    }

    private List<PEPModelVO> packageModelAndProcess(List<PEPModelVO> pepModelVOS,
                                                    Map<String, PEPProcessDefinitionVO> pepProcessDefinitionMap,
                                                    PEPModelVO.ModelStatus modelStatus) {
        List<PEPModelVO> newPEPModelVOs = new ArrayList<>();
        for (PEPModelVO pepModelVO : pepModelVOS) {
            PEPProcessDefinitionVO pepProcessDefinitionVO = pepProcessDefinitionMap.get(pepModelVO.getKey());
            if (null != pepProcessDefinitionVO) {
                pepModelVO.setProcessVersion(pepProcessDefinitionVO.getVersion());
                pepModelVO.setDeploymentTime(pepProcessDefinitionVO.getDeploymentTime());
                pepModelVO.setStatus(buildModelStatus(pepModelVO, pepProcessDefinitionVO));
                if (modelStatus == PEPModelVO.ModelStatus.DEPLOYED) {
                    pepModelVO.setStatus(dataDicService.get(PEPModelVO.ModelStatus.DEPLOYED));
                }
                pepModelVO.setStartFormKey(pepProcessDefinitionVO.getStartFormKey());
                List<FormProperty> formProperties = formService.getStartFormData(pepProcessDefinitionVO.getId())
                    .getFormProperties();
                if (CollectionUtil.isNotEmpty(formProperties)) {
                    Map<String, PEPPropertyVO> pepPropertyMap = new HashMap<>(16);
                    for (FormProperty formProperty : formProperties) {
                        pepPropertyMap.put(formProperty.getId(), BeanUtil.convert(new PEPProperty(formProperty),
                            PEPPropertyVO.class));
                    }
                    pepModelVO.setFormProperties(CollectionUtil.isEmpty(pepPropertyMap) ? null : pepPropertyMap);
                }
                newPEPModelVOs.add(pepModelVO);
                continue;
            }
            if (modelStatus == PEPModelVO.ModelStatus.DEPLOYED) {
                continue;
            }
            pepModelVO.setStatus(dataDicService.get(PEPModelVO.ModelStatus.UN_DEPLOYED));
            newPEPModelVOs.add(pepModelVO);
        }
        return newPEPModelVOs;
    }

    private DataDicLite buildModelStatus(PEPModelVO pepModelVO, PEPProcessDefinitionVO pepProcessDefinitionVO) {
        if (pepModelVO.getLastUpdated().getTime() > pepProcessDefinitionVO.getDeploymentTime().getTime()) {
            return dataDicService.get(PEPModelVO.ModelStatus.UN_DEPLOYED);
        }
        return dataDicService.get(PEPModelVO.ModelStatus.DEPLOYED);
    }

    private Map<String, PEPProcessDefinitionVO> getProcessDefinitionsByKey(Set<String> modelKeys) {
        Map<String, PEPProcessDefinitionVO> returnMap = new HashMap<>(16);
        Set<String> deploymentIds = new HashSet<>();
        List<ProcessDefinition> processDefinitions = repositoryService
            .createNativeProcessDefinitionQuery()
            .sql("SELECT * FROM ACT_RE_PROCDEF a inner join ("
                + "SELECT c.KEY_,max(c.VERSION_) as VERSION_ FROM ACT_RE_PROCDEF c WHERE "
                + getInSql("c.KEY_", "KEYS", modelKeys.size()) + " GROUP BY c.KEY_ \n"
                + ") b on a.KEY_=b.KEY_ and a.VERSION_ =b.VERSION_").parameter("KEYS", modelKeys.toArray()).list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            PEPProcessDefinitionVO pepProcessDefinitionVO = new PEPProcessDefinitionVO(processDefinition);
            String startFormKey = formService.getStartFormKey(processDefinition.getId());
            pepProcessDefinitionVO.setStartFormKey(startFormKey);
            returnMap.put(pepProcessDefinitionVO.getKey(), pepProcessDefinitionVO);
            deploymentIds.add(pepProcessDefinitionVO.getDeploymentId());
        }
        return packageProcessAndDeploy(returnMap, getDeploymentByIds(deploymentIds));
    }

    private Map<String, PEPProcessDefinitionVO> packageProcessAndDeploy(Map<String, PEPProcessDefinitionVO> pepProcessDefinitionMap,
                                                                        Map<String, Deployment> deploymentMap) {
        for (Map.Entry<String, PEPProcessDefinitionVO> entry : pepProcessDefinitionMap.entrySet()) {
            Deployment deployment = deploymentMap.get(entry.getValue().getDeploymentId());
            if (null != deployment) {
                entry.getValue().setDeploymentTime(deployment.getDeploymentTime());
            }
        }
        return pepProcessDefinitionMap;
    }


    private Map<String, Deployment> getDeploymentByIds(Set<String> deploymentIds) {
        Map<String, Deployment> deploymentMap = new HashMap<>(16);
        List<Deployment> deployments = repositoryService
            .createNativeDeploymentQuery()
            .sql("SELECT * FROM ACT_RE_DEPLOYMENT WHERE " + getInSql("ID_",
                "IDS", deploymentIds.size())).parameter("IDS", deploymentIds.toArray()).list();
        for (Deployment deployment : deployments) {
            deploymentMap.put(deployment.getId(), deployment);
        }
        return deploymentMap;
    }


    private String getInSql(String fieldName, String paramName, int paramSize) {
        List<String> inValue = new ArrayList<>();
        if (0 == paramSize) {
            return fieldName + " IN (null)";
        }
        for (int i = 0; i < paramSize; i++) {
            inValue.add("#{" + paramName + "[" + i + "]}");
        }
        return fieldName + " IN (" + StringUtils.join(inValue, ",") + ")";
    }
}
