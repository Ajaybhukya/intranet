package com.intranet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
import com.intranet.repository.UserApproverMapRepo;

@Service
public class TimeSheetApprovalService {

    @Autowired
    private TimeSheetApprovalRepo repo;

    @Autowired
    private UserApproverMapRepo userApproverMapRepo;


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

public int bulkUpdateSelectedApprovals(Long managerId, String status, List<Long> timesheetIds) {
    // Step 1: Get users under this manager
    List<UserApproverMap> mappedUsers = userApproverMapRepo.findByApproverId(managerId);
    Set<Long> allowedUserIds = mappedUsers.stream()
            .map(UserApproverMap::getUserId)
            .collect(Collectors.toSet());

    // Step 2: Get all approvals that match manager, status=PENDING, and timesheet ID in list
    List<TimeSheetApproval> approvals = repo.findByManagerAndTimesheetIds(managerId, "PENDING", timesheetIds);

    // Step 3: Filter approvals where the timesheet belongs to users mapped to this manager
    List<TimeSheetApproval> validApprovals = approvals.stream()
            .filter(approval -> allowedUserIds.contains(approval.getTimesheet().getUserId()))
            .collect(Collectors.toList());

    // Step 4: Update
    for (TimeSheetApproval approval : validApprovals) {
        approval.setApprovalStatus(status);
        approval.setApprovalTime(LocalDateTime.now());
    }

    repo.saveAll(validApprovals);

    return validApprovals.size();
}


public void updateApprovalStatus(Long managerId, Long userId, Long timesheetId, String status) {
    if (!"APPROVED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
        throw new IllegalArgumentException("Status must be APPROVED or REJECTED");
    }

    TimeSheetApproval approval = repo
        .findByTimesheetAndApprover(timesheetId, userId, managerId)
        .orElseThrow(() -> new RuntimeException("No approval record found for given manager, user, and timesheet"));

    approval.setApprovalStatus(status.toUpperCase());
    approval.setApprovalTime(LocalDateTime.now());

    repo.save(approval);
}


}