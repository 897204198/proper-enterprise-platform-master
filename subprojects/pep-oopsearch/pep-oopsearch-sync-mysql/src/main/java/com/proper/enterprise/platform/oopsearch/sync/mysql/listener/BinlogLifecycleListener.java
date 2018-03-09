package com.proper.enterprise.platform.oopsearch.sync.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinlogLifecycleListener implements BinaryLogClient.LifecycleListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinlogLifecycleListener.class);

    @Override
    public void onConnect(BinaryLogClient client) {
        LOGGER.info("BinaryLogClient:Connected");
    }

    @Override
    public void onCommunicationFailure(BinaryLogClient client, Exception ex) {
        LOGGER.info("BinaryLogClient:Communication failed");
    }

    @Override
    public void onEventDeserializationFailure(BinaryLogClient client, Exception ex) {
        LOGGER.info("BinaryLogClient:Event deserialization failed");
    }

    @Override
    public void onDisconnect(BinaryLogClient client) {
        LOGGER.info("BinaryLogClient:Disconnected");
    }
}
