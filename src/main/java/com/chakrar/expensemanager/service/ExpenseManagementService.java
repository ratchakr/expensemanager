package com.chakrar.expensemanager.service;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chakrar.expensemanager.repo.ExpenseDetail;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonMappingException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.transcoder.JacksonTransformers;

@Service
public class ExpenseManagementService {
	

	private static final Logger log = LoggerFactory.getLogger(ExpenseManagementService.class);

	
	

	
	public static List<ExpenseDetail> findAll(String user) {
		log.info(" findAll called of IMPL "+user);
		List<ExpenseDetail> expenses = new ArrayList<ExpenseDetail>();
		ExpenseDetail ed = null;
		Statement statement = select("transactionDate", "category", "transactionType", "transactionAmount", "merchant", "description").from(i("default")).where(x("username").eq(x("$username")));
		JsonObject placeholderValues = JsonObject.create().put("username", user);
		ParameterizedN1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);
		Cluster cluster = CouchbaseCluster.create("127.0.0.1");
		Bucket bucket = cluster.openBucket("default");
		JsonObject rowJson = null;
		for (N1qlQueryRow row : bucket.query(q)) {
			rowJson = row.value();
		    log.info(" RATNO rowJson === "+ rowJson);
		    try {
				ed = JacksonTransformers.MAPPER.readValue(rowJson.toString(), ExpenseDetail.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    log.info(" description = "+ ed.getDescription());
		    expenses.add(ed);
		}		
		return expenses;
	}	

}
