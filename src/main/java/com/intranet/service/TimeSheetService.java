package com.intranet.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dto.TeamTimeSheetDTO;
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



     public void createTimeSheetWithEntriesAndApproval(Long userId, LocalDate workDate, List<TimeSheetEntryDTO> entriesDto) {
        // Step 1: Create or get existing TimeSheet
        TimeSheet timesheet = timeSheetRepository.findByUserIdAndWorkDate(userId, workDate)
                .orElseGet(() -> {
                    TimeSheet ts = new TimeSheet();
                    ts.setUserId(userId);
                    ts.setWorkDate(workDate);
                    return timeSheetRepository.save(ts);
                });

        // Step 2: Save all entries
        for (TimeSheetEntryDTO dto : entriesDto) {
            TimeSheetEntry entry = new TimeSheetEntry();
            entry.setTimesheet(timesheet);
            entry.setProjectId(dto.getProjectId());
            entry.setTaskId(dto.getTaskId());
            entry.setDescription(dto.getDescription());
            entry.setWorkType(dto.getWorkType());
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



    public List<TimeSheetResponseDTO> getUserTimeSheetHistory(Long userId) {
    List<TimeSheet> timesheets = timeSheetRepository.findByUserIdOrderByWorkDateDesc(userId);

    return timesheets.stream().map(ts -> {
        TimeSheetResponseDTO dto = new TimeSheetResponseDTO();
        dto.setTimesheetId(ts.getId());
        dto.setWorkDate(ts.getWorkDate());
        dto.setCreatedAt(ts.getCreatedAt());

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

}
