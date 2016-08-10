/**
 * 
 */
package com.chakrar.expensemanager.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author eratnch
 *
 */
@ViewIndexed(designDoc = "expenseDetail")
@N1qlPrimaryIndexed
public interface ExpenseDetailsRepository extends CrudRepository<ExpenseDetail, String> {
	
	List<ExpenseDetail> findAll();
	
	List<ExpenseDetail> findByTransactionDateBetween(Date fromDate, Date toDate);
	
	List<ExpenseDetail> findByMerchant(String merchantName);
	
	List<ExpenseDetail> findByMerchantStartsWithIgnoreCase(String merchantName);

}
