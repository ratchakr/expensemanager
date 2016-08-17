package com.chakrar.expensemanager.auth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chakrar.expensemanager.repo.User;
import com.chakrar.expensemanager.service.UserProfileService;
import com.chakrar.expensemanager.ui.ExpenseManagerUI;
import com.chakrar.expensemanager.ui.ExpensesGridView;
import com.chakrar.expensemanager.ui.SettingsView;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonMappingException;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class SignUpForm extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private TextField username = new TextField ("username");
    
    private PasswordField password = new PasswordField ("password");
    
    private TextField address = new TextField ("address");
    
    private TextField phone = new TextField ("phone");
	
	private TextField age = new TextField ("age");
	
	private TextField cardNumber = new TextField ("card number");
	
	private TextField zipcode = new TextField ("zipcode");
	
	private Button save = new Button ("Save", FontAwesome.SAVE);
	
	private User user;
	
	
	private Button cancel = new Button ("Cancel", FontAwesome.EXCLAMATION_CIRCLE);

    private LoginScreen loginScreen;
    
    private static final Logger logger = LoggerFactory.getLogger(SignUpForm.class);

	public SignUpForm(LoginScreen login, SettingsView view) {
		this.loginScreen = login;
		setSizeUndefined();
		save.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		cancel.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout buttons = new HorizontalLayout(save, cancel);
		buttons.setSpacing(true);

		addComponents(username, password, address, phone, age, cardNumber, zipcode, buttons);
		if (!CurrentUser.get().isEmpty()) {
			try {
				username.setEnabled(false);
				password.setVisible(false);
				user = getCurrentUserData();
				logger.info(" address  = "+ user.getAddress());
				initializeFormValues(user);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
		if (null == user)
			user = new User ();
		save.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				user.setUsername(username.getValue());
				//user.setPassword(password.getValue());
				user.setAddress(address.getValue());
				user.setPhone(phone.getValue());
				user.setCardNumber(cardNumber.getValue());
				user.setAge(Integer.parseInt(age.getValue()));
				user.setZipCode(Integer.parseInt(zipcode.getValue()));
				if(CurrentUser.get().isEmpty()) {
					loginScreen.register(user);	
				} else {
					try {
						UserProfileService.saveUserSettings(user);
						showNotification(new Notification("Saved Data",
			                    "User Settings Saved Successfully.",
			                    Notification.Type.HUMANIZED_MESSAGE));
			            //navigateToMainView();
					} catch (JsonParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JsonMappingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				
			}



		});
	}

	/**
	 * 
	 */
	private void navigateToMainView() {
        Page page = ExpenseManagerUI.get().getPage();
        page.setUriFragment("!" + ExpensesGridView.VIEW_NAME + "/"
                + "", false);
		
	}
	
	
	
    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }
	
	private void initializeFormValues(User user) {
		username.setValue(user.getEmail());
		address.setValue(user.getAddress());
		phone.setValue(user.getPhone());
		age.setValue(String.valueOf(user.getAge()));
		zipcode.setValue(String.valueOf(user.getZipcode()));
		cardNumber.setValue(user.getCardNumber());
	}



	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private User getCurrentUserData() throws Exception {
        return UserProfileService.getUserSettings(CurrentUser.get());
	}

	public SignUpForm(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the username
	 */
	public TextField getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(TextField username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public PasswordField getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(PasswordField password) {
		this.password = password;
	}

	/**
	 * @return the address
	 */
	public TextField getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(TextField address) {
		this.address = address;
	}

	/**
	 * @return the phone
	 */
	public TextField getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(TextField phone) {
		this.phone = phone;
	}

	/**
	 * @return the age
	 */
	public TextField getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(TextField age) {
		this.age = age;
	}

	/**
	 * @return the cardNumber
	 */
	public TextField getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(TextField cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the zipCode
	 */
	public TextField getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipcode(TextField zipCode) {
		this.zipcode = zipCode;
	}

}
