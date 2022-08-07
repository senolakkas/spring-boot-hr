package com.akkas.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.akkas.hr.model.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
  List<Leave> findLeavesByStatus(String status);
}
