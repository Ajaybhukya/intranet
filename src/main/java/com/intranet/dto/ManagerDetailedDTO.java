package com.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDetailedDTO {
    private Long userId;
    private Long timesheetId;
    private Long projectId;
    private Long taskId;
    private BigDecimal hoursWorked;
    private String approvalStatus;
    private String description;
    private LocalDate workDate;

}
