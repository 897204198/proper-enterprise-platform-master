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
import com.proper.enterprise.platform.core.model.CurrentModel;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.streamline.sdk.constants.StreamlineConstant;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
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
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginMap, HttpServletRequest request) throws IOException {
        String username = authcService.getUsername(loginMap);
        String pwd = authcService.getPassword(loginMap);

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
    public ResponseEntity logout() {
        User user = userService.getCurrentUser();
        String username = user.getUsername();
        if (username != null) {
            jwtAuthcService.clearUserToken(username);
        }
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

}
