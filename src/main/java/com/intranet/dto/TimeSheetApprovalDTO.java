package com.intranet.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetApprovalDTO {
    private String approvalStatus;
    private Long approverId;
    private LocalDateTime approvalTime;
    private String description;
}