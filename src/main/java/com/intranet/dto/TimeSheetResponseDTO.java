

package com.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetResponseDTO {
    private Long timesheetId;
    private LocalDate workDate;
    private LocalDateTime createdAt;
    private String approvalStatus; // FINAL status
    private List<TimeSheetEntryResponseDTO> entries;

    private List<Long> approvedBy;
    private List<Long> rejectedBy;
    private List<Long> pendingBy;
}

