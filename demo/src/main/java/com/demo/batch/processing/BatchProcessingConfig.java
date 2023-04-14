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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

import com.demo.batch.processing.config.SechduleProperties;
import com.demo.batch.processing.model.Employees;
import com.demo.batch.processing.repositories.EmployeesRepositery;

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
	 public Job processJob(JobCompletionListener listener,@Qualifier("orderStep1") Step step1,@Qualifier("orderStep2") Step step2)
	            throws Exception {

	      return this.jobBuilderFactory.get("processJob")
	    		      .incrementer(new RunIdIncrementer())
	                  .listener(listener)
	                  .start(step1)
	                  .next(step2)
	                  .build();
	 }
	@Qualifier("orderStep1")
	@Bean
	public Step orderStep1(@Qualifier("readerStep1") ItemReader<Employees> itemReader,
						   @Qualifier("witerStep1")ItemWriter<Employees> itemWriter,
						   @Qualifier("processorStep1") Processor employeeItemProcessor) {
			return stepBuilderFactory.get("orderStep1")
					.<Employees, Employees> chunk(sechduleProperties.getChunkSize())				
					.reader(itemReader)
					.processor(employeeItemProcessor)
					.writer(itemWriter)						
					.listener(chunkListener()).build();
	}
	@Qualifier("orderStep2")
	@Bean
	public Step orderStep2(@Qualifier("readerStep2") ItemReader<Employees> itemReader2,
						   @Qualifier("witerStep2")ItemWriter<Employees> itemWriter2,
						   @Qualifier("processorStep2") Processor2 employeeItemProcessor2) {
			return stepBuilderFactory.get("orderStep1")
					.<Employees, Employees> chunk(sechduleProperties.getChunkSize())				
					.reader(itemReader2)
					.processor(employeeItemProcessor2)
					.writer(itemWriter2)						
					.listener(chunkListener()).build();
	}
	
	@Qualifier("readerStep1")
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
        sorts.put("id", Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }
	@Qualifier("readerStep2")
	@StepScope
	@Bean
    public RepositoryItemReader<Employees> readerStep2(@Value("#{jobParameters['startDate']}") Date fromDate,@Value("#{jobParameters['endDate']}") Date toDate) {
        RepositoryItemReader<Employees> reader = new RepositoryItemReader<>();
        
        reader.setRepository(employeesRepositery);
        reader.setMethodName("findAll");
      
        List<Object> queryMethodArguments = new ArrayList<>();
        //queryMethodArguments.add(fromDate);
        //queryMethodArguments.add(toDate);
       // reader.setArguments(queryMethodArguments);
        
        reader.setPageSize(sechduleProperties.getPageSize());
        Map<String, Direction> sorts = new HashMap<>();        
        sorts.put("id", Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }
	@Qualifier("processorStep1")
	@Bean
    public Processor employeeItemProcessor()
    {
        return new Processor();
    }
	@Qualifier("processorStep2")
	@Bean
    public Processor2 employeeItemProcessor2()
    {
        return new Processor2();
    }
	@Qualifier("witerStep1")
	@Bean
    public ItemWriter<Employees> itemWriter() {       
		return new Writer();
    }
	@Qualifier("witerStep2")
	@Bean
    public ItemWriter<Employees> itemWriterStep2() {       
		return new Writer2();
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
	

	
	
}
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

import com.demo.batch.processing.config.SechduleProperties;
import com.demo.batch.processing.model.Employees;
import com.demo.batch.processing.repositories.EmployeesRepositery;

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
	 public Job processJob(JobCompletionListener listener,@Qualifier("orderStep1") Step step1,@Qualifier("orderStep2") Step step2)
	            throws Exception {

	      return this.jobBuilderFactory.get("processJob")
	    		      .incrementer(new RunIdIncrementer())
	                  .listener(listener)
	                  .start(step1)
	                  .next(step2)
	                  .build();
	 }
	@Qualifier("orderStep1")
	@Bean
	public Step orderStep1(@Qualifier("readerStep1") ItemReader<Employees> itemReader,
						   @Qualifier("witerStep1")ItemWriter<Employees> itemWriter,
						   @Qualifier("processorStep1") Processor employeeItemProcessor) {
			return stepBuilderFactory.get("orderStep1")
					.<Employees, Employees> chunk(sechduleProperties.getChunkSize())				
					.reader(itemReader)
					.processor(employeeItemProcessor)
					.writer(itemWriter)						
					.listener(chunkListener()).build();
	}
	@Qualifier("orderStep2")
	@Bean
	public Step orderStep2(@Qualifier("readerStep2") ItemReader<Employees> itemReader2,
						   @Qualifier("witerStep2")ItemWriter<Employees> itemWriter2,
						   @Qualifier("processorStep2") Processor2 employeeItemProcessor2) {
			return stepBuilderFactory.get("orderStep1")
					.<Employees, Employees> chunk(sechduleProperties.getChunkSize())				
					.reader(itemReader2)
					.processor(employeeItemProcessor2)
					.writer(itemWriter2)						
					.listener(chunkListener()).build();
	}
	
	@Qualifier("readerStep1")
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
        sorts.put("id", Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }
	@Qualifier("readerStep2")
	@StepScope
	@Bean
    public RepositoryItemReader<Employees> readerStep2(@Value("#{jobParameters['startDate']}") Date fromDate,@Value("#{jobParameters['endDate']}") Date toDate) {
        RepositoryItemReader<Employees> reader = new RepositoryItemReader<>();
        
        reader.setRepository(employeesRepositery);
        reader.setMethodName("findAll");
      
        List<Object> queryMethodArguments = new ArrayList<>();
        //queryMethodArguments.add(fromDate);
        //queryMethodArguments.add(toDate);
       // reader.setArguments(queryMethodArguments);
        
        reader.setPageSize(sechduleProperties.getPageSize());
        Map<String, Direction> sorts = new HashMap<>();        
        sorts.put("id", Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }
	@Qualifier("processorStep1")
	@Bean
    public Processor employeeItemProcessor()
    {
        return new Processor();
    }
	@Qualifier("processorStep2")
	@Bean
    public Processor2 employeeItemProcessor2()
    {
        return new Processor2();
    }
	@Qualifier("witerStep1")
	@Bean
    public ItemWriter<Employees> itemWriter() {       
		return new Writer();
    }
	@Qualifier("witerStep2")
	@Bean
    public ItemWriter<Employees> itemWriterStep2() {       
		return new Writer2();
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
	

	
	
}
