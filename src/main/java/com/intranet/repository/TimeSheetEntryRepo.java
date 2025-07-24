package com.intranet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.intranet.entity.TimeSheetEntry;

public interface TimeSheetEntryRepo extends JpaRepository<TimeSheetEntry, Long> {

    @Query("SELECT e FROM TimeSheetEntry e WHERE e.timesheet.id = ?1")
    List<TimeSheetEntry> findByTimesheetId(Long timesheetId);
}
