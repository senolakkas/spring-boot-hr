package com.akkas.hr.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.akkas.hr.model.Employee;
import com.akkas.hr.model.Leave;
import com.akkas.hr.model.LeaveStatus;
import com.akkas.hr.repository.EmployeeRepository;
import com.akkas.hr.repository.LeaveRepository;
import com.akkas.hr.service.impl.LeaveServiceImpl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

  @InjectMocks LeaveServiceImpl leaveService;

  @Mock LeaveRepository leaveRepository;

  @Mock EmployeeRepository employeeRepository;

  @Test
  void findLeavesByPendingStatus_whenLeaveRecordExistsOnPendingStatus_returnLeaveList() {
    Mockito.when(leaveRepository.findLeavesByStatus(String.valueOf(LeaveStatus.PENDING)))
        .thenReturn(createLeaveList(1, String.valueOf(LeaveStatus.PENDING)));
    List<Leave> resultList = leaveService.findLeavesByPendingStatus();
    assertEquals(1, resultList.size());
  }

  @Test
  void findLeavesByEmployeeId_whenEmployeeHasLeaves_returnLeaveList() {
    Mockito.when(employeeRepository.findById(1L)).thenReturn(employee(1L, new Date()));
    List<Leave> resultList = leaveService.findLeavesByEmployeeId(1L);
    assertEquals(1, resultList.size());
  }

  @Test
  void getUsedLeaveDays_whenPendingLeaveExist_returnDayCount() {
    List<Leave> list = createLeaveList(1, String.valueOf(LeaveStatus.PENDING));
    Integer result = leaveService.getUsedLeaveDays(list);
    assertEquals(1, result);
  }

  @Test
  void getUsedLeaveDays_whenApprovedLeaveExist_returnDayCount() {
    List<Leave> list = createLeaveList(1, String.valueOf(LeaveStatus.APPROVED));
    Integer result = leaveService.getUsedLeaveDays(list);
    assertEquals(1, result);
  }

  @Test
  void getTotalLeaveRights_whenEmployeeWorkedLessThanOneYear_returnFiveDaysRight() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DATE, -100);
    Integer result = leaveService.getTotalLeaveRights(c.getTime());
    assertEquals(5, result);
  }

  @Test
  void getTotalLeaveRights_whenEmployeeWorkedFiveYear_returnFifteenDaysRight() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DATE, 365);
    Integer result = leaveService.getTotalLeaveRights(c.getTime());
    assertEquals(15, result);
  }

  @Test
  void getTotalLeaveRights_whenEmployeeWorkedTenYears_return165DaysRight() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DATE, 365 * 10);
    Integer result = leaveService.getTotalLeaveRights(c.getTime());
    assertEquals(165, result);
  }

  @Test
  void getTotalLeaveRights_whenEmployeeWorkedFifteenYears_return285DaysRight() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DATE, 365 * 15);
    Integer result = leaveService.getTotalLeaveRights(c.getTime());
    assertEquals(285, result);
  }

  @Test
  void calculateWorkDays_whenStartDateEqualsEndDate_returnZeroWorkDays() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    try {
      Integer result = leaveService.calculateWorkDays(c.getTime(), c.getTime());
      assertEquals(0, result);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Test
  void calculateWorkDays_whenStartDateDoesNotEqualsEndDate_returnNonZeroWorkDays() {
    Calendar c1 = Calendar.getInstance();
    c1.setTime(new Date());

    Calendar c2 = Calendar.getInstance();
    c2.setTime(new Date());
    c2.add(Calendar.DATE, 100);

    try {
      Integer result = leaveService.calculateWorkDays(c1.getTime(), c2.getTime());
      assertNotEquals(0, result);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Test
  void requestLeave_whenEmployeeIsNotFound_returnNotFoundMessageCode() {
    Calendar c1 = Calendar.getInstance();
    c1.setTime(new Date());

    Calendar c2 = Calendar.getInstance();
    c2.setTime(new Date());
    c2.add(Calendar.DATE, 365);

    String messageCode = leaveService.requestLeave(1L, c1.getTime(), c2.getTime());

    assertEquals("employeeNotFound", messageCode);
  }

  public static List<Leave> createLeaveList(int count, String status) {
    List<Leave> list = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      Leave leave = new Leave();
      leave.setStatus(status);
      leave.setWorkDay(1);
      list.add(leave);
    }
    return list;
  }

  public static Optional<Employee> employee(Long id, Date startWorkDate) {
    Employee emp = new Employee();
    emp.setEmployeeId(id);
    emp.setWorkStartDate(startWorkDate);
    emp.setLeaveList(createLeaveList(1, String.valueOf(LeaveStatus.APPROVED)));
    return Optional.of(emp);
  }
}
