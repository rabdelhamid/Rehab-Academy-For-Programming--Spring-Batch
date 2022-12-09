package com.demo.batch.processing.repositories;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.batch.processing.model.Employees;



@Repository
public interface EmployeesRepositery extends JpaRepository <Employees, Integer> {
	
	@Query(value ="select e from Employees e"
			+ " where startDate <= :toDate "			
			+ " and ((endDate !=null and endDate >= :fromDate) or (endDate is null)) ",nativeQuery = false) 
	Page<Integer> findAllEmployeesBy(@Param("fromDate")Date fromDate,@Param("toDate")Date toDate,Pageable pageable);
}
