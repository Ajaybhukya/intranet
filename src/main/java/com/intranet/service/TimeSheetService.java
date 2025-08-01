package com.intranet.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dto.TimeSheetEntryDTO;
import com.intranet.dto.TimeSheetEntryResponseDTO;
import com.intranet.dto.TimeSheetResponseDTO;
import com.intranet.entity.TimeSheet;
import com.intranet.entity.TimeSheetApproval;
import com.intranet.entity.TimeSheetEntry;
import com.intranet.entity.UserApproverMap;
import com.intranet.repository.TimeSheetApprovalRepo;
import com.intranet.repository.TimeSheetEntryRepo;
import com.intranet.repository.TimeSheetRepo;
import com.intranet.repository.UserApproverMapRepo;

@Service
public class TimeSheetService {

    @Autowired
    private TimeSheetRepo timeSheetRepository;

    @Autowired
    private TimeSheetEntryRepo timeSheetEntryRepo;

    @Autowired
    private TimeSheetApprovalRepo timeSheetApprovalRepo;

    @Autowired
    private UserApproverMapRepo userApproverMapRepo;

    public void createTimeSheetWithEntriesAndApproval(Long userId, LocalDate workDate,
            List<TimeSheetEntryDTO> entriesDto) {

        // ✅ Step 0: Check if user is assigned to any manager
        List<UserApproverMap> approver = userApproverMapRepo.findByUserId(userId);
        if (approver == null || approver.isEmpty()) {
            throw new IllegalArgumentException("User is not assigned to any manager. Cannot submit timesheet.");
        }

        for (TimeSheetEntryDTO dto : entriesDto) {
            if (dto.getFromTime() != null && dto.getToTime() != null) {
                long minutes = Duration.between(dto.getFromTime(), dto.getToTime()).toMinutes();
                dto.setHoursWorked(BigDecimal.valueOf(minutes / 60.0));
            } else if (dto.getHoursWorked() == null) {
                dto.setHoursWorked(BigDecimal.ZERO); // ✅ Fallback if both time and hours are missing
            }
        }

        // validate entries
        validateEntries(entriesDto);

        // Step 1: Create TimeSheet
        TimeSheet timesheet = new TimeSheet();
        timesheet.setUserId(userId);
        timesheet.setWorkDate(workDate);
        timesheet = timeSheetRepository.save(timesheet);

        // Step 2: Save all entries
        for (TimeSheetEntryDTO dto : entriesDto) {
            TimeSheetEntry entry = new TimeSheetEntry();
            entry.setTimesheet(timesheet);
            entry.setProjectId(dto.getProjectId());
            entry.setTaskId(dto.getTaskId());
            entry.setDescription(dto.getDescription());
            // entry.setWorkType(dto.getWorkType());

            // if (entry.getWorkType() == null || entry.getWorkType().isBlank()) {
            // entry.setWorkType("WFO");
            // } else {
            // entry.setWorkType(dto.getWorkType());
            // }

            String workType = dto.getWorkType();
            if (workType == null || workType.isBlank()) {
                workType = "WFO";
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Work Type: " + workType);
            System.out.println(workType.getClass().getName());
            entry.setWorkType(workType);

            // entry.setWorkType(dto.getWorkType() != null ? dto.getWorkType() : "WFO");
            entry.setFromTime(dto.getFromTime());
            entry.setToTime(dto.getToTime());
            entry.setOtherDescription(dto.getOtherDescription());

            // Calculate hours if from/to provided
            if (dto.getFromTime() != null && dto.getToTime() != null) {
                long hours = Duration.between(dto.getFromTime(), dto.getToTime()).toMinutes();
                entry.setHoursWorked(BigDecimal.valueOf(hours / 60.0));
            } else {
                entry.setHoursWorked(dto.getHoursWorked());
            }

            timeSheetEntryRepo.save(entry);
        }

        // Step 3: Create approvals
        List<UserApproverMap> approverMaps = userApproverMapRepo.findByUserId(userId);
        for (UserApproverMap approverMap : approverMaps) {
            TimeSheetApproval approval = new TimeSheetApproval();
            approval.setTimesheet(timesheet);
            approval.setApprover(approverMap);
            approval.setApprovalStatus("PENDING");
            approval.setDescription("Auto approval created");
            approval.setApprovalTime(LocalDateTime.now());
            timeSheetApprovalRepo.save(approval);
        }
    }

    // public List<TimeSheetResponseDTO> getUserTimeSheetHistory(Long userId) {
    //     List<TimeSheet> timesheets = timeSheetRepository.findByUserIdOrderByWorkDateDesc(userId);

    //     return timesheets.stream().map(ts -> {
    //         TimeSheetResponseDTO dto = new TimeSheetResponseDTO();
    //         dto.setTimesheetId(ts.getId());
    //         dto.setWorkDate(ts.getWorkDate());
    //         dto.setCreatedAt(ts.getCreatedAt());

    //         // Fetch approval status (optional: pick latest or first if multiple)
    //         Optional<TimeSheetApproval> approvalOpt = timeSheetApprovalRepo.findFirstByTimesheet_Id(ts.getId());
    //         dto.setApprovalStatus(approvalOpt.map(TimeSheetApproval::getApprovalStatus).orElse("PENDING"));

    //         // Map entries
    //         List<TimeSheetEntryResponseDTO> entryDTOs = ts.getEntries().stream().map(entry -> {
    //             TimeSheetEntryResponseDTO entryDto = new TimeSheetEntryResponseDTO();
    //             entryDto.setTimesheetEntryId(entry.getTimesheetEntryId());
    //             entryDto.setProjectId(entry.getProjectId());
    //             entryDto.setTaskId(entry.getTaskId());
    //             entryDto.setDescription(entry.getDescription());
    //             entryDto.setWorkType(entry.getWorkType());
    //             entryDto.setFromTime(entry.getFromTime());
    //             entryDto.setToTime(entry.getToTime());
    //             entryDto.setHoursWorked(entry.getHoursWorked());
    //             entryDto.setOtherDescription(entry.getOtherDescription());
    //             return entryDto;
    //         }).toList();

    //         dto.setEntries(entryDTOs);
    //         return dto;
    //     }).toList();
    // }

    public List<TimeSheetResponseDTO> getUserTimeSheetHistory(Long userId) {
    List<TimeSheet> timesheets = timeSheetRepository.findByUserIdOrderByWorkDateDesc(userId);

    return timesheets.stream().map(ts -> {
        TimeSheetResponseDTO dto = new TimeSheetResponseDTO();
        dto.setTimesheetId(ts.getId());
        dto.setWorkDate(ts.getWorkDate());
        dto.setCreatedAt(ts.getCreatedAt());

        List<TimeSheetApproval> approvals = timeSheetApprovalRepo.findByTimesheet_Id(ts.getId());

        // Group manager IDs by their approval status
        List<Long> approvedBy = new ArrayList<>();
        List<Long> rejectedBy = new ArrayList<>();
        List<Long> pendingBy = new ArrayList<>();

        for (TimeSheetApproval approval : approvals) {
            String status = approval.getApprovalStatus();
            Long managerId = approval.getApprover().getApproverId(); // or getApproverId() if flat

            if ("APPROVED".equalsIgnoreCase(status)) {
                approvedBy.add(managerId);
            } else if ("REJECTED".equalsIgnoreCase(status)) {
                rejectedBy.add(managerId);
            } else {
                pendingBy.add(managerId);
            }
        }

        // Determine overall status
        String overallStatus;
        if (!pendingBy.isEmpty()) {
            overallStatus = "PENDING";
        } else if (!rejectedBy.isEmpty()) {
            overallStatus = "REJECTED";
        } else if (!approvedBy.isEmpty() && rejectedBy.isEmpty()) {
            overallStatus = "ACCEPTED";
        } else {
            overallStatus = "PENDING";
        }

        dto.setApprovalStatus(overallStatus);
        dto.setApprovedBy(approvedBy);
        dto.setRejectedBy(rejectedBy);
        dto.setPendingBy(pendingBy);

        // Entries mapping
        List<TimeSheetEntryResponseDTO> entryDTOs = ts.getEntries().stream().map(entry -> {
            TimeSheetEntryResponseDTO entryDto = new TimeSheetEntryResponseDTO();
            entryDto.setTimesheetEntryId(entry.getTimesheetEntryId());
            entryDto.setProjectId(entry.getProjectId());
            entryDto.setTaskId(entry.getTaskId());
            entryDto.setDescription(entry.getDescription());
            entryDto.setWorkType(entry.getWorkType());
            entryDto.setFromTime(entry.getFromTime());
            entryDto.setToTime(entry.getToTime());
            entryDto.setHoursWorked(entry.getHoursWorked());
            entryDto.setOtherDescription(entry.getOtherDescription());
            return entryDto;
        }).toList();

        dto.setEntries(entryDTOs);
        return dto;
    }).toList();
    }


    public void updateTimeSheet(Long user_id, TimeSheetResponseDTO timeSheetDto) {
        TimeSheet timeSheet = timeSheetRepository.findById(timeSheetDto.getTimesheetId())
                .orElseThrow(() -> new IllegalArgumentException("Timesheet not found"));

        // Check if the user is authorized to update this timesheet
        if (!timeSheet.getUserId().equals(user_id)) {
            throw new IllegalArgumentException("You are not authorized to update this timesheet");
        }

        // Check if the timesheet is already approved
        List<TimeSheetApproval> approvals = timeSheetApprovalRepo.findByTimesheetId(timeSheet.getId());
        if (approvals.stream().anyMatch(a -> "APPROVED".equalsIgnoreCase(a.getApprovalStatus()))) {
            throw new IllegalArgumentException("Cannot update an approved timesheet");
        }

        // Validate entries
        List<TimeSheetEntryDTO> entries;
        if (timeSheetDto.getEntries() == null || timeSheetDto.getEntries().isEmpty()) {
            throw new IllegalArgumentException("No entries provided for update");
        } else {
            entries = timeSheetDto.getEntries().stream()
                    .map(entryDto -> {
                        TimeSheetEntryDTO dto = new TimeSheetEntryDTO();
                        dto.setProjectId(entryDto.getProjectId());
                        dto.setTaskId(entryDto.getTaskId());
                        dto.setDescription(entryDto.getDescription());
                        dto.setWorkType(entryDto.getWorkType());
                        dto.setFromTime(entryDto.getFromTime());
                        dto.setToTime(entryDto.getToTime());
                        dto.setHoursWorked(entryDto.getHoursWorked());
                        dto.setOtherDescription(entryDto.getOtherDescription());
                        return dto;
                    }).toList();
        }
        validateEntries(entries);

        // Update work date
        if (timeSheetDto.getWorkDate() != null) {
            timeSheet.setWorkDate(timeSheetDto.getWorkDate());
        }

        // Update entries
        for (TimeSheetEntryResponseDTO entryDto : timeSheetDto.getEntries()) {
            TimeSheetEntry entry = timeSheetEntryRepo.findById(entryDto.getTimesheetEntryId())
                    .orElseThrow(() -> new IllegalArgumentException("Entry not found"));

            entry.setProjectId(entryDto.getProjectId());
            entry.setTaskId(entryDto.getTaskId());
            entry.setDescription(entryDto.getDescription());
            entry.setWorkType(entryDto.getWorkType());
            entry.setFromTime(entryDto.getFromTime());
            entry.setToTime(entryDto.getToTime());
            entry.setOtherDescription(entryDto.getOtherDescription());

            if (entry.getFromTime() != null && entry.getToTime() != null) {
                long hours = Duration.between(entry.getFromTime(), entry.getToTime()).toMinutes();
                entry.setHoursWorked(BigDecimal.valueOf(hours / 60.0));
            } else {
                entry.setHoursWorked(entryDto.getHoursWorked());
            }

            timeSheetEntryRepo.save(entry);
        }

        timeSheetRepository.save(timeSheet);
    }

    private boolean validateEntries(List<TimeSheetEntryDTO> entries) {
        // Implement validation logic for entries check if entries overlap and if the
        // total hours exceed a certain limit

        // total hours should not exceed 24 in a day
        BigDecimal totalHours = entries.stream()
                .map(TimeSheetEntryDTO::getHoursWorked)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalHours.compareTo(BigDecimal.valueOf(24)) > 0) {
            throw new IllegalArgumentException("Total hours worked in a day cannot exceed 24 hours.");
        }
        // Check for overlapping entries
        for (int i = 0; i < entries.size(); i++) {
            TimeSheetEntryDTO entry1 = entries.get(i);
            for (int j = i + 1; j < entries.size(); j++) {
                TimeSheetEntryDTO entry2 = entries.get(j);
                if (entry1.getFromTime() != null && entry1.getToTime() != null &&
                        entry2.getFromTime() != null && entry2.getToTime() != null) {
                    if ((entry1.getFromTime().isBefore(entry2.getToTime()) &&
                            entry1.getToTime().isAfter(entry2.getFromTime())) ||
                            (entry2.getFromTime().isBefore(entry1.getToTime()) &&
                                    entry2.getToTime().isAfter(entry1.getFromTime()))) {
                        throw new IllegalArgumentException("Entries overlap in time.");
                    }
                }
            }
        }

        return true; // Placeholder for actual validation logic
    }
}
