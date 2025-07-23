// package com.intranet.service;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;

// import com.intranet.dto.TimeSheetApprovalDTO;
// import com.intranet.dto.TimeSheetEntryDTO;
// import com.intranet.dto.TimeSheetHistoryDTO;
// import com.intranet.entity.TimeSheet;
// import com.intranet.entity.TimeSheetApproval;
// import com.intranet.entity.TimeSheetEntry;
// import com.intranet.repository.TimeSheetApprovalRepo;
// import com.intranet.repository.TimeSheetEntryRepo;
// import com.intranet.repository.TimeSheetRepo;

// public class TimeSheetService {

//     @Autowired
//     private TimeSheetRepo timeSheetRepository;

//     @Autowired
//     private TimeSheetEntryRepo entryRepository;

//     @Autowired
//     private TimeSheetApprovalRepo approvalRepository;

//     public List<TimeSheetHistoryDTO> getTimesheetHistoryForUser(Long userId) {
//     List<TimeSheet> sheets = timeSheetRepository.findByUserIdOrderByWorkDateDesc(userId);
//     List<TimeSheetHistoryDTO> history = new ArrayList<>();

//     for (TimeSheet sheet : sheets) {
//         List<TimeSheetEntry> entries = entryRepository.findByTimesheetId(sheet.getTimesheetId());
//         List<TimeSheetApproval> approvals = approvalRepository.findByTimesheetId(sheet.getTimesheetId());

//         TimeSheetHistoryDTO dto = new TimeSheetHistoryDTO();
//         dto.setWorkDate(sheet.getWorkDate());

//         dto.setEntries(entries.stream().map(entry -> {
//             TimeSheetEntryDTO eDto = new TimeSheetEntryDTO();
//             eDto.setProjectId(entry.getProjectId());
//             eDto.setTaskId(entry.getTaskId());
//             eDto.setDescription(entry.getDescription());
//             eDto.setWorkType(entry.getWorkType());
//             eDto.setHoursWorked(entry.getHoursWorked());
//             return eDto;
//         }).toList());

//         dto.setApprovals(approvals.stream().map(app -> {
//             TimeSheetApprovalDTO aDto = new TimeSheetApprovalDTO();
//             aDto.setApprovalStatus(app.getApprovalStatus());
//             aDto.setApproverId(app.getApproveeId());
//             aDto.setApprovalTime(app.getApprovalTime());
//             aDto.setDescription(app.getDescription());
//             return aDto;
//         }).toList());

//         history.add(dto);
//     }

//     return history;
// }

// }
