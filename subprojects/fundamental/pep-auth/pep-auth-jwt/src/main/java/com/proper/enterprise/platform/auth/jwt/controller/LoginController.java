package com.proper.enterprise.platform.auth.jwt.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.AuthcService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO;
import com.proper.enterprise.platform.auth.common.vo.UserVO;
import com.proper.enterprise.platform.auth.service.JWTAuthcService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.i18n.I18NService;
import com.proper.enterprise.platform.core.model.CurrentModel;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.streamline.sdk.constants.StreamlineConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Api(tags = "/auth")
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private static final String[] IGNORE_ROLES = {"parent", "menus", "users", "userGroups", "resources"};

    private static final String[] IGNORE_USER_GROUPS = {"users", "roles"};

    @Autowired
    private AuthcService authcService;

    @Autowired
    private JWTAuthcService jwtAuthcService;

    @Autowired
    private UserService userService;

    @Autowired
    private I18NService i18NService;

    @AuthcIgnore
    @ApiOperation("???????????????")
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> login(@RequestBody LoginVO loginMap, HttpServletRequest request) throws IOException {
        String username = loginMap.getUsername();
        String pwd = loginMap.getPwd();

        HttpHeaders headers = new HttpHeaders();
        if (StringUtil.isNotEmpty(request.getHeader(StreamlineConstant.SERVICE_KEY))) {
            headers.add(StreamlineConstant.SERVICE_KEY, request.getHeader(StreamlineConstant.SERVICE_KEY));
            LOGGER.debug("request header service key is {}", request.getHeader(StreamlineConstant.SERVICE_KEY));
        }

        LOGGER.debug("User {} want to login", username);

        if (authcService.authenticate(username, pwd)) {
            return new ResponseEntity<>(jwtAuthcService.getUserToken(username), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(i18NService.getMessage("pep.auth.common.user.password.not.match"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/auth/current/user", method = RequestMethod.GET)
    @JsonView(UserVO.CurrentUser.class)
    @ApiOperation("?????????????????????????????????")
    public ResponseEntity<CurrentModel> getCurrentUser() {
        User user = userService.getCurrentUser();
        UserVO currentUserVO = BeanUtil.convert(user, UserVO.class);
        if (CollectionUtil.isNotEmpty(user.getRoles())) {
            currentUserVO.setRoles(BeanUtil.convert(userService.getUserRoles(user.getId(), EnableEnum.ENABLE),
                RoleVO.class, IGNORE_ROLES));
        }
        if (CollectionUtil.isNotEmpty(user.getUserGroups())) {
            currentUserVO.setUserGroups(BeanUtil.convert(userService.getUserGroups(user.getId(), EnableEnum.ENABLE),
                UserGroupVO.class, IGNORE_USER_GROUPS));
        }
        CurrentModel currentModel = new CurrentModel();
        currentModel.setId(currentUserVO.getId());
        currentModel.setData(currentUserVO);
        return new ResponseEntity<>(currentModel, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/logout", method = RequestMethod.POST)
    @ApiOperation("????????????????????????token")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity logout() {
        User user = userService.getCurrentUser();
        String username = user.getUsername();
        if (username != null) {
            jwtAuthcService.clearUserToken(username);
        }
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    public static class LoginVO {

        @ApiModelProperty(name = "????????????", required = true)
        private String username;

        @ApiModelProperty(name = "?????????", required = true)
        private String pwd;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }

    }

}
