package com.chakrar.expensemanager.repo;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParser;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.core.ObjectCodec;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.DeserializationContext;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonDeserializer;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;


public class ExpenseDetailDeserilizer extends JsonDeserializer<ExpenseDetail> {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ExpenseDetailDeserilizer.class);

	
	
	@Override
	public ExpenseDetail deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		ObjectCodec oc = parser.getCodec();
		JsonNode node = oc.readTree(parser);

		logger.info("               INSIDE deserialize =====================           ");
		String type = node.get("transactionType") == null ? null : node.get("transactionType").textValue();
		logger.info("transactionType ===== " + type);
		String category = node.get("category") == null ? null : node.get("category").textValue();
		Double amount = node.get("transactionAmount") == null ? null : node.get("transactionAmount").doubleValue();
		String merchant = node.get("merchant") == null ? null : node.get("merchant").textValue();
		String description = node.get("description") == null ? null : node.get("description").textValue();
		 
		Long lDate = node.get("transactionDate") == null ? null : node.get("transactionDate").longValue();
		Date transactionDate = getDate(lDate);
		return new ExpenseDetail(type, transactionDate, amount, merchant, description, category);

	}


	private Date getDate(Long dateInMs) {
	      
	       //creating Date from millisecond
	       Date currentDate = new Date(dateInMs);
	      
	       //printing value of Date
	       System.out.println("current Date: " + currentDate);
	      
/*	       DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
	      
	       //formatted value of current Date
	       System.out.println("Milliseconds to Date: " + df.format(currentDate));
	      
	       //Converting milliseconds to Date using Calendar
	       Calendar cal = Calendar.getInstance();
	       cal.setTimeInMillis(dateInMs);
	       System.out.println("Milliseconds to Date using Calendar:"
	               + df.format(cal.getTime()));
	      
	       //copying one Date's value into another Date in Java
	       Date now = new Date();
	       Date copiedDate = new Date(now.getTime());
	      
	       System.out.println("original Date: " + df.format(now));
	       System.out.println("copied Date: " + df.format(copiedDate));*/
	       return currentDate;
	}



}
