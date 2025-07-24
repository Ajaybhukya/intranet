package com.intranet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.client.ProjectClient;
import com.intranet.dto.TimeSheetEntryDTO;
import com.intranet.dto.TimeSheetHistoryDTO;
import com.intranet.dto.external.ProjectDTO;
import com.intranet.dto.external.TaskDTO;
import com.intranet.dto.external.UserDTO;
import com.intranet.entity.TimeSheetEntry;
import com.intranet.service.TimeSheetService;

@RestController
@RequestMapping("/timesheet")
public class TimeSheetsController {

    @Autowired
    private TimeSheetService timesheetService;

    @Autowired
    private ProjectClient projectClient;

    // Endpoint to get timesheet history for logged-in user
    @GetMapping("/history")
    public ResponseEntity<List<TimeSheetHistoryDTO>> getMyTimesheetHistory(
            @AuthenticationPrincipal UserDTO user) {
        List<TimeSheetHistoryDTO> history = timesheetService.getTimesheetHistoryForUser(user.getId());
        return ResponseEntity.ok(history);
    }

    // Endpoint to log a timesheet entry
    @PostMapping("/log-entry")
    public ResponseEntity<TimeSheetEntry> logEntry(@AuthenticationPrincipal UserDTO user,
                                                   @RequestBody TimeSheetEntryDTO entryDto) {
        TimeSheetEntry savedEntry = timesheetService.logTimesheetEntry(user.getId(), entryDto);
        return ResponseEntity.ok(savedEntry);
    }

    // Endpoint to get projects for the dropdown
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getProjects(@AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok(projectClient.getProjectsByUserId(user.getId()));
    }

    // Endpoint to get tasks for a given project ID
    @GetMapping("/tasks/{projectId}")
    public ResponseEntity<List<TaskDTO>> getTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectClient.getTasksByProjectId(projectId));
    }

    @PostMapping("/edit-entry/{entryId}")
    public ResponseEntity<TimeSheetEntryDTO> editEntry(@AuthenticationPrincipal UserDTO user,
                                                    @PathVariable Long entryId,
                                                    @RequestBody TimeSheetEntryDTO entryDto) {
        TimeSheetEntry updatedEntry = timesheetService.editTimesheetEntry(user.getId(), entryId, entryDto);
        // return updated entry dto
        TimeSheetEntryDTO updatedEntryDto = new TimeSheetEntryDTO();
        updatedEntryDto.setProjectId(updatedEntry.getProjectId());
        updatedEntryDto.setTaskId(updatedEntry.getTaskId());
        updatedEntryDto.setDescription(updatedEntry.getDescription());
        updatedEntryDto.setWorkType(updatedEntry.getWorkType());
        updatedEntryDto.setHoursWorked(updatedEntry.getHoursWorked());
        return ResponseEntity.ok(updatedEntryDto);
        
    }
}
