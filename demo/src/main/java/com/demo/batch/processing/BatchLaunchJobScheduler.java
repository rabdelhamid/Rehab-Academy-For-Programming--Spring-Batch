package com.demo.batch.processing;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.demo.batch.processing.utils.DateUtils;



@Component
//@AllArgsConstructor
//@NoArgsConstructor//https://stackoverflow.com/questions/52841620/parameter-0-of-constructor-in-required-a-bean-of-type-java-lang-string-that-co
public class BatchLaunchJobScheduler {
	@Autowired
    JobLauncher jobLauncher;
 
    @Autowired
    Job processJob;
    //@Scheduled(fixedRate = 60000)
    //@Scheduled(cron = "0 0 13 * * *")
    @Scheduled(cron = "${sechdule.cron-expression}")
    public void schedule() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
    	
    	
    	JobParameters jobParameters = new JobParametersBuilder()
    			.addDate("startDate", DateUtils.setStartDate(new Date()) )
    			.addDate("endDate", DateUtils.setEndDate(new Date()))   
    				
                .toJobParameters();
        jobLauncher.run(processJob, jobParameters);
    }
}
