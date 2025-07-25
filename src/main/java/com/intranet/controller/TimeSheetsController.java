package com.intranet.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intranet.dto.TimeSheetEntryDTO;
import com.intranet.dto.TimeSheetResponseDTO;
import com.intranet.service.TimeSheetService;

@RestController
@RequestMapping("/api/timesheet")
public class TimeSheetsController {

    @Autowired
    TimeSheetService timeSheetService;

   @PostMapping("/{userId}")
public ResponseEntity<String> submitTimeSheet(
        @PathVariable Long userId,
        @RequestParam(value = "workDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDate,
        @RequestBody List<TimeSheetEntryDTO> entries
) {
    // If no workDate is passed, use today's date
    if (workDate == null) {
        workDate = LocalDate.now();
    }

    timeSheetService.createTimeSheetWithEntriesAndApproval(userId, workDate, entries);
    return ResponseEntity.ok("Timesheet submitted successfully.");
}

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<TimeSheetResponseDTO>> getTimeSheetHistory(@PathVariable Long userId) {
        List<TimeSheetResponseDTO> history = timeSheetService.getUserTimeSheetHistory(userId);
        return ResponseEntity.ok(history);
    }

}
