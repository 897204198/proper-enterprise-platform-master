package com.proper.enterprise.platform.avbackend.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.proper.enterprise.platform.api.service.IMongoDBService;

@Controller

@RequestMapping("/avdemo")
public class SampleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

	@Autowired
	private IMongoDBService iMongoDBService;



	public SampleController() {
		LOGGER.info("------------load SampleController-----------------");

	}

	@RequestMapping(value = "/classes/{collection}", method = RequestMethod.POST)
	@ResponseBody
	Map<String, Object> createOrQuery(@PathVariable String collection, @RequestBody String objectStr,
			HttpServletRequest request, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "*");
		String url = request.getRequestURI();
		return handler(collection, null, objectStr, url);
	}

	@RequestMapping(value = "/classes/{collection}/{objectIds}", method = RequestMethod.POST)
	@ResponseBody
	Map<String, Object> delOrUpdate(@PathVariable String collection, @PathVariable String objectIds,
			@RequestBody String objectStr, HttpServletRequest request, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "*");
		String url = request.getRequestURI();
		return handler(collection, objectIds, objectStr, url);
	}


	@RequestMapping(value = "/cloudQuery", method = RequestMethod.POST)
	@ResponseBody
	Map<String, Object> cloudQuery(@RequestBody String cqlstr, HttpServletRequest request, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "*");

		try {
			// return iMongoDBService.updateByCql(cqlstr);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	private Map<String, Object> handler(String collection, String objectId, String objectStr, String url) {
		LOGGER.info("Received payload: {}", objectStr);

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(objectStr);
			if (innerMethod(root, "DELETE")) {
				return doDelete(collection, objectId, url);
			} else if (innerMethod(root, "PUT")) {
				return doPut(root, collection, objectId, url);
			} else if (root.has("where") && innerMethod(root, "GET")) {
				return doQuery(root, collection, url);
			} else {
				return doCreate(root, collection);
			}
		} catch (IOException ioe) {
			LOGGER.error("Parse json to object error!", ioe);
		} catch (Exception ioe) {
			LOGGER.error("Parse json to object error!", ioe);
		}

		return null;
	}



	private boolean innerMethod(JsonNode node, String method) {
		return node.has("_method") && method.equals(node.get("_method").textValue());
	}

	private Map<String, Object> doDelete(String collection, String objectIds, String url) throws Exception {
		iMongoDBService.delete(collection, objectIds, url);
		return new HashMap<String, Object>();

	}

	private Map<String, Object> doPut(JsonNode root, String collection, String objectId, String url) throws Exception {
		iMongoDBService.updateById(root, collection, objectId, url);
		return new HashMap<String, Object>();
	}

	private Map<String, Object> doCreate(JsonNode root, String collection) throws Exception {
		Document doc = iMongoDBService.insertOne(root, collection);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("objectId", doc.getObjectId("_id").toHexString());
		result.put("createdAt", ISO8601Utils.format(new Date(), true));
		return result;
	}

	private Map<String, Object> doQuery(JsonNode root, String collection, String url) throws Exception {
		List<Document> docs = iMongoDBService.query(root, collection, url);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("results", docs);
		return result;
	}



}