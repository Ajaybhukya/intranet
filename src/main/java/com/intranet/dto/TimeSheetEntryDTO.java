package com.intranet.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetEntryDTO {
    private Long projectId;
    private Long taskId;
    private String description;
    private String workType;
    private BigDecimal hoursWorked;
}