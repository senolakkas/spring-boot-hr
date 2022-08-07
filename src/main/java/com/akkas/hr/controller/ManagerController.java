package com.akkas.hr.controller;

import com.akkas.hr.model.Leave;
import com.akkas.hr.service.LeaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "manager")
@Api(value = "Manager Rest Api")
public class ManagerController {

  @Autowired LeaveService leaveService;

  @GetMapping("/leave")
  @ApiOperation(value = "list leaves on pending status")
  public ResponseEntity<List<Leave>> findPendingLeaves() {
    List<Leave> leaveList = leaveService.findLeavesByPendingStatus();
    return new ResponseEntity<>(leaveList, HttpStatus.OK);
  }

  @PostMapping("/approve")
  @ApiOperation(value = "approve leave with leave id")
  public ResponseEntity<String> approveLeave(@RequestParam(name = "id") Long id) {
    String response = leaveService.approveLeave(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/reject")
  @ApiOperation(value = "reject leave with leave id")
  public ResponseEntity<String> rejectLeave(@RequestParam(name = "id") Long id) {
    String response = leaveService.rejectLeave(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
