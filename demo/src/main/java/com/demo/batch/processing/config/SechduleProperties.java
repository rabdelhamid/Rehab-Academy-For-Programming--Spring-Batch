package com.demo.batch.processing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//https://www.baeldung.com/configuration-properties-in-spring-boot

@Configuration
@ConfigurationProperties(prefix = "sechdule")
public class SechduleProperties {
 //private String cronExpression;
 private int pageSize;
 private int chunkSize;
 private long afterChunkWaitTime;
 
 public SechduleProperties() {
		super();
	}
 
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}
public int getChunkSize() {
	return chunkSize;
}
public void setChunkSize(int chunkSize) {
	this.chunkSize = chunkSize;
}
public long getAfterChunkWaitTime() {
	return afterChunkWaitTime;
}
public void setAfterChunkWaitTime(long afterChunkWaitTime) {
	this.afterChunkWaitTime = afterChunkWaitTime;
}

 
 
}
