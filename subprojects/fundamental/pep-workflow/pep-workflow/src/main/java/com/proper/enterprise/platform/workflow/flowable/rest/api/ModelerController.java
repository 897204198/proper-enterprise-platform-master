package com.proper.enterprise.platform.workflow.flowable.rest.api;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.enums.ParserEnum;
import com.proper.enterprise.platform.workflow.factory.PEPCandidateExtQueryFactory;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.model.PEPVariablesModel;
import com.proper.enterprise.platform.workflow.service.impl.PEPCandidateRuleExtQueryImpl;
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
    @ApiOperation("????????????????????????????????????")
    public ResponseEntity<Collection<ModelerIdmModel>> get(@PathVariable @ApiParam(value = "???????????????", required = true) String candidateType,
                                                           @ApiParam("?????????") String name) {
        return responseOfGet(convert(PEPCandidateExtQueryFactory
            .product(candidateType).findCandidatesByNameLike(name)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/assignee")
    @ApiOperation("?????????????????????????????????")
    public ResponseEntity<Collection<ModelerIdmModel>> get(@ApiParam("?????????") String name) {
        return responseOfGet(convert(PEPCandidateExtQueryFactory
            .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE)
            .findCandidatesByNameLike(name)));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/rule")
    @ApiOperation("????????????????????????????????????")
    public ResponseEntity<Collection<ModelerIdmModel>> getRule() {
        return responseOfGet(convert(PEPCandidateExtQueryFactory
            .product(PEPCandidateRuleExtQueryImpl.RULE_CONF_CODE)
            .findAllCandidates()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/condition")
    @ApiOperation("???????????????????????????????????????")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ConditionModel> getCondition(@RequestBody ConditionModel conditionModel) {
        String sequenceCondition;
        List<PEPVariablesModel> pepVariablesModels = new ArrayList<>(BeanUtil.convert(conditionModel.getParams(), PEPVariablesModel.class));
        if (conditionModel.getParserEnum() == ParserEnum.TONATUAL) {
            sequenceCondition = FlowableConditionUtil.toNaturalCondition(conditionModel.getSequenceCondition(),
                pepVariablesModels);
        } else {
            sequenceCondition = FlowableConditionUtil.toFlowableCondition(conditionModel.getSequenceCondition(),
                pepVariablesModels);
        }
        ConditionModel result = new ConditionModel();
        result.setSequenceCondition(sequenceCondition);
        return responseOfGet(result);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ApiOperation("?????????model(??????)")
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
     * ?????????????????????????????????????????????
     */
    private static class ModelerIdmModel {

        /**
         * ????????????????????? ???????????????id ??????????????????id:CODE
         */
        private String id;

        /**
         * ??????
         */
        private String name;

        /**
         * ??????
         */
        private String type;

        /**
         * ????????????
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
     * ?????????????????????????????????????????????
     */
    private static class ConditionModel {
        @ApiModelProperty(name = "?????????", required = true)
        private String sequenceCondition;

        @ApiModelProperty(name = "???????????????", required = true)
        private List<VariablesModel> params;

        @ApiModelProperty(name = "???????????????")
        private ParserEnum parserEnum;

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

        public ParserEnum getParserEnum() {
            if (parserEnum == null) {
                return ParserEnum.TOFLOWABLE;
            }
            return parserEnum;
        }

        public void setParserEnum(ParserEnum parserEnum) {
            this.parserEnum = parserEnum;
        }
    }

    private static class VariablesModel {
        @ApiModelProperty(name = "?????????Id", required = true)
        private String id;

        @ApiModelProperty(name = "????????????", required = true)
        private String name;

        @ApiModelProperty(name = "?????????????????????")
        private Boolean textValue;

        @ApiModelProperty(name = "???????????????")
        private String componentKey;

        @ApiModelProperty(name = "?????????????????????????????????")
        private List<VariablesChildrenModel> children;

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

        public Boolean getTextValue() {
            if (null == textValue) {
                return false;
            }
            return textValue;
        }

        public void setTextValue(Boolean textValue) {
            this.textValue = textValue;
        }

        public String getComponentKey() {
            return componentKey;
        }

        public void setComponentKey(String componentKey) {
            this.componentKey = componentKey;
        }

        public List<VariablesChildrenModel> getChildren() {
            return children;
        }

        public void setChildren(List<VariablesChildrenModel> children) {
            this.children = children;
        }
    }

    private static class VariablesChildrenModel {

        @ApiModelProperty(name = "????????????????????????", required = true)
        private String label;

        @ApiModelProperty(name = "????????????????????????", required = true)
        private String value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
