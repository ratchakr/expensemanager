package com.chakrar.expensemanager.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.chakrar.expensemanager.repo.User;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParseException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonMappingException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.transcoder.JacksonTransformers;

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
            responseContent = JsonObject.empty().put("success", true).put("failure", "Bad Username or Password");
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
            .put("zipcode", user.getZipcode())
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
    private static ResponseEntity<String> viewSettings(final Bucket bucket, final String username) {
        JsonDocument doc = bucket.get("user::" + username);

        JsonObject responseContent;
        if (doc == null) {
            responseContent = JsonObject.create().put("success", false).put("failure", "No document with username "+ username);
        } 
        responseContent = JsonObject.create().put("success", true).put("data", doc.content());
        return new ResponseEntity<String>(responseContent.toString(), HttpStatus.OK);
    }
    
    /**
     * 
     * @param user
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
	public static User getUserSettings(String username) throws JsonParseException, JsonMappingException, IOException {
		logger.info(" getUserSettings called  "+username);
		User user = null;
		Cluster cluster = CouchbaseCluster.create("127.0.0.1");
		Bucket bucket = cluster.openBucket("default");

		ResponseEntity<String> response = viewSettings(bucket, username);
		
		if (response.getStatusCode() == HttpStatus.OK) {
    		JsonObject retJson = JsonObject.fromJson(response.getBody());
    		JsonObject data = (JsonObject) retJson.get("data");
    		if (Boolean.TRUE == retJson.get("success")) {
    			logger.info("    User info retrieved successfully      "+data);
    			user = JacksonTransformers.MAPPER.readValue(data.toString(), User.class);
    		}
    	} 		
		return user;
	}	
	
	public static void saveUserSettings(User user) throws JsonParseException, JsonMappingException, IOException {
		logger.info(" saveUserSettings called  with "+user.getEmail());
		Cluster cluster = CouchbaseCluster.create("127.0.0.1");
		Bucket bucket = cluster.openBucket("default");

		JsonDocument existingDoc = bucket.get("user::" + user.getEmail());
		String password = existingDoc.content().getString("password");
		if (null != password) 
			user.setPassword(password);
		
		ResponseEntity<String> response = saveSettings(bucket, user);
		
		if (response.getStatusCode() == HttpStatus.OK) {
    		JsonObject retJson = JsonObject.fromJson(response.getBody());
    		JsonObject data = (JsonObject) retJson.get("data");
    		if (Boolean.TRUE == retJson.get("success")) {
    			logger.info("    User info saved successfully      "+data);
    			user = JacksonTransformers.MAPPER.readValue(data.toString(), User.class);
    		}
    	} 		
	}
    
    /**
     * Create a user account.
     */
    private static ResponseEntity<String> saveSettings(final Bucket bucket, User user) {
        JsonObject data = JsonObject.create()
                .put("type", "user")
                .put("email", user.getUsername())
                .put("address", user.getAddress())
                .put("phone", user.getPhone())
                .put("age", user.getAge())
                .put("cardNumber", user.getCardNumber())
                .put("zipcode", user.getZipcode())
                .put("password", user.getPassword());
            
            JsonDocument doc = JsonDocument.create("user::" + user.getUsername(), data);

            try {
                bucket.upsert(doc);
                JsonObject responseData = JsonObject.create()
                    .put("success", true)
                    .put("data", data);
                
                logger.info(" data after upsert = "+ responseData);
                
                return new ResponseEntity<String>(responseData.toString(), HttpStatus.OK);
            } catch (Exception e) {
                JsonObject responseData = JsonObject.empty()
                    .put("success", false)
                    .put("failure", "There was an error updating user profile data")
                    .put("exception", e.getMessage());
                 e.printStackTrace();
                return new ResponseEntity<String>(responseData.toString(), HttpStatus.OK);
            }
    }
}
