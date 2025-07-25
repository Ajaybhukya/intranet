
package com.intranet.controller;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.dto.ManagerDetailedDTO;
import com.intranet.dto.TeamTimeSheetDTO;
import com.intranet.dto.TimeSheetApprovalDTO;
import com.intranet.service.TimeSheetApprovalService;
import com.intranet.service.TimeSheetService;
@RestController
@RequestMapping("/api/manager")
public class ManagerTimeSheetController {

    @Autowired
    private TimeSheetService timeSheetService;

    @Autowired
    private TimeSheetApprovalService service;

    @GetMapping("/detailed/{managerId}")
    public ResponseEntity<List<ManagerDetailedDTO>> getDetailedTimesheets(@PathVariable Long managerId) {
        List<ManagerDetailedDTO> detailedList = service.getManagerDetailedData(managerId);
        return ResponseEntity.ok(detailedList);
    }

    @GetMapping("/filter/{managerId}") // BY Status
    public ResponseEntity<List<ManagerDetailedDTO>> getRecentTimesheetsByManager(
            @PathVariable Long managerId,
            @RequestParam(required = false) String approvalStatus
    ) {
        List<ManagerDetailedDTO> results = service.getRecentTimesheets(managerId, approvalStatus);
        return ResponseEntity.ok(results);
    }

        @PutMapping("/bulk/{managerId}") // Bulk approval or rejection
    public ResponseEntity<String> updateTimesheetStatusesByManager(
            @PathVariable Long managerId,
            @RequestParam("status") String status
    ) {
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            return ResponseEntity.badRequest().body("Invalid status. Use APPROVED or REJECTED only.");
        }

        service.bulkUpdateApprovals(managerId, status);
        return ResponseEntity.ok("All pending timesheets updated to: " + status);
    }

}
