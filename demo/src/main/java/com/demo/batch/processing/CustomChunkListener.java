package com.demo.batch.processing;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.batch.processing.config.SechduleProperties;
//https://docs.spring.io/spring-batch/docs/current/reference/html/step.html
public class CustomChunkListener implements ChunkListener {
	
	@Autowired 
	private SechduleProperties attendenceSechduleProperties;
	
	@Override
	public void afterChunk(ChunkContext context) {
//		try {
//			Thread.sleep(attendenceSechduleProperties.getAfterChunkWaitTime());//5 min
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Called afterChunk().");
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		//System.out.println("Called beforeChunk().");
	}

       @Override
	public void afterChunkError(ChunkContext context) {
		//System.out.println("Called afterChunkError().");
	}
}