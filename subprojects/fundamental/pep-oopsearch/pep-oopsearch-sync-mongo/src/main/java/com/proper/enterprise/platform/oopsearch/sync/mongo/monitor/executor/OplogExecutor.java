package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.executor;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.OplogMonitor;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.handle.NoticeHandle;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OplogExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OplogExecutor.class);

    private static final String UNRECOGNIZED_PIPELINE = "Unrecognized pipeline stage name: '$changeStream'";

    @Autowired
    OplogMonitor oplogMonitor;

    @Async
    public void execute(MongoCollection<Document> collection) {
        try (
            MongoCursor<ChangeStreamDocument<Document>> cursor = collection
                .watch()
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .iterator()) {
            while (cursor.hasNext()) {
                NoticeHandle.notice(cursor.next());
            }
        } catch (MongoCommandException e) {
            LOGGER.error("MongoCommandException can't start oplog", e);
        } catch (Exception e) {
            LOGGER.error("sync oplog error:", e);
            oplogMonitor.start(collection);
            LOGGER.info("restart oplog");
        }
    }
}
