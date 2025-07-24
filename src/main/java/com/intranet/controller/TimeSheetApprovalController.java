package com.intranet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.intranet.dto.PendingApprovalDTO;
import com.intranet.dto.TimeSheetApprovalDTO;
import com.intranet.service.TimeSheetApprovalService;

@RestController
@RequestMapping("/api/timesheet-approval")
public class TimeSheetApprovalController {

    @Autowired
    private TimeSheetApprovalService service;

    @GetMapping("/manager/{managerId}")
    public List<TimeSheetApprovalDTO> getByManager(@PathVariable Long managerId) {
        return service.getApprovalsByManager(managerId);
    }

    @GetMapping("/timesheet/{timesheetId}")
    public List<TimeSheetApprovalDTO> getByTimesheet(@PathVariable Long timesheetId) {
        return service.getApprovalsByTimesheetId(timesheetId);
    }

    @PostMapping("/create")
    public TimeSheetApprovalDTO create(@RequestBody TimeSheetApprovalDTO dto) {
        return service.createApproval(dto);
    }

    @GetMapping("/pending-users")
    public List<PendingApprovalDTO> getPendingUserManagerPairs() {
    return service.getPendingUserManagerPairs();
    }

    @GetMapping("/users-by-status")
    public List<PendingApprovalDTO> getUserManagerPairsByStatus(
            @RequestParam(required = false) String status) {
        return service.getUserManagerPairsByStatus(status);
    }


}
