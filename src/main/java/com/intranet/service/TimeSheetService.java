package com.intranet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.entity.TimeSheet;
import com.intranet.entity.TimeSheetEntry;
import com.intranet.repository.TimeSheetRepo;

@Service
public class TimeSheetService {
    @Autowired
    private TimeSheetRepo timeSheetRepo;
    public TimeSheet createTimeSheet(TimeSheet timesheet) {
        if (timesheet.getEntries() != null) {
            for (TimeSheetEntry entry : timesheet.getEntries()) {
                entry.setTimesheet(timesheet);
            }
        }

        return timeSheetRepo.save(timesheet); // saves timesheet and entries due to CascadeType.ALL
    }
}