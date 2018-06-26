package com.proper.enterprise.platform.push.client;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.client.model.PushMessage;
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest;
import com.proper.enterprise.platform.push.client.service.impl.PushApiServiceRequestServiceImpl;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * 推送程序
 *
 * @author 沈东生
 *
 */
public class PusherApp {
    private final ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("pusherApp-pool-%d").daemon(true).build();
    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1, namedThreadFactory);

    private static final Logger LOGGER = LoggerFactory.getLogger(PusherApp.class);
    public static final String DEVICE_TYPE_ANDROID = "android";
    public static final String DEVICE_TYPE_IOS = "ios";
    private String secureKey = "";
    private String appkey = "";
    private String pushUrl = "";

    /**
     * 超时时间
     */
    private int connTimeout = 30000;

    /**
     * 是否异步调用
     */
    private boolean isAsync = true;

    private IPushApiServiceRequest pushApiRequest;

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

    /**
     * 默认的http请求方法
     */
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
     */
    public void pushMessageToUsers(final PushMessage msg, final List<String> lstUserIds) {
        Runnable r = () -> {
            String msgStr = null;
            String userids = null;
            try {
                msgStr = JSONUtil.toJSON(msg);
                userids = JSONUtil.toJSON(lstUserIds);
                LinkedHashMap<String, Object> params = new LinkedHashMap<>();
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
        Runnable r = () -> {
            try {
                LinkedHashMap<String, Object> params = new LinkedHashMap<>();
                params.put("msg", JSONUtil.toJSON(msg));
                params.put("deviceids", JSONUtil.toJSON(lstDeviceIds.toArray()));
                params.put("device_type", deviceType);
                params.put("appkey", appkey);
                String rtn = pushApiRequest.requestServiceServer(pushUrl, "pushMessageToDevices", params,
                        connTimeout);
                LOGGER.info("{pushMessageToDevices:}" + rtn);
            } catch (Exception ex) {
                LOGGER.error("Exception:" + ex.getMessage(), ex);
            }
        };
        startRunTask(r, isAsync);

    }

    public void pushMessageToOneDevice(PushMessage msg, String deviceType, String deviceId) {
        List<String> lst = new ArrayList<>(1);
        lst.add(deviceId);
        pushMessageToDevices(msg, deviceType, lst);
    }

    /**
     * 向一个用户推送单条消息
     *
     * @param msg
     *            消息
     * @param userId 用户 id
     */
    public void pushMessageToOneUser(PushMessage msg, String userId) {
        Assert.isTrue(StringUtil.isNotNull(userId), "UserId SHOULD NOT NULL when push message to one user!");
        List<String> lst = new ArrayList<>(1);
        lst.add(userId);
        pushMessageToUsers(msg, lst);
    }

    /**
     * 向所有的用户推送消息。
     *
     * @param msg 消息
     */
    public void pushMessageToAllUsers(final PushMessage msg) {
        Runnable r = () -> {
            try {
                final LinkedHashMap<String, Object> params = new LinkedHashMap<>();
                params.put("msg", JSONUtil.toJSON(msg));
                params.put("appkey", appkey);
                String rtn = pushApiRequest.requestServiceServer(pushUrl, "pushMessageToAllUsers", params,
                        connTimeout);
                LOGGER.info("{pushMessageToAllUsers:}" + rtn);
            } catch (Exception ex) {
                LOGGER.error("Exception:" + ex.getMessage(), ex);
            }
        };
        startRunTask(r, isAsync);

    }

    /**
     * 向所有的设备推送消息，不管设备是否绑定userid
     *
     * @param msg 消息
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
        Runnable r = () -> {
            try {
                final LinkedHashMap<String, Object> params = new LinkedHashMap<>();
                params.put("msg", JSONUtil.toJSON(msg));
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
        };
        startRunTask(r, isAsync);
    }

    /**
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
