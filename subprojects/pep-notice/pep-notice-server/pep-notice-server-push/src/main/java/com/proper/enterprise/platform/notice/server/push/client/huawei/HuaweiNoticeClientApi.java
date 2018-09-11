package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.proper.enterprise.platform.notice.server.push.client.BasePushNoticeClientApi;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;

import java.io.IOException;

public interface HuaweiNoticeClientApi extends BasePushNoticeClientApi {

    /**
     * 根据华为 appKey获取access token
     *
     * @param appKey 系统唯一标识
     * @return access token
     */
    String getAccessToken(String appKey);

    /**
     * 根据华为 appKey获取华为配置信息
     *
     * @param appKey 系统唯一标识
     * @return 华为配置信息
     */
    PushConfDocument getConf(String appKey);

    /**
     * http post请求处理
     *
     * @param postUrl  url
     * @param postBody 主体
     * @return 响应内容
     * @throws IOException 异常
     */
    String handlePost(String postUrl, String postBody) throws IOException;
}
