package com.proper.enterprise.platform.auth.streamline;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.streamline.client.StreamlineClient;
import com.proper.enterprise.platform.streamline.client.result.Result;
import com.proper.enterprise.platform.streamline.sdk.request.SignRequest;
import com.proper.enterprise.platform.streamline.sdk.status.SignStatus;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("streamlineAspect")
@Lazy(false)
public class StreamlineAspect {

    @Value("${streamline.server.serviceKey}")
    private String serviceKey;

    public void insert(User user) {
        Result result = new StreamlineClient(serviceKey).addSign(user.getId(), user.getUsername(), user.getPassword());
        if (SignStatus.FAIL.equals(result.getStatus())) {
            throw new ErrMsgException(result.getMessage());
        }
    }

    public void insertBatch(User... users) {
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
            throw new ErrMsgException(result.getMessage());
        }
    }

    public void update(User user) {
        Result result = new StreamlineClient(serviceKey).updateSign(user.getUsername(), user.getPassword(), user.getId());
        if (SignStatus.FAIL.equals(result.getStatus())) {
            throw new ErrMsgException(result.getMessage());
        }
    }

    public void updateBatch(Collection<? extends User> users) {
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
            throw new ErrMsgException(result.getMessage());
        }
    }

    public void delete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String ids = (String) args[0];
        Result result = new StreamlineClient(serviceKey).deleteSigns(ids);
        if (SignStatus.FAIL.equals(result.getStatus())) {
            throw new ErrMsgException(result.getMessage());
        }
    }
}
