package com.intranet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.entity.TimeSheetEntry;
import com.intranet.repository.TimeSheetEntryRepo;

@Service
public class TimeSheetEntryService {
    
    @Autowired
    private TimeSheetEntryRepo timeSheetEntryRepo;


    public TimeSheetEntry creaTimeSheetEntry(TimeSheetEntry timeSheetEntry) {
        return timeSheetEntryRepo.save(timeSheetEntry);
    }
}
