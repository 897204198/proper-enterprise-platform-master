package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.executor;

import com.mongodb.*;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.config.MongoMonitorClient;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.handle.NoticeHandle;
import org.bson.types.BSONTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class OplogExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OplogExecutor.class);


    private MongoClient client;
    private BSONTimestamp lastTimeStamp;
    private DBCollection shardTimeCollection;
    private MongoMonitorClient mongoMonitorClient;

    public OplogExecutor(Entry<String, MongoClient> client, DB timeDB, MongoMonitorClient mongoMonitorClient) {
        this.client = client.getValue();
        this.mongoMonitorClient = mongoMonitorClient;
        shardTimeCollection = timeDB.getCollection(client.getKey());
        DBObject findOne = shardTimeCollection.findOne();
        if (findOne != null) {
            lastTimeStamp = (BSONTimestamp) findOne.get("ts");
        }
    }

    public void execute() {
        DBCollection fromCollection = client.getDB("local").getCollection("oplog.rs");
        DBCursor opCursor = fromCollection.find(getFilter())
            .sort(new BasicDBObject("$natural", 1))
            .addOption(Bytes.QUERYOPTION_TAILABLE)
            .addOption(Bytes.QUERYOPTION_AWAITDATA)
            .addOption(Bytes.QUERYOPTION_NOTIMEOUT);
        if (opCursor.hasNext()) {
            DBObject nextOp = opCursor.next();
            lastTimeStamp = (BSONTimestamp) nextOp.get("ts");
            shardTimeCollection.update(new BasicDBObject(), new BasicDBObject("$set", new BasicDBObject("ts",
                lastTimeStamp)), true, true, WriteConcern.SAFE);
            try {
                NoticeHandle.notice(nextOp);
            } catch (Exception e) {
                LOGGER.error("notice exception", e);
            }
        }
    }

    private DBObject getFilter() {
        List<DBObject> and = new ArrayList<>();
        DBObject timeQuery = getTimeQuery();
        and.add(timeQuery);
        String[] ignoreCollections = mongoMonitorClient.getIgnoreCollections();
        if (null != ignoreCollections) {
            for (String ignoreCollection : ignoreCollections) {
                and.add(new BasicDBObject("ns", new BasicDBObject("$ne", mongoMonitorClient
                    .getDatabase() + "." + ignoreCollection)));
            }
        }
        and.add(new BasicDBObject("ns", new BasicDBObject("$not", Pattern.compile(MongoMonitorClient.TIME_DB_NAME))));
        BasicDBObject filter = new BasicDBObject();
        filter.put("$and", and);
        return filter;
    }

    private DBObject getTimeQuery() {
        return lastTimeStamp == null ? new BasicDBObject() : new BasicDBObject("ts", new BasicDBObject("$gt", lastTimeStamp));
    }


}
