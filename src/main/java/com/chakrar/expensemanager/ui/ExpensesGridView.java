package com.chakrar.expensemanager.ui;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.util.StringUtils;
import org.vaadin.viritin.form.AbstractForm.DeleteHandler;
import org.vaadin.viritin.form.AbstractForm.ResetHandler;
import org.vaadin.viritin.form.AbstractForm.SavedHandler;

import com.chakrar.expensemanager.auth.CurrentUser;
import com.chakrar.expensemanager.repo.ExpenseDetail;
import com.chakrar.expensemanager.repo.ExpenseDetailsRepository;
//import com.chakrar.expensemanager.repo.ExpenseDetailsRepository;
import com.chakrar.expensemanager.repo.ExpenseEditor;
import com.chakrar.expensemanager.service.ExpenseManagementService;
import com.chakrar.expensemanager.service.FullTextSearchService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
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

	//private final MTable<ExpenseDetail> grid;
	
	private final Grid grid;
	
	private final TextField filter;

	private final Button addNewBtn;
	
	private final Button findBtn;
	
	
	
	//private NativeSelect fieldToSearch;

	private TextField merchantField;
	
	private TextField categoryField;
	
	private DateField fromDate;
	
	private DateField toDate;
	
	private final Button showAllBtn;
	
	private final Button advanceSearchBtn;

	static final Logger log = LoggerFactory.getLogger(ExpensesGridView.class);
    
    private SampleCrudLogic viewLogic = new SampleCrudLogic(this);
    
    
    @Autowired
    public ExpensesGridView(ExpenseDetailsRepository repo, ExpenseEditor editor, CouchbaseTemplate couchbaseTemplate) {
		this.repo = repo;
		this.couchbaseTemplate = couchbaseTemplate;
		this.editor = editor;
		this.grid = new Grid ();
		//grid.setWidth("800px");
		//grid.setHeightMode(HeightMode.ROW);
		//grid.setSizeFull();
		grid.setColumns("transactionDate", "transactionAmount", "merchant", "category", "description");
		//new MTable<>(ExpenseDetail.class).withProperties("transactionDate", "transactionAmount", "transactionType", "merchant", "category", "description").withHeight("300px");
		this.addNewBtn = new Button("Add New Expense", FontAwesome.PLUS_CIRCLE);
		this.addNewBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		this.filter = new TextField();
		this.merchantField = new TextField("Merchant");
		this.categoryField = new TextField("Category");
		
		fromDate = new DateField("From Date");
		toDate = new DateField("To Date");
		this.findBtn = new Button("Search", FontAwesome.SEARCH);
		this.advanceSearchBtn = new Button("Show/Hide Search Bar", FontAwesome.SEARCH_PLUS);
		this.showAllBtn = new Button("Clear Filter", FontAwesome.LIST);
		
		setSizeFull();
		
		final Panel searchPanel = new Panel("Filter by criteria");
		searchPanel.setSizeUndefined();
		searchPanel.setIcon(FontAwesome.SEARCH);
		addComponent(searchPanel);
		searchPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);
		
		final HorizontalLayout searchBar = new HorizontalLayout(fromDate, toDate, merchantField, categoryField);
		searchBar.setSpacing(true);
		searchBar.setSizeUndefined();
		
		HorizontalLayout searchBtns = new HorizontalLayout(findBtn, showAllBtn);
		searchBtns.setSpacing(true);
		
		VerticalLayout searchV = new VerticalLayout(searchBar, searchBtns);
		searchV.setSpacing(true);
		searchV.setMargin(true);
		searchPanel.setContent(searchV);
		searchPanel.setVisible(false);
		
		viewLogic.init();
		
		advanceSearchBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				/*final Window sub = new Window("This is a sub-window");
                sub.setModal(true);
                sub.center();
                UI.getCurrent().addWindow(sub);
                
                VerticalLayout popupContent = new VerticalLayout();
                popupContent.addComponents(fromDate, toDate, findBtn, showAllBtn);
                
                final PopupView popup = new PopupView("Open the search screen", popupContent);
                popup.setSizeFull();
                sub.setContent(popup);*/
                
				//searchBar.setVisible(true);
				searchPanel.setVisible(!searchPanel.isVisible());
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
		
		Label currUserLabel = new Label();
        currUserLabel.setCaption("LoggedIn User "+CurrentUser.get());
        
		
		CssLayout filtering = new CssLayout();
		filtering.addComponents(filter, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		HorizontalLayout topBar = new HorizontalLayout(filtering, addNewBtn, this.advanceSearchBtn); 
		topBar.setSpacing(true);
		//topBar.setMargin(true);
		topBar.setSizeUndefined();
		
        VerticalLayout v = new VerticalLayout(topBar);
        
        v.setSpacing(true);
		v.setMargin(true);
		
		HorizontalLayout main = new HorizontalLayout(grid, editor);
		main.setSpacing(true);
		main.setMargin(true);
		main.setSizeFull();
		grid.setSizeFull();
		
		
		
		addComponents(v, searchPanel, main);
		
		// Initialize listing
		listExpenses(null);		
		handleGridActions();
       
        
        
    }

	private void handleGridActions() {
		/*grid.addMValueChangeListener(new MValueChangeListener<ExpenseDetail>() {
			@Override
			public void valueChange(MValueChangeEvent<ExpenseDetail> e) {
				if (e.getValue() == null) {
					editor.setVisible(false);
				} else {
					editor.setEntity(e.getValue());
				}
			}
		});*/

		findBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				
				if (null != fromDate.getValue() && null != toDate.getValue() && null == merchantField.getValue() && null == categoryField.getValue()) {
					listExpensesByDateRange (fromDate.getValue(), toDate.getValue());
				} else if (null == fromDate.getValue() && null == toDate.getValue() && null != merchantField.getValue() && null == categoryField.getValue()) {
					grid.setContainerDataSource(new BeanItemContainer<>(ExpenseDetail.class, repo.findByMerchant(merchantField.getValue())));
				} else if (null == fromDate.getValue() && null == toDate.getValue() && null == merchantField.getValue() && null != categoryField.getValue()) {
					grid.setContainerDataSource(new BeanItemContainer<>(ExpenseDetail.class, repo.findByCategory(categoryField.getValue())));
				} else {
					grid.setContainerDataSource(new BeanItemContainer<>(ExpenseDetail.class, 
							ExpenseManagementService.findByCriteria(CurrentUser.get(), fromDate.getValue(), toDate.getValue(), merchantField.getValue(), categoryField.getValue())));
					
				}
				
				
				//sub.close();
			}

		});		
		
		
		showAllBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				fromDate.clear();
				toDate.clear();
				listExpenses(null);
				//sub.close();
			}
		});		
		
		grid.addSelectionListener(new SelectionListener() {
			
			@Override
			public void select(SelectionEvent event) {
				log.info("   grid element selected   ");
				if (event.getSelected().isEmpty()) {
					editor.setVisible(false);
				} else {
					editor.setEntity((ExpenseDetail)event.getSelected().iterator().next());
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
				ed.setUsername(CurrentUser.get());
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



		filter.setInputPrompt("Filter by text");
		
		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent e) {
				listExpenses(e.getText());
			}
		});	
		
		
		

		

	}

	private void listExpenses(String text) {
		log.info("*********     listExpenses called with       ************ "+ text);
		if (StringUtils.isEmpty(text)) {
			//grid.setBeans(repo.findAll());
			//grid.setContainerDataSource(new BeanItemContainer<>(ExpenseDetail.class, repo.findAll(CurrentUser.get())));
			grid.setContainerDataSource(new BeanItemContainer<>(ExpenseDetail.class, ExpenseManagementService.findAll(CurrentUser.get())));
		} else {
			//grid.setBeans(repo.findByMerchantStartsWithIgnoreCase(text));
			log.info("*********      searchText is       ************ "+ text);
			try {
				//grid.setBeans(FullTextSearchService.findByText(text));
				grid.setContainerDataSource(new BeanItemContainer<>(ExpenseDetail.class, FullTextSearchService.findByText(text)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
        
	}
	
	private void listExpensesByDateRange(Date from, Date to) {
		log.info("*********      from       ************ "+ from);
		log.info("*********      to       ************ "+ to);
		grid.setContainerDataSource(new BeanItemContainer<>(ExpenseDetail.class, repo.findByTransactionDateBetween(from, to)));
		
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
