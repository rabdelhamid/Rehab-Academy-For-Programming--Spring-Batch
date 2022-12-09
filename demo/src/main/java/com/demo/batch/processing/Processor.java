package com.demo.batch.processing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.demo.batch.processing.model.Employees;


//https://stackoverflow.com/questions/31737209/how-to-get-job-parameteres-in-to-item-processor-using-spring-batch-annotation
@Scope(value = "step")
//@Component
public class Processor implements ItemProcessor<Employees,Employees> {
//	@Autowired
//	AttendenceEngine attendenceEngine;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	private Date startDate;
	private Date endDate;
		
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
	    JobParameters jobParameters = stepExecution.getJobParameters();

	    startDate = jobParameters.getDate("startDate");
	    endDate = jobParameters.getDate("endDate");
	  
	}
	
	@Override	
	public Employees process(Employees employee) throws Exception {
		
		
		employee.setFullName(employee.getFirstName()+" "+employee.getLastName());
		
		System.out.print("   process:     "+employee.getId()+System.lineSeparator());
		
		
		//Call remote api
		/*HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String startDateToString = df.format(startDate);
        String endDateToString = df.format(endDate);
        
		final Map<String, Object> variables = new HashMap<>();
		variables.put("startDate", startDateToString);		
		variables.put("endDate", endDateToString);
		variables.put("employeeId", employee.getId());
		
		restTemplate.exchange("https://xyz....", HttpMethod.GET, entity, String.class,variables).getBody();
		*/
		return employee;
	}

}