package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserNoticeService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.api.auth.service.ValidCodeService;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.encrypt.EncryptUtil;
import com.proper.enterprise.platform.notice.service.NoticeSender;
import com.proper.enterprise.platform.sys.datadic.enums.AppConfigEnum;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserNoticeServiceImpl implements UserNoticeService {

    @Autowired
    private NoticeSender noticeSender;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidCodeService validCodeService;

    @Override
    public String sendValidCode(String userName) {
        User user = userService.getByUsername(userName, EnableEnum.ALL);
        if (null == user) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.username.not.exist"));
        }
        if (StringUtil.isEmpty(user.getEmail())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.password.retrieve.email.not.exit"));
        }
        Map<String, Object> templateParams = new HashMap<>(16);
        templateParams.put("appName", DataDicUtil.get(AppConfigEnum.NAME).getName());
        templateParams.put("userName", user.getUsername());
        templateParams.put("validCode", validCodeService.getPasswordValidCode(userName));
        Map<String, Object> custom = new HashMap<>(0);
        //设置标题
        custom.put("title", DataDicUtil.get(AppConfigEnum.NAME).getName() + I18NUtil.getMessage("pep.auth.common.password.retrieve"));
        noticeSender.sendNotice(user.getId(), "passwordRetrieve", templateParams, custom);
        return I18NUtil.getMessage("pep.auth.common.password.retrieve.email.sent") + ":" + EncryptUtil.encryptEmail(user.getEmail());
    }


}
