package com.chakrar.expensemanager.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.Version;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class SettingsView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "Settings";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SettingsView () {
	    CustomLayout settingsContent = new CustomLayout("settingsview");
	    settingsContent.setStyleName("settings-content");

	    // you can add Vaadin components in predefined slots in the custom
	    // layout
	    settingsContent.addComponent(
	            new Label(FontAwesome.INFO_CIRCLE.getHtml() + " User Settings ", ContentMode.HTML), "settings");

	    setSizeFull();
	    setStyleName("settings-view");
	    addComponent(settingsContent);
	    setComponentAlignment(settingsContent, Alignment.MIDDLE_CENTER);		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
