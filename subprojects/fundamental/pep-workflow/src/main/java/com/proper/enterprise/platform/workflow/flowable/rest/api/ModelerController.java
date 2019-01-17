package com.proper.enterprise.platform.workflow.flowable.rest.api;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.factory.PEPCandidateExtQueryFactory;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.service.impl.PEPCandidateUserExtQueryImpl;
import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import com.proper.enterprise.platform.workflow.util.FlowableConditionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelKeyRepresentation;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/workflow/ext/modeler")
@Api("/workflow/ext/modeler")
@AuthcIgnore
public class ModelerController extends BaseController {

    @Autowired
    private ModelService modelService;

    @RequestMapping(method = RequestMethod.GET, value = "/candidate/{candidateType}")
    @ApiOperation("‍流程设计器查询候选集合")
    public ResponseEntity<Collection<ModelerIdmModel>> get(@PathVariable @ApiParam(value = "‍候选类型", required = true) String candidateType,
                                                           @ApiParam("‍名称") String name) {
        return responseOfGet(convert(PEPCandidateExtQueryFactory
            .product(candidateType).findCandidatesByNameLike(name)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/assignee")
    @ApiOperation("‍流程设计器查询经办人")
    public ResponseEntity<Collection<ModelerIdmModel>> get(@ApiParam("‍名称") String name) {
        return responseOfGet(convert(PEPCandidateExtQueryFactory
            .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE)
            .findCandidatesByNameLike(name)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/condition")
    @ApiOperation("‍‍流程设计器转换分支条件")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ConditionModel> getCondition(@RequestBody ConditionModel conditionModel) {
        Map<String, Object> params = new HashMap<>(conditionModel.getParams().size());
        conditionModel.getParams().forEach(
            variablesModel -> params.put(variablesModel.getName(), variablesModel.getId())
        );
        String sequenceCondition;
        if (conditionModel.getParsing()) {
            sequenceCondition = FlowableConditionUtil.toFlowableCondition(conditionModel.getSequenceCondition(), params);
        } else {
            sequenceCondition = FlowableConditionUtil.toNatural(conditionModel.getSequenceCondition(), params);
        }
        ConditionModel result = new ConditionModel();
        result.setSequenceCondition(sequenceCondition);
        return responseOfGet(result);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ApiOperation("‍创建model(表单)")
    public ModelRepresentation createForm(@RequestBody ModelRepresentation modelRepresentation) {
        modelRepresentation.setKey(modelRepresentation.getKey().replaceAll(" ", ""));
        ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(null, modelRepresentation.getModelType(), modelRepresentation.getKey());
        if (modelKeyInfo.isKeyAlreadyExists()) {
            return modelService.getModelRepresentation(modelKeyInfo.getId());
        }

        String json = modelService.createModelJson(modelRepresentation);

        Model newModel = modelService.createModel(modelRepresentation, json, simulationCurrentUser());
        return new ModelRepresentation(newModel);
    }

    private Collection<ModelerIdmModel> convert(Collection<PEPCandidateModel> pepCandidateModels) {
        Collection<ModelerIdmModel> modelerIdmModels = new ArrayList<>();
        if (CollectionUtil.isEmpty(pepCandidateModels)) {
            return modelerIdmModels;
        }
        for (PEPCandidateModel pepCandidateModel : pepCandidateModels) {
            modelerIdmModels.add(convert(pepCandidateModel));
        }
        return modelerIdmModels;
    }

    private ModelerIdmModel convert(PEPCandidateModel pepCandidateModel) {
        if (null == pepCandidateModel) {
            return null;
        }
        ModelerIdmModel modelerIdmModel = new ModelerIdmModel();
        modelerIdmModel.setId(CandidateIdUtil.encode(pepCandidateModel.getId(), pepCandidateModel.getType()));
        if (PEPCandidateUserExtQueryImpl.USER_CONF_CODE.equals(pepCandidateModel.getType())) {
            modelerIdmModel.setId(pepCandidateModel.getId());
        }
        modelerIdmModel.setName(pepCandidateModel.getName());
        modelerIdmModel.setType(pepCandidateModel.getType());
        modelerIdmModel.setTypeName(pepCandidateModel.getTypeName());
        return modelerIdmModel;
    }

    private User simulationCurrentUser() {
        User user = new UserEntityImpl();
        user.setId(Authentication.getCurrentUserId());
        return user;
    }

    /**
     * 返回给前台流程设计器的选择模型
     */
    private static class ModelerIdmModel {

        /**
         * 经过编码的主键 人员为人员id 用户组角色为id:CODE
         */
        private String id;

        /**
         * 名称
         */
        private String name;

        /**
         * 类型
         */
        private String type;

        /**
         * 类型名称
         */
        private String typeName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    /**
     * 返回给前台流程设计器的条件对象
     */
    private static class ConditionModel {
        @ApiModelProperty(name = "‍条件", required = true)
        private String sequenceCondition;

        @ApiModelProperty(name = "‍替换变量", required = true)
        private List<VariablesModel> params;

        @ApiModelProperty(name = "‍转换方向, true 为转换为flowable条件, fasle 为转换为自然语言")
        private Boolean parsing;

        public String getSequenceCondition() {
            return sequenceCondition;
        }

        public void setSequenceCondition(String sequenceCondition) {
            this.sequenceCondition = sequenceCondition;
        }

        public List<VariablesModel> getParams() {
            return params;
        }

        public void setParams(List<VariablesModel> params) {
            this.params = params;
        }

        public Boolean getParsing() {
            if (parsing == null) {
                return false;
            }
            return parsing;
        }

        public void setParsing(Boolean parsing) {
            this.parsing = parsing;
        }
    }

    private static class VariablesModel {
        @ApiModelProperty(name = "‍变量Id", required = true)
        private String id;

        @ApiModelProperty(name = "‍变量名", required = true)
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
