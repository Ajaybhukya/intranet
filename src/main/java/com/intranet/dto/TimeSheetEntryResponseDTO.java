package com.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetEntryResponseDTO {

    private Long timesheetEntryId;        // Changed from entryId
    private Long projectId;
    private Long taskId;
    private String description;
    private String workType;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private BigDecimal hoursWorked;
    private String otherDescription;
}
