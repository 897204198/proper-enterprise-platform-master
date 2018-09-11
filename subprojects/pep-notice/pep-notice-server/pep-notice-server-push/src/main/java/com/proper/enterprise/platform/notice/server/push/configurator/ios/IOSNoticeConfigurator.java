package com.proper.enterprise.platform.notice.server.push.configurator.ios;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.client.ios.IOSNoticeClientApi;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service("iosNoticeConfigurator")
public class IOSNoticeConfigurator extends AbstractPushConfigSupport {

    private FileService fileService;

    private IOSNoticeClientApi iosNoticeClientApi;

    private static final String PUSH_PACKAGE = "pushPackage";

    @Autowired
    public IOSNoticeConfigurator(FileService fileService, IOSNoticeClientApi iosNoticeClientApi) {
        this.fileService = fileService;
        this.iosNoticeClientApi = iosNoticeClientApi;
    }

    private static final String CERT_ID = "certificateId";

    private static final String P12_PASSWORD = "p12Password";

    private static final String CERT_TYPE = "p12";

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get(P12_PASSWORD)) {
            throw new ErrMsgException("p12Password can't be null");
        }
        if (null == config.get(CERT_ID)) {
            throw new ErrMsgException("certificateId can't be null");
        }
        File file = fileService.findOne((String) config.get(CERT_ID));
        if (null == file) {
            throw new ErrMsgException("ios cert is not find");
        }
        if (!CERT_TYPE.equals(file.getFileType())) {
            throw new ErrMsgException("ios cert type must be p12");
        }
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        iosNoticeClientApi.post(appKey, pushDocument);
        return super.post(appKey, config, request);
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        iosNoticeClientApi.delete(appKey);
        super.delete(appKey, request);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        File file = fileService.findOne((String) config.get(CERT_ID));
        if (!CERT_TYPE.equals(file.getFileType())) {
            throw new ErrMsgException("ios cert type must be p12");
        }
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        iosNoticeClientApi.put(appKey, pushDocument);
        return super.put(appKey, config, request);
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }

}
