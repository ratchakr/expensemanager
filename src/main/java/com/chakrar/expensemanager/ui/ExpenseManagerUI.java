package com.chakrar.expensemanager.ui;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import com.chakrar.expensemanager.auth.AccessControl;
import com.chakrar.expensemanager.auth.BasicAccessControl;
import com.chakrar.expensemanager.auth.LoginScreen;
import com.chakrar.expensemanager.auth.LoginScreen.LoginListener;
import com.chakrar.expensemanager.repo.ExpenseDetailsRepository;
import com.chakrar.expensemanager.repo.ExpenseEditor;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("expensesmanagertheme")
@SpringUI
public class ExpenseManagerUI extends UI {

	private final ExpenseDetailsRepository repo;

	private final ExpenseEditor editor;
	
	private CouchbaseTemplate template;
	
    private AccessControl accessControl = new BasicAccessControl();

    @Autowired
    public ExpenseManagerUI(ExpenseDetailsRepository repo, ExpenseEditor editor, CouchbaseTemplate couchbaseTemplate) {
    	this.repo = repo;
    	this.editor = editor;
    	this.template = couchbaseTemplate;
    }
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("Expense Manager");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, new LoginListener() {
                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(ExpenseManagerUI.this, repo, editor, this.template));
        getNavigator().navigateTo(getNavigator().getState());
    }

    public static ExpenseManagerUI get() {
        return (ExpenseManagerUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    @WebServlet(urlPatterns = "/*", name = "ExpensesManagerUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ExpenseManagerUI.class, productionMode = false)
    public static class ExpensesManagerUIServlet extends VaadinServlet {
    }
}
