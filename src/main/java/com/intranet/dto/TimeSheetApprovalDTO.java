package com.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetApprovalDTO {
    private Long timeSheetApprovalId;
    private Long timesheetId;
    private Long userApproverMapId;
    private String approvalStatus;
    private String description;
    private LocalDateTime approvalTime;
    public void setApproverId(Long approverId) {
        this.userApproverMapId = approverId;
    }
}