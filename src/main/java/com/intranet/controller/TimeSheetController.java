package com.intranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.entity.TimeSheet;
import com.intranet.service.TimeSheetService;

@RestController
@RequestMapping("/api/timesheets")
public class TimeSheetController {

    @Autowired
    private TimeSheetService timeSheetService;

    @PostMapping("/create")
    public ResponseEntity<TimeSheet> createTimeSheet(@RequestBody TimeSheet timesheet) {
        TimeSheet savedTimeSheet = timeSheetService.createTimeSheet(timesheet);
        return new ResponseEntity<>(savedTimeSheet, HttpStatus.CREATED);
    }
}
