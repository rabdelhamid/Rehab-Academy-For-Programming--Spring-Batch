package com.demo.batch.processing;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.demo.batch.processing.model.Employees;

public class Writer2 implements ItemWriter<Employees> {

	@Override
	public void write(List<? extends Employees> nResult) throws Exception {
		for (Employees empId : nResult) {
			System.out.println(" Writing the data from Step2 " + empId.getFullName()+System.lineSeparator());
		}
	}

}