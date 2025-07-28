package com.intranet.dto;

import lombok.Data;

@Data
public class ApprovalStatusUpdateRequest {
    private Long userId;
    private Long timesheetId;
    private String status;
}
