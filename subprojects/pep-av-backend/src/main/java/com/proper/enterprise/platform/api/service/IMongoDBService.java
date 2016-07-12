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

	boolean checkJurisdiction(String userid) throws Exception;

	/**
	 * 
	 * @param useid
	 *            用户id
	 * @param operation
	 *            操作符
	 * @return 组织主键，为空返回null
	 */
	String[] getPk_orgsByUseid(String useid, String operation) throws Exception;

	void initializationData();

}
