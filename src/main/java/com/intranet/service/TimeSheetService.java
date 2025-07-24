package com.intranet.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dto.TimeSheetApprovalDTO;
import com.intranet.dto.TimeSheetEntryDTO;
import com.intranet.dto.TimeSheetHistoryDTO;
import com.intranet.entity.TimeSheet;
import com.intranet.entity.TimeSheetApproval;
import com.intranet.entity.TimeSheetEntry;
import com.intranet.repository.TimeSheetApprovalRepo;
import com.intranet.repository.TimeSheetEntryRepo;
import com.intranet.repository.TimeSheetRepo;

@Service
public class TimeSheetService {

    @Autowired
    private TimeSheetRepo timeSheetRepository;

    @Autowired
    private TimeSheetEntryRepo entryRepository;

    @Autowired
    private TimeSheetApprovalRepo approvalRepository;

    public List<TimeSheetHistoryDTO> getTimesheetHistoryForUser(Long userId) {
        List<TimeSheet> sheets = timeSheetRepository.findByUserIdOrderByWorkDateDesc(userId);
        List<TimeSheetHistoryDTO> history = new ArrayList<>();

        for (TimeSheet sheet : sheets) {
            List<TimeSheetEntry> entries = entryRepository.findByTimesheetId(sheet.getId());
            List<TimeSheetApproval> approvals = approvalRepository.findByTimesheetId(sheet.getId());

            TimeSheetHistoryDTO dto = new TimeSheetHistoryDTO();
            dto.setWorkDate(sheet.getWorkDate());

            dto.setEntries(entries.stream().map(entry -> {
                TimeSheetEntryDTO eDto = new TimeSheetEntryDTO();
                eDto.setProjectId(entry.getProjectId());
                eDto.setTaskId(entry.getTaskId());
                eDto.setDescription(entry.getDescription());
                eDto.setWorkType(entry.getWorkType());
                eDto.setHoursWorked(entry.getHoursWorked());
                return eDto;
            }).toList());

            dto.setApprovals(approvals.stream().map(app -> {
                TimeSheetApprovalDTO aDto = new TimeSheetApprovalDTO();
                aDto.setApprovalStatus(app.getApprovalStatus());
                aDto.setApproverId(app.getApprover().getApproverId()); // Fixed: Use approver from UserApproverMap
                aDto.setApprovalTime(app.getApprovalTime());
                aDto.setDescription(app.getDescription());
                return aDto;
            }).toList());

            history.add(dto);
        }

        return history;
    }

    // New method for logging (saving) a timesheet entry
    public TimeSheetEntry logTimesheetEntry(Long userId, TimeSheetEntryDTO entryDto) {
        TimeSheet timesheet = timeSheetRepository.findByUserIdOrderByWorkDateDesc(userId).stream()
                .findFirst().orElse(new TimeSheet()); // Get or create timesheet
        timesheet.setUserId(userId);
        // Set other timesheet fields as needed
        timeSheetRepository.save(timesheet);

        TimeSheetEntry entry = new TimeSheetEntry();
        entry.setTimesheet(timesheet);
        entry.setProjectId(entryDto.getProjectId());
        entry.setTaskId(entryDto.getTaskId());
        entry.setDescription(entryDto.getDescription()); // Store task description
        entry.setWorkType(entryDto.getWorkType());

        // Calculate hoursWorked if fromTime and toTime are provided
        if (entry.getFromTime() != null && entry.getToTime() != null) {
            long hours = Duration.between(entry.getFromTime(), entry.getToTime()).toHours();
            entry.setHoursWorked(BigDecimal.valueOf(hours));
        } else {
            entry.setHoursWorked(entryDto.getHoursWorked());
        }

        return entryRepository.save(entry);
    }

    // method to edit/update a timesheet entry
    public TimeSheetEntry editTimesheetEntry(Long userId, Long entryId, TimeSheetEntryDTO entryDto) {
        TimeSheetEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        // Ensure the user is authorized to edit this entry
        if (!entry.getTimesheet().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to edit this entry");
        }

        // Check if the entry status is PENDING
        if (!entry.getStatus().equals("PENDING")) {
            throw new RuntimeException("Entry can only be edited if status is PENDING");
        }

        // Update fields
        entry.setProjectId(entryDto.getProjectId());
        entry.setTaskId(entryDto.getTaskId());
        entry.setDescription(entryDto.getDescription());
        entry.setWorkType(entryDto.getWorkType());

        // Recalculate hoursWorked if fromTime and toTime are provided
        if (entry.getFromTime() != null && entry.getToTime() != null) {
            long hours = Duration.between(entry.getFromTime(), entry.getToTime()).toHours();
            entry.setHoursWorked(BigDecimal.valueOf(hours));
        } else {
            entry.setHoursWorked(entryDto.getHoursWorked());
        }

        return entryRepository.save(entry);
    }
    

}
