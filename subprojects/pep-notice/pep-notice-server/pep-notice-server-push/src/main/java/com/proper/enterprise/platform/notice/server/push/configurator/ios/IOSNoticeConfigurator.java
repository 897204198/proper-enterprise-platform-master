package com.proper.enterprise.platform.notice.server.push.configurator.ios;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.push.client.ios.IOSNoticeClientApi;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

@Service("iosNoticeConfigurator")
public class IOSNoticeConfigurator extends AbstractPushConfigSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOSNoticeConfigurator.class);

    private FileService fileService;

    @Autowired
    private IOSNoticeClientApi iosNoticeClientApi;

    @Autowired
    public IOSNoticeConfigurator(FileService fileService) {
        this.fileService = fileService;
    }

    public static final String CERT_ID = "certificateId";


    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get(CERT_ID)) {
            throw new ErrMsgException("certificateId can't be null");
        }
        try {
            InputStream inputStream = fileService.download((String) config.get(CERT_ID));
            inputStream.close();
        } catch (Exception e) {
            LOGGER.error("can't find ios cert by cert_id:{}", config.get(CERT_ID), e);
            throw new ErrMsgException("can't find ios cert by cert_id:" + config.get(CERT_ID));
        }
        Map postConf = super.post(appKey, config, request);
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        iosNoticeClientApi.post(appKey, pushDocument);
        return postConf;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        super.delete(appKey, request);
        iosNoticeClientApi.delete(appKey);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map updateConf = super.put(appKey, config, request);
        PushConfDocument pushDocument = buildPushDocument(appKey, config, request);
        iosNoticeClientApi.put(appKey, pushDocument);

        return updateConf;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }

}
