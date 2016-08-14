package com.chakrar.expensemanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.chakrar.expensemanager.repo.User;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

@Service
public class UserProfileService {

	private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);
	
    /**
     * Try to log the given user in.
     */
    public static ResponseEntity<String> login(final Bucket bucket, final String username, final String password) {
        JsonDocument doc = bucket.get("user::" + username);
        logger.info(" doc received from bucket " + doc);
       
        JsonObject responseContent;
        if (doc == null) {
            responseContent = JsonObject.create().put("success", false).put("failure", "no user doc found in bucket");
        } else if(BCrypt.checkpw(password, doc.content().getString("password"))) {
        	logger.info("  ***        password      ******* =   "+ doc.content().getString("password"));
            responseContent = JsonObject.create().put("success", true).put("data", doc.content());
        } else {
            responseContent = JsonObject.empty().put("success", false).put("failure", "Bad Username or Password");
        }
        logger.info(" responseContent.toString() = "+ responseContent.toString());
        return new ResponseEntity<String>(responseContent.toString(), HttpStatus.OK);
    }
    
    /**
     * Create a user account.
     */
    public static ResponseEntity<String> signUp(final Bucket bucket, final User user) {
    	logger.info("   Inside UserProfileService.signUp()   ===       "+ user.getPassword());
        JsonObject data = JsonObject.create()
            .put("type", "user")
            .put("email", user.getUsername())
            .put("address", user.getAddress())
            .put("phone", user.getPhone())
            .put("age", user.getAge())
            .put("cardNumber", user.getCardNumber())
            .put("zipcode", user.getZipCode())
            .put("password", BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        
        JsonDocument doc = JsonDocument.create("user::" + user.getUsername(), data);

        try {
        	//logger.info(" data before insert = "+ data);
            bucket.insert(doc);
            JsonObject responseData = JsonObject.create()
                .put("success", true)
                .put("data", data);
            
            logger.info(" data after insert = "+ responseData);
            
            return new ResponseEntity<String>(responseData.toString(), HttpStatus.OK);
        } catch (Exception e) {
            JsonObject responseData = JsonObject.empty()
                .put("success", false)
                .put("failure", "There was an error creating account")
                .put("exception", e.getMessage());
             e.printStackTrace();
            return new ResponseEntity<String>(responseData.toString(), HttpStatus.OK);
        }
    }    
	
    /**
     * Create a user account.
     */
    public static ResponseEntity<String> viewSettings(final Bucket bucket, final String username, final String password) {
        JsonDocument doc = bucket.get("user::" + username);

        JsonObject responseContent;
        if (doc == null) {
            responseContent = JsonObject.create().put("success", false).put("failure", "Bad Username or Password");
        } else if(BCrypt.checkpw(password, doc.content().getString("password"))) {
            responseContent = JsonObject.create().put("success", true).put("data", doc.content());
        } else {
            responseContent = JsonObject.empty().put("success", false).put("failure", "Bad Username or Password");
        }
        return new ResponseEntity<String>(responseContent.toString(), HttpStatus.OK);
    }
    
    /**
     * Create a user account.
     */
    public static ResponseEntity<String> updateSettings(final Bucket bucket, final String username, final String password) {
        JsonObject data = JsonObject.create()
            .put("type", "user")
            .put("name", username)
            .put("password", BCrypt.hashpw(password, BCrypt.gensalt()));
        JsonDocument doc = JsonDocument.create("user::" + username, data);

        try {
            bucket.upsert(doc);
            JsonObject responseData = JsonObject.create()
                .put("success", true)
                .put("data", data);
            return new ResponseEntity<String>(responseData.toString(), HttpStatus.OK);
        } catch (Exception e) {
            JsonObject responseData = JsonObject.empty()
                .put("success", false)
                .put("failure", "There was an error creating account")
                .put("exception", e.getMessage());
            return new ResponseEntity<String>(responseData.toString(), HttpStatus.OK);
        }
    }
}
