package com.proper.enterprise.platform.workflow.flowable.rest.api;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.factory.PEPCandidateExtQueryFactory;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.service.impl.PEPCandidateUserExtQueryImpl;
import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/workflow/ext/modeler")
@Api("/workflow/ext/modeler")
@AuthcIgnore
public class ModelerController extends BaseController {

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


}
