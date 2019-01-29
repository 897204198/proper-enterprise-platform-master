package com.proper.enterprise.platform.core.listener;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPVersion;
import com.proper.enterprise.platform.core.utils.MacAddressUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.Callback;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
class SuccessfulStartListener implements ApplicationListener<ContextRefreshedEvent> {

    private CoreProperties coreProperties;

    private ServerProperties serverProperties;

    @Autowired
    public SuccessfulStartListener(CoreProperties coreProperties, ServerProperties serverProperties) {
        this.coreProperties = coreProperties;
        this.serverProperties = serverProperties;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }

        final long start = event.getTimestamp();
        final String profile = getProfiles(event.getApplicationContext());
        sendStatInfo(profile, start, null);

        PrintStream ps = System.out;
        String success = "|| Propersoft Enterprise Platform (v" + PEPVersion.getVersion() + ") Started Successfully! ||";
        int len = success.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append("=");
        }
        ps.println();
        ps.println(sb.toString());
        ps.println(success);
        ps.println(sb.toString());
        ps.println();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> sendStatInfo(profile, start, System.currentTimeMillis())));
    }

    private String getProfiles(ApplicationContext context) {
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        if (ObjectUtils.isEmpty(activeProfiles)) {
            activeProfiles = context.getEnvironment().getDefaultProfiles();
        }
        return StringUtils.arrayToCommaDelimitedString(activeProfiles);
    }

    private void sendStatInfo(String profile, long start, Long end) {
        String ver = PEPVersion.getVersion();
        String mac = MacAddressUtil.getCompressedMacAddress();
        String contextPath = serverProperties.getServlet().getContextPath();
        if (StringUtil.isBlank(contextPath)) {
            contextPath = "/";
        }
        String begin = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault()).toString();
        String range = end == null ? "" : String.valueOf(end - start);
        String url = String.format("%s/%s,%s,%s,%s,%s,%s", coreProperties.getStatSite(), ver, profile, mac, contextPath, begin, range);
        // Don't care request success or error, connection timeout in 200 ms
        HttpClient.get(url, 200, new Callback() {
            @Override
            public void onSuccess(ResponseEntity<byte[]> responseEntity) { }

            @Override
            public void onError(IOException ioe) { }
        });
    }

}
