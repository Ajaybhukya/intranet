package com.intranet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dto.ManagerDetailedDTO;
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



public List<PendingApprovalDTO> getUserManagerPairsByStatus(String status) {
    return repo.findUserAndManagerPairsByStatus(status);
}

public List<ManagerDetailedDTO> getManagerDetailedData(Long managerId) {
        return repo.findDetailedByManagerId(managerId);
    }


    public List<ManagerDetailedDTO> getRecentTimesheets(Long managerId, String approvalStatus) {
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusDays(6); // Last 7 days including today

    List<TimeSheetApproval> approvals;

    if (approvalStatus != null) {
        approvals = repo.findByManagerIdAndStatusAndDateRange(managerId, approvalStatus, startDate, today);
    } else {
        approvals = repo.findByManagerIdAndDateRange(managerId, startDate, today);
    }

    return approvals.stream().flatMap(approval -> {
        TimeSheet timesheet = approval.getTimesheet();
        return timesheet.getEntries().stream().map(entry -> {
            ManagerDetailedDTO dto = new ManagerDetailedDTO();
            dto.setUserId(timesheet.getUserId());
            dto.setTimesheetId(timesheet.getId());
            dto.setProjectId(entry.getProjectId());
            dto.setTaskId(entry.getTaskId());
            dto.setHoursWorked(entry.getHoursWorked());
            dto.setApprovalStatus(approval.getApprovalStatus());
            dto.setDescription(entry.getDescription());
            dto.setWorkDate(timesheet.getWorkDate());
            return dto;
        });
    }).toList();
}

public void bulkUpdateApprovals(Long managerId, String status) {
    List<TimeSheetApproval> approvals = repo.findByManagerIdAndStatus(managerId, "PENDING");

    for (TimeSheetApproval approval : approvals) {
        approval.setApprovalStatus(status);
        approval.setApprovalTime(LocalDateTime.now());
    }

    repo.saveAll(approvals);
}


public void updateApprovalStatus(Long managerId, Long userId, Long timesheetId, String status) {
    // if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
    //     throw new IllegalArgumentException("Status must be APPROVED or REJECTED");
    // }

    TimeSheetApproval approval = repo
        .findByTimesheetIdAndApprover_UserIdAndApprover_ApproverId(timesheetId, userId, managerId)
        .orElseThrow(() -> new RuntimeException("Approval record not found"));

    approval.setApprovalStatus(status);
    approval.setApprovalTime(LocalDateTime.now()); // optional update
    repo.save(approval);
}

}