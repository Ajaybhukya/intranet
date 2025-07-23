package com.intranet.controller;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.intranet.dto.TimeSheetHistoryDTO;
import com.intranet.dto.external.UserDTO;
import com.intranet.service.TimeSheetService;

public class TimeSheetsController {

    @Autowired
    private TimeSheetService timesheetService;

    @GetMapping("/timesheet/history")
    public ResponseEntity<List<TimeSheetHistoryDTO>> getMyTimesheetHistory(
        @AuthenticationPrincipal UserDTO user) {
        List<TimeSheetHistoryDTO> history = timesheetService.getTimesheetHistoryForUser((user).getId());
        return ResponseEntity.ok(history);
    }

}