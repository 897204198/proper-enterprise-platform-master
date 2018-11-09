package com.proper.enterprise.platform.notice.server.push.configurator.ios;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.client.apns.ApnsNoticeClientManagerApi;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("iosNoticeConfigurator")
public class IOSNoticeConfigurator extends AbstractPushConfigSupport {

    private FileService fileService;

    private ApnsNoticeClientManagerApi iosNoticeClientApi;

    @Autowired
    public IOSNoticeConfigurator(FileService fileService, ApnsNoticeClientManagerApi iosNoticeClientApi) {
        this.fileService = fileService;
        this.iosNoticeClientApi = iosNoticeClientApi;
    }

    private static final String CERT_ID = "certificateId";

    private static final String CERT_PASSWORD = "certPassword";

    private static final String CERT_TYPE = "p12";

    @Override
    public Map post(String appKey, Map<String, Object> config, Map<String, Object> params) {
        if (null == config.get(CERT_PASSWORD)) {
            throw new ErrMsgException("certPassword can't be null");
        }
        if (null == config.get(CERT_ID)) {
            throw new ErrMsgException("certificateId can't be null");
        }
        File file = fileService.findOne((String) config.get(CERT_ID));
        if (null == file) {
            throw new ErrMsgException("apns cert is not find");
        }
        if (!CERT_TYPE.equals(file.getFileType())) {
            throw new ErrMsgException("apns cert type must be p12");
        }
        PushConfDocument pushDocument = buildPushDocument(appKey, config, params);
        iosNoticeClientApi.post(appKey, pushDocument);
        return super.post(appKey, config, params);
    }

    @Override
    public void delete(String appKey, Map<String, Object> params) {
        iosNoticeClientApi.delete(appKey);
        super.delete(appKey, params);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config,  Map<String, Object> params) {
        File file = fileService.findOne((String) config.get(CERT_ID));
        if (!CERT_TYPE.equals(file.getFileType())) {
            throw new ErrMsgException("apns cert type must be p12");
        }
        PushConfDocument pushDocument = buildPushDocument(appKey, config, params);
        iosNoticeClientApi.put(appKey, pushDocument);
        return super.put(appKey, config, params);
    }

    @Override
    public Map get(String appKey, Map<String, Object> params) {
        return super.get(appKey, params);
    }

}
