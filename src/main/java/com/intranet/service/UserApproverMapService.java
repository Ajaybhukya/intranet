package com.intranet.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.controller.UserController;
import com.intranet.dto.ApproverDTO;
import com.intranet.dto.ApproverUserListDTO;
import com.intranet.dto.UserApproverIdListDTO;
import com.intranet.dto.UserApproverMapDTO;
import com.intranet.dto.UserApproverSummaryDTO;
import com.intranet.dto.UserSDTO;
import com.intranet.dto.UserSDTO;
import com.intranet.entity.TimeSheet;
import com.intranet.entity.TimeSheetApproval;
import com.intranet.entity.UserApproverMap;
import com.intranet.repository.TimeSheetApprovalRepo;
import com.intranet.repository.TimeSheetRepo;
import com.intranet.repository.UserApproverMapRepo;

import jakarta.transaction.Transactional;

@Service
public class UserApproverMapService {


    @Autowired
    private UserApproverMapRepo repo;

    @Autowired
    private TimeSheetApprovalRepo timeSheetApprovalRepo;

    @Autowired
    private TimeSheetRepo timeSheetRepo;

    public List<UserApproverMapDTO> getMappingsByApproverId(Long approverId) {
        List<UserApproverMap> mappings = repo.findByApproverId(approverId);

    return mappings.stream()
        .map(mapping -> new UserApproverMapDTO(
            mapping.getUserApproverMapId(),
            mapping.getUserId(),
            mapping.getApproverId(),
            mapping.getRolePriority()
        ))
        .collect(Collectors.toList());
    }
    
    public UserApproverMapDTO createMapping(UserApproverMapDTO dto) {UserApproverMap entity = new UserApproverMap();
    entity.setUserId(dto.getUserId());
    entity.setApproverId(dto.getApproverId());
    entity.setRolePriority(dto.getRolePriority());

    UserApproverMap savedEntity = repo.save(entity);

    UserApproverMapDTO response = new UserApproverMapDTO();
    response.setUserApproverMapId(savedEntity.getUserApproverMapId());
    response.setUserId(savedEntity.getUserId());
    response.setApproverId(savedEntity.getApproverId());
    response.setRolePriority(savedEntity.getRolePriority());

    return response;
    }
    public List<UserApproverMapDTO> getMappingsByUserId(Long userId) {
    List<UserApproverMap> mappings = repo.findByUserId(userId);
    
    return mappings.stream().map(map -> {
        UserApproverMapDTO dto = new UserApproverMapDTO();
        dto.setUserApproverMapId(map.getUserApproverMapId());
        dto.setUserId(map.getUserId());
        dto.setApproverId(map.getApproverId());
        dto.setRolePriority(map.getRolePriority());
        return dto;
    }).collect(Collectors.toList());
    }
    

     public void deleteByUserId(Long userId) {
        List<UserApproverMap> mappings = repo.findByUserId(userId);
        repo.deleteAll(mappings);
        // Due to CascadeType.ALL + orphanRemoval=true on timeSheetApprovals, all related TimeSheetApproval records will be deleted too.
    }

    
    
public List<UserApproverSummaryDTO> getUserApproverSummary() {

    List<UserSDTO> mockUsers = UserController.getAllMockUsers();
    // 🔹 Mock users
    // List<UserSDTO> mockUsers = List.of(
    //     new UserSDTO(1L, "Ajay Kumar", "ajay@example.com"),
    //     new UserSDTO(2L, "Sonal Mehta", "sonal@example.com"),
    //     new UserSDTO(3L, "Rahul Sharma", "rahul@example.com"),
    //     new UserSDTO(4L, "Nikita Das", "nikita@example.com"),
    //     new UserSDTO(101L, "Pankaj Kumar", "pankaj@example.com"),
    //     new UserSDTO(102L, "Amit Kumar", "amit@example.com"),
    //     new UserSDTO(103L, "Rohit Sharma", "rohit@example.com"),
    //     new UserSDTO(110L, "Alice Johnson", "alice.johnson@example.com"),
    //     new UserSDTO(112L, "Bob Smith", "bob.smith@example.com"),
    //     new UserSDTO(113L, "Carol Williams", "carol.williams@example.com"),
    //     new UserSDTO(114L, "David Lee", "david.lee@example.com"),
    //     new UserSDTO(115L, "Eva Brown", "eva.brown@example.com")
    // );

    // 🔹 Index users by ID for quick lookup
    Map<Long, String> userIdToNameMap = mockUsers.stream()
        .collect(Collectors.toMap(UserSDTO::getId, UserSDTO::getName));

    // 🔹 Fetch mappings from DB
    List<UserApproverMap> allMappings = repo.findAll();

    // 🔹 Group approverIds by userId
    Map<Long, List<Long>> userToApproverIds = allMappings.stream()
        .collect(Collectors.groupingBy(
            UserApproverMap::getUserId,
            Collectors.mapping(UserApproverMap::getApproverId, Collectors.toList())
        ));

    // 🔹 Build summary list
    return mockUsers.stream().map(user -> {
        List<Long> approverIds = userToApproverIds.get(user.getId());

        List<ApproverDTO> approverList = (approverIds != null && !approverIds.isEmpty())
            ? approverIds.stream()
                .map(id -> new ApproverDTO(id, userIdToNameMap.getOrDefault(id, "Unknown")))
                .toList()
            : Collections.emptyList();

        return new UserApproverSummaryDTO(user.getId(), user.getName(), approverList);
    }).toList();
}


public UserApproverIdListDTO getApproverIdsByUserId(Long userId) {
    List<UserApproverMap> mappings = repo.findByUserId(userId);

    List<Long> approverIds = mappings.stream()
        .map(UserApproverMap::getApproverId)
        .collect(Collectors.toList());

    return new UserApproverIdListDTO(userId, approverIds);
}


// public UserApproverIdListDTO getApproverListByUserId(Long userId) {
//     // 🔹 Mock users
//     List<UserSDTO> mockUsers = List.of(
//         new UserSDTO(1L, "Ajay Kumar", "ajay@example.com"),
//         new UserSDTO(2L, "Sonal Mehta", "sonal@example.com"),
//         new UserSDTO(3L, "Rahul Sharma", "rahul@example.com"),
//         new UserSDTO(4L, "Nikita Das", "nikita@example.com"),
//         new UserSDTO(101L, "Pankaj Kumar", "pankaj@example.com"),
//         new UserSDTO(102L, "Amit Kumar", "amit@example.com"),
//         new UserSDTO(103L, "Rohit Sharma", "rohit@example.com")
//     );

//     // 🔹 Map userId to name
//     Map<Long, String> userIdToName = mockUsers.stream()
//         .collect(Collectors.toMap(UserSDTO::getId, UserDTO::getName));

//     // 🔹 Fetch approvers for the user from DB
//     List<UserApproverMap> mappings = repo.findByUserId(userId);

//     // 🔹 Convert to approver DTOs
//     List<ApproverDTO> approvers = mappings.stream()
//         .map(m -> {
//             Long approverId = m.getApproverId();
//             String name = userIdToName.getOrDefault(approverId, "Unknown");
//             return new ApproverDTO(approverId, name);
//         })
//         .collect(Collectors.toList());

//     return new UserApproverIdListDTO(userId, approvers);
// }

public ApproverUserListDTO getUsersMappedToApprover(Long approverId) {
    List<UserApproverMap> mappings = repo.findByApproverId(approverId);

    List<Long> userIds = mappings.stream()
        .map(UserApproverMap::getUserId)
        .distinct()
        .collect(Collectors.toList());

    return new ApproverUserListDTO(approverId, userIds);
}

// @Transactional
// public boolean deleteMapping(Long approverId, Long deleteId) {
//         Optional<UserApproverMap> mapping = repo.findByUserIdAndApproverId(deleteId, approverId);
//         if (mapping.isPresent()) {
//             repo.deleteByUserIdAndApproverId(deleteId, approverId);
//             return true;
//         }
//         return false;
//     }

    @Transactional
    public boolean deleteMapping(Long approverId, Long userId) {
        Optional<UserApproverMap> optional = repo.findByUserIdAndApproverId(userId, approverId);
        
        if (optional.isEmpty()) return false;

        UserApproverMap map = optional.get();

        // Step 1: Collect all timesheets referenced in approvals
        List<TimeSheet> relatedTimesheets = map.getApprovals().stream()
            .map(TimeSheetApproval::getTimesheet)
            .distinct()
            .toList();

        // Step 2: Delete the UserApproverMap (will cascade and delete approvals)
        repo.delete(map);

        // Step 3: For each timesheet, check if it's orphaned, and delete it
        for (TimeSheet timesheet : relatedTimesheets) {
            boolean stillHasApprovals = timeSheetApprovalRepo.existsByTimesheet(timesheet);
            if (!stillHasApprovals) {
                timeSheetRepo.delete(timesheet); // entries are auto-deleted via cascade
            }
        }

        return true;
    }
    
    public UserApproverSummaryDTO getUserApproverSummaryByUserId(Long userId) {
    // 🔹 Use mock users
    List<UserSDTO> mockUsers = UserController.getAllMockUsers();  // Static call to controller method

    // 🔹 Find the user from mock list
    UserSDTO user = mockUsers.stream()
        .filter(u -> u.getId().equals(userId))
        .findFirst()
        .orElse(null);

    if (user == null) {
        throw new IllegalArgumentException("User not found for ID: " + userId);
    }

    // 🔹 Map userId → userName
    Map<Long, String> userIdToNameMap = mockUsers.stream()
        .collect(Collectors.toMap(UserSDTO::getId, UserSDTO::getName));

    // 🔹 Fetch mappings from DB
    List<UserApproverMap> allMappings = repo.findByUserId(userId);

    // 🔹 Extract approver IDs
    List<Long> approverIds = allMappings.stream()
        .map(UserApproverMap::getApproverId)
        .collect(Collectors.toList());

    // 🔹 Convert to DTOs with names using mock data
    List<ApproverDTO> approverList = approverIds.stream()
        .map(id -> new ApproverDTO(id, userIdToNameMap.getOrDefault(id, "Unknown")))
        .collect(Collectors.toList());

    return new UserApproverSummaryDTO(user.getId(), user.getName(), approverList);
}

}