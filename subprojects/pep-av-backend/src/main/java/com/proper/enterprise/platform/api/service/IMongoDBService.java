package com.proper.enterprise.platform.api.service;

import java.util.List;

import org.bson.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.result.UpdateResult;

public interface IMongoDBService {

	Document insertOne(JsonNode root, String collection) throws Exception;

	int delete(String collection, String objectIds) throws Exception;

	int delete(String collection, String objectIds, String url) throws Exception;

	Document deleteById(String collection, String objectIds) throws Exception;

	UpdateResult updateById(JsonNode root, String collection, String objectId) throws Exception;

	UpdateResult updateById(JsonNode root, String collection, String objectId, String url) throws Exception;

	UpdateResult updateByCql(String cql) throws Exception;

	List<Document> query(JsonNode root, String collection) throws Exception;

	List<Document> query(JsonNode root, String collection, String url) throws Exception;

	Document queryById(String id, String collection) throws Exception;



}
