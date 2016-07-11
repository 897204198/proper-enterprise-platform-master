package com.proper.enterprise.platform.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.proper.enterprise.platform.api.service.IMongoDBService;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MongoDBServiceImpl implements IMongoDBService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBServiceImpl.class);

	private MongoDatabase database;

	private String userTable = "user";

    @Autowired
    private MongoClient mongoClient;


	public MongoDBServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public Object getConnection() throws Exception {
		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase(ConfCenter.get("mongodb.database"));
		System.out.println("Connect to database successfully");
		if (mongoDatabase == null) {
			throw new Exception("数据库连接获取失败，请检查其对应的数据源配置是否正确。");
		}

		return mongoDatabase;

	}

	private MongoDatabase getDatabase() {

		try {
			if (database != null) {
				return database;
			}
			database = (MongoDatabase) getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return database;
	}

	// --------------------------------------增方法--------------------------------------
	@Override
	public Document insertOne(JsonNode root, String collection) {
		// TODO Auto-generated method stub
		MongoCollection<Document> col = getDatabase().getCollection(collection);
		Document doc = new Document();
		doc = doc.parse(root.toString());
		col.insertOne(doc);
		return doc;
	}

	// --------------------------------------删方法--------------------------------------
	@Override
	public int delete(String collection, String objectIds) {
		return delete(collection, objectIds, null);
	}

	@Override
	public int delete(String collection, String objectIds, String url) {
		// TODO Auto-generated method stub

		LOGGER.info("In do DELETE method");
		MongoCollection<Document> col = getDatabase().getCollection(collection);
		for (String objectId : objectIds.split(",")) {
			col.findOneAndDelete(Filters.eq("_id", new ObjectId(objectId)));
		}
		return objectIds.split(",").length;
	}

	public Document deleteById(String collection, String objectIds) throws Exception {
		// TODO Auto-generated method stub

		LOGGER.info("In do DELETE method");
		MongoCollection<Document> col = getDatabase().getCollection(collection);
		Document doc = null;
		for (String objectId : objectIds.split(",")) {
			doc = col.findOneAndDelete(Filters.eq("_id", objectId));
		}
		return doc;
	}

	// --------------------------------------改方法--------------------------------------
	@Override
	public UpdateResult updateById(JsonNode root, String collection, String objectId) {
		return updateById(root, collection, objectId, null);
	}

	@Override
	public UpdateResult updateById(JsonNode root, String collection, String objectId, String url) {
		LOGGER.info("In do PUT method");
		MongoCollection<Document> col = getDatabase().getCollection(collection);
		Iterator<String> iter = root.fieldNames();
		List<Bson> list = new ArrayList<Bson>();
		while (iter.hasNext()) {
			String field = iter.next();
			if (field.startsWith("_")) {
				continue;
			}
			if (root.get(field).isTextual()) {
				LOGGER.info("Set {} to {}", field, root.get(field).textValue());
				list.add(Updates.set(field, root.get(field).textValue()));
			} else if (root.get(field).has("__op") && "Delete".equals(root.get(field).get("__op").textValue())) {
				LOGGER.info("Unset {}", field);
				list.add(Updates.unset(field));
			}
		}

		return col.updateOne(Filters.eq("_id", new ObjectId(objectId)), Updates.combine(list));
	}

	@Override
	public UpdateResult updateByCql(String cql) {
		// TODO Auto-generated method stub
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(cql);

			Iterator<String> iter = root.fieldNames();
			// List<Bson> list = new ArrayList<Bson>();
			while (iter.hasNext()) {
				String field = iter.next();
				root.get(field);
			}
			root.fieldNames();
			root.fields();
			root.get("cql");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Document> query(JsonNode root, String collection) {
		return query(root, collection, null);
	}

	@Override
	public List<Document> query(JsonNode root, String collection, String url) {
		// try {
		//// User loginuser = userService.getCurrentUser();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// throw new Exception(e.getMessage());
		// }

		JsonNode whereNode = root.get("where");
		int limit = root.has("limit") ? root.get("limit").asInt() : -1;
		String order = root.has("order") ? root.get("order").asText() : "";
		LOGGER.info("Where node: {}", whereNode);
		LOGGER.info("limit is {}", limit);
		LOGGER.info("orders are {}", order);

		MongoCollection<Document> col = getDatabase().getCollection(collection);
		String wherestr = whereNode.toString();
		if (wherestr.indexOf("objectId") > 0) {
			String objectId = whereNode.get("objectId").toString();
			wherestr = wherestr.replaceAll("objectId", "_id");
			wherestr = wherestr.replaceAll(objectId, "ObjectId(" + objectId + ")");
		}

		FindIterable<Document> findIter = col.find(Document.parse(wherestr));

		if (limit > 0) {
			findIter.limit(limit);
		}
		if (!StringUtils.isEmpty(order)) {
			String[] orders = order.split(",");
			for (String o : orders) {
				if (o.startsWith("-")) {
					findIter.sort(Sorts.descending(o.substring(1)));
				} else {
					findIter.sort(Sorts.ascending(o));
				}
			}
		}
		Iterator<Document> docsIter = findIter.iterator();
		List<Document> docs = new ArrayList<Document>();
		Document doc;
		while (docsIter.hasNext()) {
			doc = docsIter.next();
			doc.put("objectId", doc.getObjectId("_id").toHexString());
			doc.remove("_id");
			docs.add(doc);
		}
		return docs;
	}

	@Override
	public Document queryById(String id, String collection) {

		MongoCollection<Document> col = getDatabase().getCollection(collection);
		// MongoCollection<Document> collection = db.getCollection(table);
		BasicDBObject query = new BasicDBObject("_id", id);
		FindIterable<Document> iterable = col.find(query);

		Iterator<Document> docsIter = iterable.iterator();
		Document doc = null;
		while (docsIter.hasNext()) {
			doc = docsIter.next();
		}
		return doc;
	}



}
