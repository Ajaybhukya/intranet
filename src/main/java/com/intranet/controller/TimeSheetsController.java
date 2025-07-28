package com.intranet.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.dto.TimeSheetEntryDTO;
import com.intranet.dto.TimeSheetResponseDTO;
import com.intranet.dto.UserDTO;
import com.intranet.security.CurrentUser;
import com.intranet.service.TimeSheetService;

@RestController
@RequestMapping("/api/timesheet")
public class TimeSheetsController {

    @Autowired
    TimeSheetService timeSheetService;

    @PostMapping
    public ResponseEntity<String> submitTimeSheet(
            @RequestParam(value = "workDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDate,
            @RequestBody List<TimeSheetEntryDTO> entries,
            @CurrentUser UserDTO user) {
        // If no workDate is passed, use today's date
        if (workDate == null) {
            workDate = LocalDate.now();
        }

        try {
            timeSheetService.createTimeSheetWithEntriesAndApproval(user.getId(), workDate, entries);
            return ResponseEntity.ok("Timesheet submitted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<TimeSheetResponseDTO>> getTimeSheetHistory(
            @CurrentUser UserDTO user) {
        List<TimeSheetResponseDTO> history = timeSheetService.getUserTimeSheetHistory(user.getId());
        return ResponseEntity.ok(history);
    }

    @PutMapping
    public ResponseEntity<String> updateTimeSheet(
            @RequestBody TimeSheetResponseDTO timeSheetDto,
            @CurrentUser UserDTO user) {
        try {
            timeSheetService.updateTimeSheet(user.getId(), timeSheetDto);
            return ResponseEntity.ok("Timesheet updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
