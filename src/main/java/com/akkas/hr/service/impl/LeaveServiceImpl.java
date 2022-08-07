package com.akkas.hr.service.impl;

import com.akkas.hr.model.Employee;
import com.akkas.hr.model.Leave;
import com.akkas.hr.model.LeaveStatus;
import com.akkas.hr.model.PublicHoliday;
import com.akkas.hr.repository.EmployeeRepository;
import com.akkas.hr.repository.LeaveRepository;
import com.akkas.hr.service.LeaveService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveServiceImpl implements LeaveService {

  @Autowired
  LeaveRepository leaveRepository;

  @Autowired
  EmployeeRepository employeeRepository;

  @Override
  public List<Leave> findLeavesByPendingStatus() {
    return leaveRepository.findLeavesByStatus(String.valueOf(LeaveStatus.PENDING));
  }

  @Override
  public String approveLeave(Long id) {
    Optional<Leave> leaveData = leaveRepository.findById(id);
    if (leaveData.isPresent()) {
      Leave leave = leaveData.get();
      leave.setStatus(String.valueOf(LeaveStatus.APPROVED));
      leaveRepository.save(leave);
      return "leaveApproved";
    }
    return "leaveNotFound";
  }

  @Override
  public String rejectLeave(Long id) {
    Optional<Leave> leaveData = leaveRepository.findById(id);
    if (leaveData.isPresent()) {
      Leave leave = leaveData.get();
      leave.setStatus(String.valueOf(LeaveStatus.REJECTED));
      leaveRepository.save(leave);
      return "leaveRejected";
    }
    return "leaveNotFound";
  }

  @Override
  public List<Leave> findLeavesByEmployeeId(Long id) {
    Optional<Employee> employeeData = employeeRepository.findById(id);
    List<Leave> leaveList = null;
    if (employeeData.isPresent()) {
      Employee employee = employeeData.get();
      leaveList = employee.getLeaveList();
    }
    return leaveList;
  }

  @Override
  public String requestLeave(Long employeeId, Date startDate, Date endDate) {
    Optional<Employee> employeeData = employeeRepository.findById(employeeId);
    if (employeeData.isPresent()) {
      Integer requestedDays = null;
      try {
        requestedDays = calculateWorkDays(startDate, endDate);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      Employee emp = employeeData.get();
      Integer usedRights = getUsedLeaveDays(emp.getLeaveList());
      Integer maxRights = getTotalLeaveRights(emp.getWorkStartDate());

      int currentRight = maxRights - usedRights;

      if (requestedDays != null && requestedDays > currentRight) return "exceedLeaveRight";

      Leave leave = new Leave();
      leave.setStartDate(startDate);
      leave.setEndDate(endDate);
      leave.setStatus(String.valueOf(LeaveStatus.PENDING));
      leave.setWorkDay(requestedDays);

      emp.getLeaveList().add(leave);
      employeeRepository.save(emp);

      return "leaveRequestCreated";
    }
    return "employeeNotFound";
  }

  public Integer calculateWorkDays(Date startDate, Date endDate) throws ParseException {

    Calendar startDateCal = Calendar.getInstance();
    startDateCal.setTime(startDate);

    Calendar endDateCal = Calendar.getInstance();
    endDateCal.setTime(endDate);

    List<Calendar> holidayList = getOfficialHolidays();

    List<Integer> holidayDayOfYearList = new ArrayList<>();

    for (Calendar calendar : holidayList) {
      holidayDayOfYearList.add(calendar.get(Calendar.DAY_OF_YEAR));
    }

    int workDays = 0;

    if (startDateCal.getTimeInMillis() == endDateCal.getTimeInMillis()) {
      return 0;
    }

    do {
      startDateCal.add(Calendar.DAY_OF_MONTH, 1);
      if (startDateCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
          && startDateCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
          && !holidayDayOfYearList.contains(startDateCal.get(Calendar.DAY_OF_YEAR))) {
        ++workDays;
      }
    } while (startDateCal.getTimeInMillis() < endDateCal.getTimeInMillis());

    return workDays;
  }

  public List<Calendar> getOfficialHolidays() throws ParseException {

    List<PublicHoliday> publicHoliday =
        Unirest.get(
                "https://date.nager.at/api/v2/publicholidays/"
                    + Calendar.getInstance().get(Calendar.YEAR)
                    + "/tr")
            .asObject(new GenericType<List<PublicHoliday>>() {})
            .getBody();

    List<Calendar> officialHolidayList = new ArrayList<>();

    for (PublicHoliday holiday : publicHoliday) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date date = sdf.parse(holiday.getDate());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      officialHolidayList.add(calendar);
    }
    return officialHolidayList;
  }

  public Integer getTotalLeaveRights(Date startDate) {

    long diffInMillis = Math.abs(new Date().getTime() - startDate.getTime());
    long differenceInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

    int years = (int) (differenceInDays / 365);

    if (years < 1) {
      return 5;
    } else if (years <= 5) {
      return (years) * 15;
    } else if (years <= 10) {
      return (5 * 15) + ((years - 5) * 18);
    } else {
      return (5 * 15) + (5 * 18) + ((years - 10) * 24);
    }
  }

  public Integer getUsedLeaveDays(List<Leave> leaves) {
    int usedDays = 0;
    for (Leave leaf : leaves) {
      if (leaf.getStatus().equalsIgnoreCase(String.valueOf(LeaveStatus.APPROVED))
          || leaf.getStatus().equalsIgnoreCase(String.valueOf(LeaveStatus.PENDING))) {
        usedDays = usedDays + leaf.getWorkDay();
      }
    }
    return usedDays;
  }
}
