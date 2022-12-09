package com.demo.batch.processing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

import com.demo.batch.processing.config.SechduleProperties;
import com.demo.batch.processing.model.Employees;
import com.demo.batch.processing.repositories.EmployeesRepositery;




//https://www.javainuse.com/spring/bootbatch
@Configuration
@EnableBatchProcessing

public class BatchProcessingConfig {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	EmployeesRepositery employeesRepositery;	
	
	@Autowired 
	private SechduleProperties sechduleProperties;
	
		
	@Bean
	 public Job processJob(JobCompletionListener listener, Step step1)
	            throws Exception {

	      return this.jobBuilderFactory.get("processJob")
	    		      .incrementer(new RunIdIncrementer())
	                  .listener(listener)
	                  .start(step1)
	                  .build();
	 }
	
	//https://stackoverflow.com/questions/53490144/spring-batch-understanding-chunk-processing
	@Bean
	public Step orderStep1(ItemReader<Employees> itemReader) {
			return stepBuilderFactory.get("orderStep1")
					.<Employees, Employees> chunk(sechduleProperties.getChunkSize())				
					.reader(itemReader)
					.processor(employeeItemProcessor())
					//.writer(itemWriter())
					.writer(repositoryItemWriter())					
					.listener(chunkListener()).build();
	}
	
	// You should sepcify the method which  
    //spring batch should call in your repository to fetch 
    // data and the arguments it needs needs to be  
    //specified with the below method.
	@StepScope
	@Bean
    public RepositoryItemReader<Employees> reader(@Value("#{jobParameters['startDate']}") Date fromDate,@Value("#{jobParameters['endDate']}") Date toDate) {
        RepositoryItemReader<Employees> reader = new RepositoryItemReader<>();
        
        reader.setRepository(employeesRepositery);
        reader.setMethodName("findAllEmployeesBy");
      
        List<Object> queryMethodArguments = new ArrayList<>();
        queryMethodArguments.add(fromDate);
        queryMethodArguments.add(toDate);
        reader.setArguments(queryMethodArguments);
        
        reader.setPageSize(sechduleProperties.getPageSize());
        Map<String, Direction> sorts = new HashMap<>();        
        sorts.put("id", Direction.ASC);//https://stackoverflow.com/questions/31691470/ora-01791-not-a-selected-expression
        reader.setSort(sorts);

        return reader;
    }
	 
	@Bean
    public Processor employeeItemProcessor()
    {
        return new Processor();
    }
	@Bean
    public ItemWriter<Employees> itemWriter() {       
		return new Writer();
    }
	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
	@Bean
    public CustomChunkListener chunkListener()
    {
        return new CustomChunkListener();
    }
	@Bean
    public RepositoryItemWriter<Employees> repositoryItemWriter() {
        RepositoryItemWriter<Employees> iwriter = new RepositoryItemWriter<>();       
        iwriter.setRepository(employeesRepositery);
        iwriter.setMethodName("save");
        return iwriter;
		
    }

	
	
}
