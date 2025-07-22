package com.intranet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.entity.TimeSheetEntry;

public interface TimeSheetEntryRepo  extends JpaRepository<TimeSheetEntry, Long> {
    
}
