package com.akkas.hr.controller;

import com.akkas.hr.model.Leave;
import com.akkas.hr.service.LeaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "employee")
@Api(value = "Employee Rest Api")
public class EmployeeController {

  @Autowired LeaveService leaveService;

  @GetMapping(value = "/leave")
  @ApiOperation(value = "list leaves with employee id")
  public ResponseEntity<List<Leave>> findLeaves(
      @RequestParam(name = "employeeId") Long employeeId) {
    List<Leave> list = leaveService.findLeavesByEmployeeId(employeeId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("new")
  @ApiOperation(value = "request new leave")
  public ResponseEntity<String> requestLeave(
      @RequestParam("employeeId") @NotEmpty Long employeeId,
      @RequestParam("startDate") @DateTimeFormat(pattern = "dd.MM.yyyy") Date startDate,
      @RequestParam("endDate") @DateTimeFormat(pattern = "dd.MM.yyyy") Date endDate) {
    String response = leaveService.requestLeave(employeeId, startDate, endDate);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
