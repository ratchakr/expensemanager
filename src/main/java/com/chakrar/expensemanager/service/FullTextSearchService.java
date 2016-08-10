/**
 * 
 */
package com.chakrar.expensemanager.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chakrar.expensemanager.repo.ExpenseDetail;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.MatchQuery;
import com.couchbase.client.java.search.query.SearchQuery;
import com.couchbase.client.java.transcoder.JacksonTransformers;

/**
 * @author eratnch
 *
 */
@Service
@Component
@ComponentScan
public class FullTextSearchService {
	
	private static final Logger log = LoggerFactory.getLogger(FullTextSearchService.class);
	
	
	public static List<ExpenseDetail> findByText(String searchText) throws Exception {
		List<ExpenseDetail> expenses = new ArrayList<ExpenseDetail>();
		ExpenseDetail ed = null;
		log.info("Inside someCustomMethod ");
		Cluster cluster = CouchbaseCluster.create("127.0.0.1");
		Bucket bucket = cluster.openBucket("default");
		SearchQuery ftq = MatchQuery.on("fts_expense_manager").match(searchText).build();
		SearchQueryResult result = bucket.query(ftq);
		log.info("totalHits: " + result.totalHits());
		JsonObject rowJson = null;
		for (SearchQueryRow row : result) {
		    rowJson = bucket.get(row.id()).content();
		    log.info(" RATNO rowJson === "+ rowJson);
		    ed = JacksonTransformers.MAPPER.readValue(rowJson.toString(), ExpenseDetail.class);
		    log.info(" description = "+ ed.getDescription());
		    expenses.add(ed);
		}
		return expenses;
	}

}
