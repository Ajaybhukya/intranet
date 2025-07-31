package com.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApproverSummaryDTO {
    private Long userId;
    private String userName;
    private List<ApproverDTO> approvers; // No priority anymore
}
