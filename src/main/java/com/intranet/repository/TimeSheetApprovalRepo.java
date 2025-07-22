package com.intranet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.entity.TimeSheetApproval;

public interface TimeSheetApprovalRepo  extends JpaRepository<TimeSheetApproval, Long> {

    
}