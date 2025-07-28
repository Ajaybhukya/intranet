package com.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamTimeSheetDTO {
    private Long timeSheetId;
    private Long userId;
    private LocalDate workDate;
}
