package com.chakrar.expensemanager.auth;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.chakrar.expensemanager.service.UserProfileService;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * UI content when the user is not logged in yet.
 */
@UIScope
@SpringUI
public class LoginScreen extends CssLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LoginScreen.class);
	
	private Bucket bucket;

    private TextField username;
    private PasswordField password;
    //private Button login;
    private Button forgotPassword;
    private LoginListener loginListener;
    //private AccessControl accessControl;

    public LoginScreen(AccessControl accessControl, LoginListener loginListener) {
        this.loginListener = loginListener;
        //this.accessControl = accessControl;
        buildUI();
        username.focus();
        this.bucket = CouchbaseCluster.create("127.0.0.1").openBucket("default");
    }

    private void buildUI() {
        addStyleName("login-screen");

        // login form, centered in the available part of the screen
        Component loginForm = buildLoginFormNew();

        // layout to center login form when there is sufficient screen space
        // - see the theme for how this is made responsive for various screen
        // sizes
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent(loginForm);
        centeringLayout.setComponentAlignment(loginForm,
                Alignment.MIDDLE_CENTER);

        addComponent(centeringLayout);
    }


    private void login() {
    	logger.info("    Inside login method of LoginScreen...   bucket = "+ bucket + " username = "+ username.getValue() + " password = "+ password.getValue());
    	
    	ResponseEntity<String> response = UserProfileService.login(bucket, username.getValue(), password.getValue());
    	
    	if (response.getStatusCode() == HttpStatus.OK) {
    		JsonObject json = JsonObject.fromJson(response.getBody());
    		
    		if (Boolean.TRUE == json.get("success")) {
    			logger.info("    Login Success      ");
    			loginListener.loginSuccessful();
    		}
    		
    	} else {
    		logger.info("    Login Failed      ");
            showNotification(new Notification("Login failed",
                    "Please check your username and password and try again.",
                    Notification.Type.HUMANIZED_MESSAGE));
            username.focus();
        }
    	
/*        if (accessControl.signIn(username.getValue(), password.getValue())) {
            loginListener.loginSuccessful();
        } else {
            showNotification(new Notification("Login failed",
                    "Please check your username and password and try again.",
                    Notification.Type.HUMANIZED_MESSAGE));
            username.focus();
        }
*/    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

    public interface LoginListener extends Serializable {
        void loginSuccessful();
    }
    
    private Component buildLoginFormNew() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        loginPanel.setMargin(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        //loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(new CheckBox("Remember me", true));
        return loginPanel;
    }  
    
    private Component buildFields() {
        final VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username = new TextField("Username");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        
        final Button forgotPassword = new Button("Forgot Password ?");
        forgotPassword.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                showNotification(new Notification("Hint: Try anything"));
            }
        });
        forgotPassword.addStyleName(ValoTheme.BUTTON_LINK);

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        signin.setIcon(FontAwesome.SIGN_IN);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.setDisableOnClick(true);
        signin.focus();
        
        final Button signup = new Button("Sign Up");
        signup.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signup.setIcon(FontAwesome.REGISTERED);
        signup.setClickShortcut(KeyCode.R);
        signup.focus();

        fields.addComponents(username, password, signin, forgotPassword, signup);
        fields.setSpacing(true);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	Notification.show("Thanks " + username.getValue() + " for signing in");
            	try {
                    login();
                } finally {
                	signin.setEnabled(true);
                }
            }
        });
        return fields;
    }
    
}
