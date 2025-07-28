package com.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApproverMapDTO {
    private Long userApproverMapId;
    private Long userId;
    private Long approverId;
    private String rolePriority;
}