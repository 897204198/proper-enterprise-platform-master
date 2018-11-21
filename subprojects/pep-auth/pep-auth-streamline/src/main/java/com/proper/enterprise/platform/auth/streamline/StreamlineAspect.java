package com.proper.enterprise.platform.auth.streamline;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.streamline.client.StreamlineClient;
import com.proper.enterprise.platform.streamline.client.result.Result;
import com.proper.enterprise.platform.streamline.sdk.request.SignRequest;
import com.proper.enterprise.platform.streamline.sdk.status.SignStatus;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("streamlineAspect")
@Lazy(false)
public class StreamlineAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamlineAspect.class);

    @Autowired
    private I18NService i18NService;

    public void insert(User user) {
        String serviceKey = null;
        DataDic dataDic = DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY");
        if (dataDic != null) {
            serviceKey = dataDic.getName();
        }
        Result result = new StreamlineClient(serviceKey).addSign(user.getId(), user.getUsername(), user.getPassword());
        if (SignStatus.FAIL.equals(result.getStatus())) {
            LOGGER.error("register to streamline throw an error : {}", result.getMessage());
            throw new ErrMsgException(i18NService.getMessage("pep.core.connect.timeout"));
        }
    }

    public void insertBatch(User... users) {
        String serviceKey = null;
        DataDic dataDic = DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY");
        if (dataDic != null) {
            serviceKey = dataDic.getName();
        }
        List<SignRequest> signRequests = new ArrayList<>();
        for (User user : users) {
            SignRequest signRequest = new SignRequest();
            signRequest.setServiceKey(serviceKey);
            signRequest.setBusinessId(user.getId());
            signRequest.setUserName(user.getUsername());
            signRequest.setPassword(user.getPassword());
            signRequests.add(signRequest);
        }
        Result result = new StreamlineClient(serviceKey).addSigns(signRequests);
        if (SignStatus.FAIL.equals(result.getStatus())) {
            LOGGER.error("register to streamline throw an error : {}", result.getMessage());
            throw new ErrMsgException(i18NService.getMessage("pep.core.connect.timeout"));
        }
    }

    public void update(User user) {
        String serviceKey = null;
        DataDic dataDic = DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY");
        if (dataDic != null) {
            serviceKey = dataDic.getName();
        }
        Result result = new StreamlineClient(serviceKey).updateSign(user.getUsername(), user.getPassword(), user.getId());
        if (SignStatus.FAIL.equals(result.getStatus())) {
            LOGGER.error("update to streamline throw an error : {}", result.getMessage());
            throw new ErrMsgException(i18NService.getMessage("pep.core.connect.timeout"));
        }
    }

    public void updateBatch(Collection<? extends User> users) {
        String serviceKey = null;
        DataDic dataDic = DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY");
        if (dataDic != null) {
            serviceKey = dataDic.getName();
        }
        List<SignRequest> signRequests = new ArrayList<>();
        for (User user : users) {
            SignRequest signRequest = new SignRequest();
            signRequest.setServiceKey(serviceKey);
            signRequest.setBusinessId(user.getId());
            signRequest.setUserName(user.getUsername());
            signRequest.setPassword(user.getPassword());
            signRequests.add(signRequest);
        }
        Result result = new StreamlineClient(serviceKey).updateSigns(signRequests);
        if (SignStatus.FAIL.equals(result.getStatus())) {
            LOGGER.error("update to streamline throw an error : {}", result.getMessage());
            throw new ErrMsgException(i18NService.getMessage("pep.core.connect.timeout"));
        }
    }

    public void delete(JoinPoint joinPoint) {
        String serviceKey = null;
        DataDic dataDic = DataDicUtil.get("STREAMLINE_SERVER", "SERVICE_KEY");
        if (dataDic != null) {
            serviceKey = dataDic.getName();
        }
        Object[] args = joinPoint.getArgs();
        String ids = (String) args[0];
        Result result = new StreamlineClient(serviceKey).deleteSigns(ids);
        if (SignStatus.FAIL.equals(result.getStatus())) {
            LOGGER.error("delete from streamline throw an error : {}", result.getMessage());
            throw new ErrMsgException(i18NService.getMessage("pep.core.connect.timeout"));
        }
    }
}
