package com.intranet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.entity.TimeSheetApproval;

public interface TimeSheetApprovalRepo  extends JpaRepository<TimeSheetApproval, Long> {

    List<TimeSheetApproval> findByTimesheetId(Long timesheetId);

}