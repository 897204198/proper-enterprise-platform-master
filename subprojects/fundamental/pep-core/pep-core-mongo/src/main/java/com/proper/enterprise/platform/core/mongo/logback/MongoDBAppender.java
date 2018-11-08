package com.proper.enterprise.platform.core.mongo.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.utils.MacAddressUtil;
import org.bson.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private static final String MAC_COMPRESSED_ADDRESS;

    static {
        MAC_COMPRESSED_ADDRESS = MacAddressUtil.getCompressedMacAddress();
    }

    private MongoDBConnectionSource connectionSource;

    @Override
    protected void append(ILoggingEvent eventObject) {
        Document document = new Document();
        document.append("lv", eventObject.getLevel().toString());
        String timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(eventObject.getTimeStamp()), ZoneId.of("GMT")).format(DateTimeFormatter
                .ofPattern(PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat()));
        document.append("tm", timestamp);
        StackTraceElement stackTraceElement = eventObject.getCallerData()[0];
        document.append("clz", stackTraceElement.getClassName() + ":" + stackTraceElement.getLineNumber());
        document.append("msg", eventObject.getFormattedMessage() + addStackTraceIfNeeded(eventObject));
        document.append("mac", MAC_COMPRESSED_ADDRESS);
        connectionSource.getMongoCollection().insertOne(document);
    }

    private String addStackTraceIfNeeded(ILoggingEvent eventObject) {
        IThrowableProxy proxy = eventObject.getThrowableProxy();
        if (proxy == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(logStackTrace(proxy));
        for (IThrowableProxy suppressed : proxy.getSuppressed()) {
            sb.append(logStackTrace(suppressed));
        }
        sb.append(logStackTrace(proxy.getCause()));
        return sb.toString();
    }

    private String logStackTrace(IThrowableProxy proxy) {
        if (proxy == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("\r\n")
            .append(proxy.getClassName()).append(": ").append(proxy.getMessage());
        for (StackTraceElementProxy step : proxy.getStackTraceElementProxyArray()) {
            sb.append("\r\n\t").append(step.getSTEAsString());
        }
        return sb.toString();
    }

    public void setConnectionSource(MongoDBConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

}
