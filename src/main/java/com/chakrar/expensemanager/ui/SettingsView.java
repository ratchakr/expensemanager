package com.chakrar.expensemanager.ui;

import com.chakrar.expensemanager.auth.CurrentUser;
import com.chakrar.expensemanager.auth.SignUpForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SettingsView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "Settings";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SignUpForm signUpForm = new SignUpForm(null, this);

	public SettingsView () {
		Panel panel = new Panel("User Details");
		panel.setSizeUndefined();
		panel.setIcon(FontAwesome.INFO);
		addComponent(panel);
		
		Label currUserLabel = new Label();
        currUserLabel.setCaption("LoggedIn User "+CurrentUser.get());
        currUserLabel.setStyleName(ValoTheme.LABEL_H3);
        
        VerticalLayout userInfo = new VerticalLayout(signUpForm);
        
        userInfo.setSpacing(true);
        userInfo.setSizeUndefined();
        userInfo.setMargin(true);
        
        signUpForm.setVisible(true);
        
        VerticalLayout main = new VerticalLayout(currUserLabel, userInfo);
        main.setSpacing(true);
        main.setMargin(true);
        
        addComponent(main);
        panel.setContent(main);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		//signUpForm.

	}

}
