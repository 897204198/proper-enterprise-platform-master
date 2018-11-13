package com.proper.enterprise.platform.notice.schedule;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("delPushTokenTask")
public class DelPushTokenTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelPushTokenTask.class);

    @Autowired
    PushDeviceService pushDeviceService;

    public void getErrorToken() {
        String noticeServerUrl = null;
        DataDic dataDic = DataDicUtil.get("NOTICE_SERVER", "URL");
        if (dataDic != null) {
            noticeServerUrl = dataDic.getName();
        }

        String noticeServerToken = null;
        dataDic = DataDicUtil.get("NOTICE_SERVER", "TOKEN");
        if (dataDic != null) {
            noticeServerToken = dataDic.getName();
        }
        try {
            String url = noticeServerUrl + "/notice/server/msg/app?access_token=" + noticeServerToken;
            ResponseEntity<byte[]> response = HttpClient.get(url);
            byte[] body = response.getBody();
            if (body == null) {
                LOGGER.debug("Get empty body from {}", url);
                return;
            }
            Charset charset = Charset.defaultCharset();
            ByteBuffer buf = ByteBuffer.wrap(body);
            CharBuffer content = charset.decode(buf);
            List<Map<String, Object>> list = JSONUtil.parse(content.toString(), List.class);
            if (CollectionUtil.isNotEmpty(list)) {
                for (Map map : list) {
                    if ("FAIL".equals(map.get("status")) && "Invalid target".equals(map.get("errorCode"))) {
                        pushDeviceService.deleteByToken(map.get("targetTo").toString());
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("delPushTokenTask[Exception]:", e);
        }
    }
}
