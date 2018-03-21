package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.workflow.vo.PEPModelVO;
import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO;
import com.proper.enterprise.platform.workflow.service.PEPModelService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.app.domain.editor.Model;
import org.flowable.app.model.common.ResultListDataRepresentation;
import org.flowable.app.repository.editor.ModelRepository;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PEPModelServiceImpl implements PEPModelService {


    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private DataDicService dataDicService;

    @Override
    public ResultListDataRepresentation getModels(String filter, String sort, Integer modelType) {
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
        List<PEPModelVO> returnData = packageModelAndProcess(pepModelVOS, getProcessDefinitionsByKey(modelKeys));
        ResultListDataRepresentation resultListDataRepresentation = new ResultListDataRepresentation();
        resultListDataRepresentation.setData(returnData);
        resultListDataRepresentation.setSize(returnData.size());
        resultListDataRepresentation.setStart(0);
        resultListDataRepresentation.setTotal((long) returnData.size());
        return resultListDataRepresentation;
    }

    private List<PEPModelVO> packageModelAndProcess(List<PEPModelVO> pepModelVOS,
                                                    Map<String, PEPProcessDefinitionVO> pepProcessDefinitionMap) {
        for (PEPModelVO pepModelVO : pepModelVOS) {
            PEPProcessDefinitionVO pepProcessDefinitionVO = pepProcessDefinitionMap.get(pepModelVO.getKey());
            if (null != pepProcessDefinitionVO) {
                pepModelVO.setProcessVersion(pepProcessDefinitionVO.getVersion());
                pepModelVO.setDeploymentTime(pepProcessDefinitionVO.getDeploymentTime());
                pepModelVO.setStatus(buildModelStatus(pepModelVO, pepProcessDefinitionVO));
                continue;
            }
            pepModelVO.setStatus(dataDicService.get(PEPModelVO.ModelStatus.UN_DEPLOYED));
        }
        return pepModelVOS;
    }

    private DataDicLite buildModelStatus(PEPModelVO pepModelVO, PEPProcessDefinitionVO pepProcessDefinitionVO) {
        if (pepModelVO.getLastUpdated().getTime() > pepProcessDefinitionVO.getDeploymentTime().getTime()) {
            return dataDicService.get(PEPModelVO.ModelStatus.UN_DEPLOYED);
        }
        return dataDicService.get(PEPModelVO.ModelStatus.DEPLOYED);
    }

    private Map<String, PEPProcessDefinitionVO> getProcessDefinitionsByKey(Set<String> modelKeys) {
        Map<String, PEPProcessDefinitionVO> returnMap = new HashMap<>();
        Set<String> deploymentIds = new HashSet<>();
        List<ProcessDefinition> processDefinitions = repositoryService
            .createNativeProcessDefinitionQuery()
            .sql("SELECT * FROM ACT_RE_PROCDEF a inner join ("
                + "SELECT c.KEY_,max(c.VERSION_) as VERSION_ FROM ACT_RE_PROCDEF c WHERE "
                + getInSql("c.KEY_", "KEYS", modelKeys.size()) + " GROUP BY c.KEY_ \n"
                + ") b on a.KEY_=b.KEY_ and a.VERSION_ =b.VERSION_").parameter("KEYS", modelKeys.toArray()).list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            PEPProcessDefinitionVO pepProcessDefinitionVO = new PEPProcessDefinitionVO(processDefinition);
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
        Map<String, Deployment> deploymentMap = new HashMap<>();
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
