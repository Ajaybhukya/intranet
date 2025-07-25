
package com.intranet.controller;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/{managerId}/timesheets") // Get all the timesheetid, userid and workdate
    public List<TeamTimeSheetDTO> getTeamTimesheets(
            @PathVariable Long managerId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return timeSheetService.getTeamTimesheets(managerId, date);
    }

    @GetMapping("/{managerId}") // All the timesheet approvals,pending,rejects
    public List<TimeSheetApprovalDTO> getApprovalsByManager(@PathVariable Long managerId) {
        return service.getApprovalsByManager(managerId);
    }
}
