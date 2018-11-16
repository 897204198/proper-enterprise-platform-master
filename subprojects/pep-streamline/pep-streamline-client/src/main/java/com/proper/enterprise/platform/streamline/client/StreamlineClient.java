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

import java.io.UnsupportedEncodingException;
import java.util.Collection;

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
                result.setMessage(handleResponseMsg(responseEntity));
            }
        } catch (Exception e) {
            result.setStatus(SignStatus.FAIL);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 批量注册标记
     *
     * @param signRequests 注册信息
     */
    public Result addSigns(Collection<SignRequest> signRequests) {
        signRequests.forEach(signRequest -> signRequest.setServiceKey(this.serviceKey));
        Result result = new Result();
        try {
            String serverUrl = ConfCenter.get("streamline.server.url");
            ResponseEntity<byte[]> responseEntity =
                HttpClient.post(serverUrl + "/streamline/signs", MediaType.APPLICATION_JSON, JSONUtil.toJSON(signRequests));
            if (HttpStatus.CREATED.equals(responseEntity.getStatusCode())) {
                result.setStatus(SignStatus.SUCCESS);
            } else {
                result.setStatus(SignStatus.FAIL);
                result.setMessage(handleResponseMsg(responseEntity));
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
                result.setMessage(handleResponseMsg(responseEntity));
            }
        } catch (Exception e) {
            result.setStatus(SignStatus.FAIL);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 批量更新标记
     *
     * @param signRequests 注册信息
     */
    public Result updateSigns(Collection<SignRequest> signRequests) {
        signRequests.forEach(signRequest -> signRequest.setServiceKey(this.serviceKey));
        Result result = new Result();
        try {
            String serverUrl = ConfCenter.get("streamline.server.url");
            ResponseEntity<byte[]> responseEntity =
                HttpClient.put(serverUrl + "/streamline/signs", MediaType.APPLICATION_JSON, JSONUtil.toJSON(signRequests));
            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                result.setStatus(SignStatus.SUCCESS);
            } else {
                result.setStatus(SignStatus.FAIL);
                result.setMessage(handleResponseMsg(responseEntity));
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
                result.setMessage(handleResponseMsg(responseEntity));
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
     * @param businessIds 用户Id集合
     */
    public Result deleteSigns(String businessIds) {
        Result result = new Result();
        try {
            String serverUrl = ConfCenter.get("streamline.server.url");
            ResponseEntity<byte[]> responseEntity =
                HttpClient.delete(serverUrl + "/streamline?businessIds=" + businessIds);
            if (HttpStatus.NO_CONTENT.equals(responseEntity.getStatusCode())) {
                result.setStatus(SignStatus.SUCCESS);
            } else {
                result.setStatus(SignStatus.FAIL);
                result.setMessage(handleResponseMsg(responseEntity));
            }
        } catch (Exception e) {
            result.setStatus(SignStatus.FAIL);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 将失败请求的响应代码以及响应内容输出
     *
     * @param responseEntity 响应对象
     * @return 响应内容
     * @throws UnsupportedEncodingException 异常
     */
    private String handleResponseMsg(ResponseEntity<byte[]> responseEntity) throws UnsupportedEncodingException {
        if (responseEntity == null) {
            return "";
        }
        StringBuffer msg = new StringBuffer();
        msg.append("response status : ");
        msg.append(responseEntity.getStatusCode());
        msg.append(" , response body : ");
        byte[] body = responseEntity.getBody();
        if (body != null) {
            msg.append(new String(body, PEPConstants.DEFAULT_CHARSET.name()));
        }
        return msg.toString();
    }
}
