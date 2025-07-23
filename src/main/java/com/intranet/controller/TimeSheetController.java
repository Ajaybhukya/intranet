package com.intranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.entity.TimeSheet;
import com.intranet.entity.TimeSheetEntry;
import com.intranet.service.TimeSheetService;

@RestController
@RequestMapping("/timesheet")
public class TimeSheetController {
    
    @Autowired
    private TimeSheetService timeSheetService;


    @PostMapping("/create/{userId}")
    public TimeSheet createTimesheet(Long userId) {
         return timeSheetService.createTimesheet(userId, new TimeSheet());
    }
}
