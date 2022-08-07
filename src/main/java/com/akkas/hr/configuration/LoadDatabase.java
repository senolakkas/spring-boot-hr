package com.akkas.hr.configuration;

import com.akkas.hr.model.Employee;
import com.akkas.hr.model.Leave;
import com.akkas.hr.model.LeaveStatus;
import com.akkas.hr.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  @Bean
  InitializingBean sendDatabase(EmployeeRepository repository) {
    return () -> {
      Calendar calendarStart = Calendar.getInstance();
      calendarStart.set(Calendar.YEAR, 2021);
      calendarStart.set(Calendar.MONTH, 11);
      calendarStart.set(Calendar.DATE, 31);

      Employee e = new Employee();
      e.setAddress("istanbul");
      e.setEmail("senolakkas@gmail.com");
      e.setGender("male");
      e.setName("Şenol");
      e.setSurname("Akkaş");
      e.setPhoneNumber("905546891491");
      e.setWorkStartDate(calendarStart.getTime());

      Leave leave = new Leave();
      leave.setStatus(String.valueOf(LeaveStatus.APPROVED));
      calendarStart.add(Calendar.DATE, -5);
      leave.setStartDate(calendarStart.getTime());
      calendarStart.add(Calendar.DATE, 3);
      leave.setEndDate(calendarStart.getTime());
      leave.setWorkDay(3);
      List<Leave> leaveList = new ArrayList<>();
      leaveList.add(leave);
      e.setLeaveList(leaveList);
      repository.save(e);
    };
  }
}
