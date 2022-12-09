package com.demo.batch.processing.controllers;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.batch.processing.utils.DateUtils;

@RestController
@RequestMapping("/engine")
public class BatchLaunchJobController {

	@Autowired
    JobLauncher jobLauncher;
 
    @Autowired
    Job processJob;

	@RequestMapping(value = "/callApi")
	public void callEngine(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(value = "employeeId") Integer employeeId) throws Exception{
	JobParameters jobParameters = new JobParametersBuilder()
			.addDate("startDate", DateUtils.setStartDate(new Date()) )
			.addDate("endDate", DateUtils.setEndDate(new Date()))   			
            .toJobParameters();
    jobLauncher.run(processJob, jobParameters);
	}

}
