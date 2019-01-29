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
import java.util.ArrayList;
import java.util.List;

@Component
public class SuccessfulStartListener implements ApplicationListener<ContextRefreshedEvent> {

    private CoreProperties coreProperties;

    private ServerProperties serverProperties;

    private List<String> successMsgs = new ArrayList<>();

    private Integer maxMassageLength;

    @Autowired
    public SuccessfulStartListener(CoreProperties coreProperties, ServerProperties serverProperties) {
        this.coreProperties = coreProperties;
        this.serverProperties = serverProperties;
        this.addSuccessMsg("Propersoft Enterprise Platform (v " + PEPVersion.getVersion() + ") Started Successfully!");
    }

    public void addSuccessMsg(String msg) {
        this.successMsgs.add(msg);
        if (this.maxMassageLength == null) {
            this.maxMassageLength = 0;
        }
        this.maxMassageLength = Math.max(this.maxMassageLength, msg.length());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }

        final long start = event.getTimestamp();
        final String profile = getProfiles(event.getApplicationContext());
        sendStatInfo(profile, start, null);

        int splitLength = "||  ||".length();
        StringBuilder sb = new StringBuilder(this.maxMassageLength + splitLength);
        for (int i = 0; i < this.maxMassageLength + splitLength; i++) {
            sb.append("=");
        }

        PrintStream ps = System.out;
        ps.println();
        ps.println(sb.toString());
        for (String successMsg : successMsgs) {
            ps.print("|| ");
            ps.printf("%-" + this.maxMassageLength + "s", successMsg);
            ps.print(" ||");
            ps.println();
        }
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
