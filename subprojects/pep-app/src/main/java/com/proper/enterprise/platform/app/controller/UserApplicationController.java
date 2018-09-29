package com.proper.enterprise.platform.app.controller;

import com.proper.enterprise.platform.app.service.UserApplicationService;
import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.app.vo.UserApplicationVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "/app/applications")
@RequestMapping(value = "/app/applications")
public class UserApplicationController extends BaseController {

    private UserApplicationService userApplicationService;

    @Autowired
    public UserApplicationController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping
    @ApiOperation("‍用户自定义应用列表‍")
    public ResponseEntity<List<ApplicationVO>> findUserApplications() {
        return responseOfGet(userApplicationService.findUserApplications());
    }

    @PutMapping
    @ApiOperation("‍修改用户自定义应用列表‍")
    public ResponseEntity<UserApplicationVO> putUserApplications(@RequestBody ApplicationParamVO reqMap) {
        String ids = reqMap.getIds();
        return responseOfPut(userApplicationService.saveOrUpdateUserApplications(ids));
    }

    @GetMapping("/all")
    @ApiOperation("‍取得应用菜单‍")
    public ResponseEntity<List<AppCatalogVO>> findCatalogAndApplications() {
        return responseOfGet(userApplicationService.findCatalogAndApplications());
    }

    public static class ApplicationParamVO {

        @ApiModelProperty("‍列表ids")
        private String ids;

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }

    }

}
