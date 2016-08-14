package com.chakrar.expensemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.chakrar.expensemanager.repo.ExpenseDetailsRepository;

@SpringBootApplication
public class ExpenseManager {
	
	static final String DATE_FORMAT = "yyyyy-mm-dd";
	
	private static final Logger log = LoggerFactory.getLogger(ExpenseManager.class);
	
	
	public static void main(String[] args) {
		SpringApplication.run(ExpenseManager.class, args);
	}
	
	@Bean
    public CommandLineRunner loadData(final ExpenseDetailsRepository repository) {
        return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
			    
			    /*for (ExpenseDetail ed : repository.findByTransactionDate(getFormattedDate("2016-03-12"))) {  
			    	log.info("********       Expense Details :=         "+ed);  
		        }*/			    
			    
			    log.info("********   Total Count of Records   "+ repository.count());
			    
			    /*for (ExpenseDetail ed : repository.findAll()) {  
			    	log.info("********       Expense Details :=         "+ed);  
		        }*/
			    
			    //List<ExpenseDetail> ex = fts.findByText("Kids");
			    log.info(" findByMerchant := "+repository.findByMerchant("Patel"));
			    //log.info(" size := "+ ex.size());
			}
		};
    }
	
/*    private Date getFormattedDate (String date_s) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    	Date newDate = sdf.parse(date_s); 
    	return newDate;
    }*/
    
}
