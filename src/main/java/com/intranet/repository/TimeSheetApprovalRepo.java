package com.intranet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.intranet.entity.TimeSheetApproval;

public interface TimeSheetApprovalRepo extends JpaRepository<TimeSheetApproval, Long> {

    @Query("SELECT a FROM TimeSheetApproval a WHERE a.timesheet.id = ?1")
    List<TimeSheetApproval> findByTimesheetId(Long timesheetId);
}
