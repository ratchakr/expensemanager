package com.chakrar.expensemanager.ui;

import org.springframework.data.couchbase.core.CouchbaseTemplate;

import com.chakrar.expensemanager.repo.ExpenseDetailsRepository;
import com.chakrar.expensemanager.repo.ExpenseEditor;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
@UIScope
public class MainScreen extends HorizontalLayout {
    private Menu menu;

    public MainScreen(ExpenseManagerUI ui, ExpenseDetailsRepository repo, ExpenseEditor editor, CouchbaseTemplate template) {

        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(navigator);
        menu.addView(new ExpensesGridView(repo, editor, template), ExpensesGridView.VIEW_NAME,
                ExpensesGridView.VIEW_NAME, FontAwesome.EDIT);
        menu.addView(new AboutView(), AboutView.VIEW_NAME, AboutView.VIEW_NAME,
                FontAwesome.INFO_CIRCLE);
        menu.addView(new SettingsView(), SettingsView.VIEW_NAME, SettingsView.VIEW_NAME,
                FontAwesome.STAR);

        navigator.addViewChangeListener(viewChangeListener);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
