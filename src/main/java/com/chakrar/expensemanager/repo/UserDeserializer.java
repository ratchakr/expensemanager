package com.chakrar.expensemanager.repo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParser;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.core.ObjectCodec;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.DeserializationContext;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonDeserializer;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;

public class UserDeserializer extends JsonDeserializer<User> {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(UserDeserializer.class);


	@Override
	public User deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = parser.getCodec();
		JsonNode node = oc.readTree(parser);

		logger.info("               INSIDE deserialize =====================           ");
		String email = node.get("email") == null ? null : node.get("email").textValue();
		logger.info("email ===== " + email);
		String address = node.get("address") == null ? null : node.get("address").textValue();
		String phone = node.get("phone") == null ? null : node.get("phone").textValue();
		Integer age = node.get("age") == null ? null : node.get("age").intValue();
		String cardNumber = node.get("cardNumber") == null ? null : node.get("cardNumber").textValue();
		String pwd = node.get("password") == null ? null : node.get("password").textValue();
		 
		Integer zipcode = node.get("zipcode") == null ? null : node.get("zipcode").intValue();
		return new User(email, email, address, pwd, phone, age, cardNumber, zipcode);

	}

}
