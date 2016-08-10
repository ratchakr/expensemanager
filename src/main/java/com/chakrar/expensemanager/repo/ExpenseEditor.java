/**
 * 
 */
package com.chakrar.expensemanager.repo;

import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author eratnch
 *
 */
@SpringComponent
@UIScope
public class ExpenseEditor extends AbstractForm<ExpenseDetail> {
	
	public ExpenseEditor() {
        setVisible(false);
    }

	
    //Fields to edit properties in CustomerExpenseDetail entity 
    TextField transactionType = new TextField("Type of Transaction");
    
    TextField merchant = new TextField("Merchant Name");
	
    DateField transactionDate = new DateField("Transaction Date");
    
    TextField transactionAmount = new TextField("Amount");
    
    TextArea description = new TextArea("Description");
    
    TextField category = new TextField("Category");
    
    

	@Override
	protected Component createContent() {
		transactionDate.setDateFormat("EEE, MMM d, ''yyyy");
		return new MFormLayout(transactionDate, transactionAmount, merchant, transactionType, description, category, getToolbar());
	}

}
