package com.intranet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.entity.TimeSheet;
import com.intranet.repository.TimeSheetRepo;

@Service
public class TimeSheetService {
    @Autowired
    private TimeSheetRepo timeSheetRepo;

    public TimeSheet createTimesheet(Long userId, TimeSheet timesheet) {
        timesheet.setUserId(userId);
        return timeSheetRepo.save(timesheet);
    }   
}