package com.chakrar.expensemanager.service;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.chakrar.expensemanager.repo.ExpenseDetail;

@Component
@ComponentScan
public interface FullTextSearch {

	public List<ExpenseDetail> findByText(String searchText) throws Exception;
	
}
