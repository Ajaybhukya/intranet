package com.intranet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.entity.TimeSheet;

public interface TimeSheetRepo  extends JpaRepository<TimeSheet, Long> {
    
}
