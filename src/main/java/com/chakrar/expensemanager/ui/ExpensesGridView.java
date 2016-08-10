package com.chakrar.expensemanager.ui;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.util.StringUtils;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.form.AbstractForm.DeleteHandler;
import org.vaadin.viritin.form.AbstractForm.ResetHandler;
import org.vaadin.viritin.form.AbstractForm.SavedHandler;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.chakrar.expensemanager.repo.ExpenseDetail;
import com.chakrar.expensemanager.repo.ExpenseDetailsRepository;
import com.chakrar.expensemanager.repo.ExpenseEditor;
import com.chakrar.expensemanager.service.FullTextSearchService;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link SampleCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@SpringUI
public class ExpensesGridView extends CssLayout implements View {


    public static final String VIEW_NAME = "Expenses";
    
	private final ExpenseDetailsRepository repo;
	
	private final CouchbaseTemplate couchbaseTemplate;
	
	//private final FullTextSearchService fts;

	private final ExpenseEditor editor;

	private final MTable<ExpenseDetail> grid;
	
	private final TextField filter;

	private final Button addNewBtn;
	
	private final Button findBtn;
	
	
	
	//private NativeSelect fieldToSearch;

	//private TextField searchText;
	
	private DateField fromDate;
	
	private DateField toDate;
	
	private final Button showAllBtn;

	static final Logger log = LoggerFactory.getLogger(ExpensesGridView.class);
    
    private SampleCrudLogic viewLogic = new SampleCrudLogic(this);

    @Autowired
    public ExpensesGridView(ExpenseDetailsRepository repo, ExpenseEditor editor, CouchbaseTemplate couchbaseTemplate) {
		this.repo = repo;
		this.couchbaseTemplate = couchbaseTemplate;
		this.editor = editor;
		this.grid = new MTable<>(ExpenseDetail.class).withProperties("transactionDate", "transactionAmount", "transactionType", "merchant", "category", "description").withHeight("300px");
		this.addNewBtn = new Button("Add New Expense", FontAwesome.PLUS_CIRCLE);
		this.filter = new TextField();
		fromDate = new DateField("Transaction From Date");
		toDate = new DateField("To Date");
		
		this.findBtn = new Button("Search", FontAwesome.SEARCH);
		this.showAllBtn = new Button("Show All", FontAwesome.LIST);
		//fts = new FullTextSearchService();
        setSizeFull();


		
		// Initialize listing
		listExpenses(null);

		handleGridActions();
		
        viewLogic.init();
    }

	private void handleGridActions() {
		grid.addMValueChangeListener(new MValueChangeListener<ExpenseDetail>() {
			@Override
			public void valueChange(MValueChangeEvent<ExpenseDetail> e) {
				if (e.getValue() == null) {
					editor.setVisible(false);
				} else {
					editor.setEntity(e.getValue());
				}
			}
		});

		// Instantiate and edit new Expense the new button is clicked
		addNewBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				editor.setEntity(new ExpenseDetail("", new Date(), new Double(0.0), "", "", ""));
			}
		});

		// Listen changes made by the editor, refresh data from backend
		editor.setSavedHandler(new SavedHandler<ExpenseDetail>() {
			@Override
			public void onSave(ExpenseDetail ed) {
				repo.save(ed);
				listExpenses(null);
				editor.setVisible(false);
			}
		});

		editor.setResetHandler(new ResetHandler<ExpenseDetail>() {
			@Override
			public void onReset(ExpenseDetail ed) {
				editor.setVisible(false);
				listExpenses(null);
			}
		});

		editor.setDeleteHandler(new DeleteHandler<ExpenseDetail>() {
			@Override
			public void onDelete(ExpenseDetail ed) {
				repo.delete(ed);
				listExpenses(null);
			}
		});



		findBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				
				listExpensesByDateRange (fromDate.getValue(), toDate.getValue());
			}

		});		
		
		
		showAllBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				fromDate.clear();
				toDate.clear();
				listExpenses(null);
			}
		});

		filter.setInputPrompt("Filter by merchant");
		
		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent e) {
				listExpenses(e.getText());
			}
		});	
		
		Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
		clearFilterTextBtn.setDescription("Clear the current filter");
		clearFilterTextBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
			  filter.clear();
			  listExpenses(null);
			}
		});		
		
		CssLayout filtering = new CssLayout();
		filtering.addComponents(filter, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		HorizontalLayout actions = new HorizontalLayout(filtering, addNewBtn); 
		HorizontalLayout dateFields = new HorizontalLayout(fromDate, toDate);
		
		//HorizontalLayout searchLayout = new HorizontalLayout(findBtn, showAllBtn);
		
		
		VerticalLayout mainLayout = new MVerticalLayout(actions, grid, editor, dateFields);
		//VerticalLayout mainLayout = new MVerticalLayout(actions, grid, editor, dateFields, searchLayout);
		addComponent(mainLayout);
		
		actions.setSpacing(true);
		dateFields.setSpacing(true);
		//searchLayout.setSpacing(true);

		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);		
		
		// Initialize listing
		listExpenses(null);
	}

	private void listExpenses(String text) {
		log.info("*********     listExpenses called with       ************ "+ text);
		if (StringUtils.isEmpty(text)) {
			grid.setBeans(repo.findAll());
		} else {
			//grid.setBeans(repo.findByMerchantStartsWithIgnoreCase(text));
			log.info("*********      searchText is       ************ "+ text);
			try {
				grid.setBeans(FullTextSearchService.findByText(text));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
        
	}
	
	private void listExpensesByDateRange(Date from, Date to) {
		log.info("*********      from       ************ "+ from);
		log.info("*********      to       ************ "+ to);
		grid.setBeans(repo.findByTransactionDateBetween(from, to));
		
	}
    


    @Override
    public void enter(ViewChangeEvent event) {
        //viewLogic.enter(event.getParameters());
    }

    public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    public void setNewProductEnabled(boolean enabled) {
        //newProduct.setEnabled(enabled);
    }

    public void clearSelection() {
        //grid.getSelectionModel().reset();
    }

 /*   public void selectRow(Product row) {
        //((SelectionModel.Single) grid.getSelectionModel()).select(row);
    }

    public Product getSelectedRow() {
        //return grid.getSelectedRow();
    	return null;
    }

    public void editProduct(Product product) {
        if (product != null) {
            //form.addStyleName("visible");
            //form.setEnabled(true);
        } else {
            //form.removeStyleName("visible");
            //form.setEnabled(false);
        }
        //form.editProduct(product);
    }

    public void showProducts(Collection<Product> products) {
        //grid.setProducts(products);
    }

    public void refreshProduct(Product product) {
        //grid.refresh(product);
        //grid.scrollTo(product);
    }

    public void removeProduct(Product product) {
        //grid.remove(product);
    }
*/
}
