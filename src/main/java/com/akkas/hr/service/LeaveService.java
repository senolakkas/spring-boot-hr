package com.akkas.hr.service;

import com.akkas.hr.model.Leave;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface LeaveService {

  List<Leave> findLeavesByPendingStatus();

  List<Leave> findLeavesByEmployeeId(Long id);

  String approveLeave(Long id);

  String rejectLeave(Long id);

  String requestLeave(Long employeeId, Date startDate, Date endDate);
}
