package com.proper.enterprise.platform.streamline.client;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.streamline.client.result.Result;
import com.proper.enterprise.platform.streamline.sdk.request.SignRequest;
import com.proper.enterprise.platform.streamline.sdk.status.SignStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class StreamlineClient {

    public StreamlineClient(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    private String serviceKey;

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * 根据用户Id、用户名和密码注册标记
     *
     * @param businessId 用户Id
     * @param userName   用户名
     * @param password   密码
     */
    public Result addSign(String businessId, String userName, String password) {
        SignRequest signRequestParam = new SignRequest();
        signRequestParam.setBusinessId(businessId);
        signRequestParam.setUserName(userName);
        signRequestParam.setPassword(password);
        signRequestParam.setServiceKey(this.serviceKey);
        Result result = new Result();
        try {
            String serverUrl = ConfCenter.get("streamline.server.url");
            ResponseEntity<byte[]> responseEntity =
                HttpClient.post(serverUrl + "/streamline", MediaType.APPLICATION_JSON, JSONUtil.toJSON(signRequestParam));
            if (HttpStatus.CREATED.equals(responseEntity.getStatusCode())) {
                result.setStatus(SignStatus.SUCCESS);
            } else {
                result.setStatus(SignStatus.FAIL);
                result.setMessage(new String(responseEntity.getBody(), PEPConstants.DEFAULT_CHARSET.name()));
            }
        } catch (Exception e) {
            result.setStatus(SignStatus.FAIL);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 根据用户Id更新标记
     *
     * @param userName   用户名
     * @param password   密码
     * @param businessId 用户Id
     */
    public Result updateSign(String userName, String password, String businessId) {
        SignRequest signRequestParam = new SignRequest();
        signRequestParam.setBusinessId(businessId);
        signRequestParam.setUserName(userName);
        signRequestParam.setPassword(password);
        signRequestParam.setServiceKey(this.serviceKey);
        Result result = new Result();
        try {
            String serverUrl = ConfCenter.get("streamline.server.url");
            ResponseEntity<byte[]> responseEntity =
                HttpClient.put(serverUrl + "/streamline", MediaType.APPLICATION_JSON, JSONUtil.toJSON(signRequestParam));
            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                result.setStatus(SignStatus.SUCCESS);
            } else {
                result.setStatus(SignStatus.FAIL);
                result.setMessage(new String(responseEntity.getBody(), PEPConstants.DEFAULT_CHARSET.name()));
            }
        } catch (Exception e) {
            result.setStatus(SignStatus.FAIL);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 根据用户Id删除标记
     *
     * @param businessId 用户Id
     */
    public Result deleteSign(String businessId) {
        Result result = new Result();
        try {
            String serverUrl = ConfCenter.get("streamline.server.url");
            ResponseEntity<byte[]> responseEntity =
                HttpClient.delete(serverUrl + "/streamline/" + businessId);
            if (HttpStatus.NO_CONTENT.equals(responseEntity.getStatusCode())) {
                result.setStatus(SignStatus.SUCCESS);
            } else {
                result.setStatus(SignStatus.FAIL);
                result.setMessage(new String(responseEntity.getBody(), PEPConstants.DEFAULT_CHARSET.name()));
            }
        } catch (Exception e) {
            result.setStatus(SignStatus.FAIL);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
