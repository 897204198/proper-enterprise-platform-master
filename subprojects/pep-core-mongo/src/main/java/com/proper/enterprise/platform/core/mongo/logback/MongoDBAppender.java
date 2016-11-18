package com.proper.enterprise.platform.core.mongo.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.bson.Document;
import org.joda.time.LocalDateTime;

public class MongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private MongoDBConnectionSource connectionSource;

    @Override
    protected void append(ILoggingEvent eventObject) {
        Document document = new Document();
        document.append("lv", eventObject.getLevel().toString());
        String timestamp = new LocalDateTime(eventObject.getTimeStamp()).toString("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(timestamp);
        document.append("tm", timestamp);
        StackTraceElement stackTraceElement = eventObject.getCallerData()[0];
        document.append("clz", stackTraceElement.getClassName() + ":" + stackTraceElement.getLineNumber());
        document.append("msg", eventObject.getFormattedMessage());
        connectionSource.getMongoCollection().insertOne(document);
    }

    public void setConnectionSource(MongoDBConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

}
