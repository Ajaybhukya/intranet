package com.intranet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dto.PendingApprovalDTO;
import com.intranet.dto.TimeSheetApprovalDTO;
import com.intranet.entity.TimeSheet;
import com.intranet.entity.TimeSheetApproval;
import com.intranet.entity.UserApproverMap;
import com.intranet.repository.TimeSheetApprovalRepo;

@Service
public class TimeSheetApprovalService {

    @Autowired
    private TimeSheetApprovalRepo repo;

   public List<TimeSheetApprovalDTO> getApprovalsByManager(Long managerId) {
    List<TimeSheetApproval> approvals = repo.findByManagerId(managerId);
    return approvals.stream().map(approval -> {
        TimeSheetApprovalDTO dto = new TimeSheetApprovalDTO();
        dto.setTimeSheetApprovalId(approval.getTimeSheetApprovalId());
        dto.setTimesheetId(approval.getTimesheet().getId());
        dto.setUserApproverMapId(approval.getApprover().getUserApproverMapId());
        dto.setApprovalStatus(approval.getApprovalStatus());
        dto.setDescription(approval.getDescription());
        dto.setApprovalTime(approval.getApprovalTime());
        return dto;
    }).collect(Collectors.toList());
}


    public List<TimeSheetApprovalDTO> getApprovalsByTimesheetId(Long timesheetId) {
        return repo.findByTimesheet_Id(timesheetId).stream()
                .map(approval -> new TimeSheetApprovalDTO(
                        approval.getTimeSheetApprovalId(),
                        approval.getTimesheet().getId(),
                        approval.getApprover().getUserApproverMapId(),
                        approval.getApprovalStatus(),
                        approval.getDescription(),
                        approval.getApprovalTime()
                )).collect(Collectors.toList());
    }

    public TimeSheetApprovalDTO createApproval(TimeSheetApprovalDTO dto) {
    TimeSheetApproval approval = new TimeSheetApproval();

    // Assuming you're passing just the IDs in DTO
    approval.setApprovalStatus(dto.getApprovalStatus());
    approval.setDescription(dto.getDescription());
    approval.setApprovalTime(dto.getApprovalTime() != null ? dto.getApprovalTime() : java.time.LocalDateTime.now());

    // Set Timesheet and UserApproverMap manually using IDs (lazy reference)
    TimeSheet timesheet = new TimeSheet();
    timesheet.setId(dto.getTimesheetId());
    approval.setTimesheet(timesheet);

    UserApproverMap approver = new UserApproverMap();
    approver.setUserApproverMapId(dto.getUserApproverMapId());
    approval.setApprover(approver);

    TimeSheetApproval saved = repo.save(approval);

    // Return DTO
    TimeSheetApprovalDTO result = new TimeSheetApprovalDTO();
    result.setTimeSheetApprovalId(saved.getTimeSheetApprovalId());
    result.setTimesheetId(saved.getTimesheet().getId());
    result.setUserApproverMapId(saved.getApprover().getUserApproverMapId());
    result.setApprovalStatus(saved.getApprovalStatus());
    result.setDescription(saved.getDescription());
    result.setApprovalTime(saved.getApprovalTime());
    return result;
}

public List<PendingApprovalDTO> getPendingUserManagerPairs() {
    return repo.findPendingUserAndManagerPairs();
}

}
