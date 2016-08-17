package com.chakrar.expensemanager.ui;

import com.chakrar.expensemanager.auth.SignUpForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalSplitPanel;
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
/*	    CustomLayout settingsContent = new CustomLayout("settingsview");
	    settingsContent.setStyleName("settings-content");

	    // you can add Vaadin components in predefined slots in the custom
	    // layout
	    settingsContent.addComponent(
	            new Label(FontAwesome.INFO_CIRCLE.getHtml() + " User Settings ", ContentMode.HTML), "settings");

	    setSizeFull();
	    setStyleName("settings-view");
	    addComponent(settingsContent);
	    setComponentAlignment(settingsContent, Alignment.MIDDLE_CENTER);*/	
	    
		Panel panel = new Panel("Split Panels Inside This Panel");
		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		
		Label welcome = new Label("View/Update User Settings");
		welcome.setStyleName(ValoTheme.LABEL_COLORED);
		hsplit.setFirstComponent(welcome);
		
		
		
        VerticalLayout userInfo = new VerticalLayout(signUpForm);
        
        userInfo.setSpacing(true);
        userInfo.setSizeFull();
        
        //addComponent(centeringLayout);
        
        signUpForm.setVisible(true);
        hsplit.setSecondComponent(userInfo);
        
        panel.setContent(hsplit);
        addComponent(panel);
     /*   addComponent(userInfo);	
        setComponentAlignment(userInfo, Alignment.TOP_RIGHT);*/
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		//signUpForm.

	}

}
