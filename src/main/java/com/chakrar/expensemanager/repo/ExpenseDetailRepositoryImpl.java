/*package com.chakrar.expensemanager.repo;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

public class ExpenseDetailRepositoryImpl  {
	
	private static final Logger log = LoggerFactory.getLogger(ExpenseDetailRepositoryImpl.class);

	@Override
	public CouchbaseOperations getCouchbaseOperations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ExpenseDetail arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Iterable<? extends ExpenseDetail> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<ExpenseDetail> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ExpenseDetail> findAll(Iterable<String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpenseDetail findOne(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends ExpenseDetail> S save(S arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends ExpenseDetail> Iterable<S> save(Iterable<S> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ExpenseDetail> findAll(Sort arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ExpenseDetail> findAll(Pageable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseDetail> findAll(String user) {
		log.info(" findAll called of IMPL ");
		List<ExpenseDetail> expenses = new ArrayList<ExpenseDetail>();
		ExpenseDetail ed = null;
		Statement statement = select("transactionDate", "transactionType", "transactionAmount", "merchant", "description").from(i("default")).where(x("user").eq(x("$user")));
		JsonObject placeholderValues = JsonObject.create().put("user", user);
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

	@Override
	public List<ExpenseDetail> findByTransactionDateBetween(Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseDetail> findByMerchant(String merchantName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseDetail> findByMerchantStartsWithIgnoreCase(String merchantName) {
		// TODO Auto-generated method stub
		return null;
	}

}
*/