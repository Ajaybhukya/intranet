package com.intranet.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetHistoryDTO {
    private LocalDate workDate;
    private List<TimeSheetEntryDTO> entries;
    private List<TimeSheetApprovalDTO> approvals;
}


