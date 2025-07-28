
package com.intranet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.dto.ApprovalStatusUpdateRequest;
import com.intranet.dto.ManagerDetailedDTO;
import com.intranet.dto.UserDTO;
import com.intranet.security.CurrentUser;
import com.intranet.service.TimeSheetApprovalService;

@RestController
@RequestMapping("/api/manager")
public class ManagerTimeSheetController {

    @Autowired
    private TimeSheetApprovalService service;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/detailed")
    public ResponseEntity<List<ManagerDetailedDTO>> getDetailedTimesheets(@CurrentUser UserDTO manager) {
        List<ManagerDetailedDTO> detailedList = service.getManagerDetailedData(manager.getId());
        return ResponseEntity.ok(detailedList);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/filter") // BY Status
    public ResponseEntity<List<ManagerDetailedDTO>> getRecentTimesheetsByManager(
            @RequestParam(required = false) String approvalStatus,
            @CurrentUser UserDTO manager) {
        List<ManagerDetailedDTO> results = service.getRecentTimesheets(manager.getId(), approvalStatus);
        return ResponseEntity.ok(results);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/bulk")
    public ResponseEntity<String> updateSelectedTimesheetStatuses(
            @RequestParam("status") String status,
            @RequestBody List<Long> timesheetIds,
            @CurrentUser UserDTO manager) {
        // if (!"APPROVED".equalsIgnoreCase(status) &&
        // !"REJECTED".equalsIgnoreCase(status)) {
        // return ResponseEntity.badRequest().body("Invalid status. Use APPROVED or
        // REJECTED only.");
        // }

        int updated = service.bulkUpdateSelectedApprovals(manager.getId(), status.toUpperCase(), timesheetIds);

        return ResponseEntity.ok(updated + " timesheets updated to: " + status.toUpperCase());
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/approve")
    public ResponseEntity<String> updateApprovalStatus(
            @CurrentUser UserDTO manager,
            @RequestBody ApprovalStatusUpdateRequest request) {
        service.updateApprovalStatus(manager.getId(), request.getUserId(), request.getTimesheetId(),
                request.getStatus());
        return ResponseEntity.ok("Status updated successfully");
    }

}
