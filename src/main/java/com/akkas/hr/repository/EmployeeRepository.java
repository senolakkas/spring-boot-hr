package com.akkas.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.akkas.hr.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
