package com.proper.enterprise.platform.push.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.proper.enterprise.platform.push.client.model.PushMessage;
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest;
import com.proper.enterprise.platform.push.client.service.impl.PushApiServiceRequestServiceImpl;
import org.springframework.util.Assert;

/**
 * 推送程序
 *
 * @author 沈东生
 *
 */
public class PusherApp {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherApp.class);
    public static final String DEVICE_TYPE_ANDROID = "android";
    public static final String DEVICE_TYPE_IOS = "ios";
    private String secureKey = "";
    private String appkey = "";
    private String pushUrl = "";
    private int connTimeout = 30000; // 超时时间

    private boolean isAsync = true; // 是否异步调用

    IPushApiServiceRequest pushApiRequest;

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public PusherApp() {
        super();
        initDefaultPushApiRequest();
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public IPushApiServiceRequest getPushApiRequest() {
        return pushApiRequest;
    }

    public void setPushApiRequest(IPushApiServiceRequest pushApiRequest) {
        this.pushApiRequest = pushApiRequest;
    }

    /**
     * 新建推送App
     *
     * @param pushUrl
     *            推送对应的服务url
     * @param appkey
     *            推送的对应的应用编号
     * @param secureKey
     *            推送对应的密钥，用于应用服务器向推送服务器间通信的加密。
     */
    public PusherApp(String pushUrl, String appkey, String secureKey) {
        super();
        this.pushUrl = pushUrl;
        this.appkey = appkey;
        this.secureKey = secureKey;
        initDefaultPushApiRequest();
    }

    // 默认的http请求方法
    private void initDefaultPushApiRequest() {
        this.pushApiRequest = new PushApiServiceRequestServiceImpl();
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    /**
     * 向指定的一组用户推送消息
     *
     * @param msg
     *            推送的消息
     * @param lstUserIds
     *            要推送消息的 userid
     * @return 服务器端返回的消息
     */
    public void pushMessageToUsers(final PushMessage msg, final List<String> lstUserIds) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                String msgStr = null;
                String userids = null;
                try {
                    msgStr = (JSONUtil.toJSON(msg)).toString();
                    userids = (JSONUtil.toJSON(lstUserIds)).toString();
                    LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
                    params.put("msg", msgStr);
                    params.put("userids", userids);
                    params.put("appkey", appkey);
                    String rtn = pushApiRequest.requestServiceServer(pushUrl, "pushMessageToUsers", params,
                        connTimeout);
                    LOGGER.info("{pushMessageToUsers:}" + rtn);
                } catch (Exception ex) {
                    LOGGER.error("Error Push: msg is {}, userids is {}, connTimeout is {}.", msgStr, userids, connTimeout);
                    LOGGER.error("Exception:" + ex.getMessage(), ex);
                }
            }
        };
        startRunTask(r, isAsync);

    }

    /**
     * 向一组设备发送消息
     *
     * @param msg
     *            消息正文
     */
    public void pushMessageToDevices(final PushMessage msg, final String deviceType, final List<String> lstDeviceIds) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
                    params.put("msg", (JSONUtil.toJSON(msg)).toString());
                    params.put("deviceids", (JSONUtil.toJSON(lstDeviceIds.toArray())).toString());
                    params.put("device_type", deviceType);
                    params.put("appkey", appkey);
                    String rtn = pushApiRequest.requestServiceServer(pushUrl, "pushMessageToDevices", params,
                            connTimeout);
                    LOGGER.info("{pushMessageToDevices:}" + rtn);
                } catch (Exception ex) {
                    LOGGER.error("Exception:" + ex.getMessage(), ex);
                }
            }
        };
        startRunTask(r, isAsync);

    }

    public void pushMessageToOneDevice(PushMessage msg, String deviceType, String deviceId) {
        List<String> lst = new ArrayList<String>(1);
        lst.add(deviceId);
        pushMessageToDevices(msg, deviceType, lst);
    }

    /**
     * 向一个用户推送单条消息
     *
     * @param msg
     *            消息
     * @param userId 用户 id
     * @return
     */
    public void pushMessageToOneUser(PushMessage msg, String userId) {
        Assert.isTrue(StringUtil.isNotNull(userId), "UserId SHOULD NOT NULL when push message to one user!");
        List<String> lst = new ArrayList<String>(1);
        lst.add(userId);
        pushMessageToUsers(msg, lst);
    }

    /**
     * 向所有的用户推送消息。
     *
     * @param msg 消息
     * @return
     */
    public void pushMessageToAllUsers(final PushMessage msg) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    final LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
                    params.put("msg", (JSONUtil.toJSON(msg)).toString());
                    params.put("appkey", appkey);
                    String rtn = pushApiRequest.requestServiceServer(pushUrl, "pushMessageToAllUsers", params,
                            connTimeout);
                    LOGGER.info("{pushMessageToAllUsers:}" + rtn);
                } catch (Exception ex) {
                    LOGGER.error("Exception:" + ex.getMessage(), ex);
                }
            }
        };
        startRunTask(r, isAsync);

    }

    /**
     * 向所有的设备推送消息，不管设备是否绑定userid
     *
     * @param msg 消息
     * @return
     */
    public void pushMessageToAllDevices(final PushMessage msg) {

        pushMessageToAllDevices(msg, "");

    }

    /**
     * 向指定类型的设备，全推消息
     *
     * @param msg 消息
     * @param deviceType 设备类型
     */
    public void pushMessageToAllDevices(final PushMessage msg, final String deviceType) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    final LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
                    params.put("msg", (JSONUtil.toJSON(msg)).toString());
                    params.put("appkey", appkey);
                    if (deviceType != null && !"".equals(deviceType)) {
                        params.put("device_type", deviceType);
                    }
                    String rtn = pushApiRequest.requestServiceServer(pushUrl, "pushMessageToAllDevices", params,
                            connTimeout);
                    LOGGER.info("{pushMssageToAllDevices:}" + rtn);
                } catch (Exception ex) {
                    LOGGER.error("Exception:" + ex.getMessage(), ex);
                }
            }
        };
        startRunTask(r, isAsync);

    }

    /*
     * 执行任务
     */
    private  void startRunTask(Runnable r, boolean isAsync) {
        if (isAsync) {
            executor.execute(r);
        } else {
            r.run();
        }

    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

}
