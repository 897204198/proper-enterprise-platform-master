package com.proper.enterprise.platform.notice.server.push.configurator.huawei;

import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;

import java.io.IOException;

public interface HuaweiNoticeClient {

    String getAccessToken(String appKey);

    PushConfDocument getConf(String appKey);

    String post(String postUrl, String postBody) throws IOException;
}
