package com.intranet.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dto.ApproverDTO;
import com.intranet.dto.UserApproverMapDTO;
import com.intranet.dto.UserApproverSummaryDTO;
import com.intranet.dto.UserSDTO;
import com.intranet.entity.UserApproverMap;
import com.intranet.repository.UserApproverMapRepo;

@Service
public class UserApproverMapService {


    @Autowired
    private UserApproverMapRepo repo;

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
    // 🔹 Mock users
    List<UserSDTO> mockUsers = List.of(
        new UserSDTO(1L, "Ajay Kumar", "ajay@example.com"),
        new UserSDTO(2L, "Sonal Mehta", "sonal@example.com"),
        new UserSDTO(3L, "Rahul Sharma", "rahul@example.com"),
        new UserSDTO(4L, "Nikita Das", "nikita@example.com"),
        new UserSDTO(101L, "Pankaj Kumar", "pankaj@example.com"),
        new UserSDTO(102L, "Amit Kumar", "amit@example.com"),
        new UserSDTO(103L, "Rohit Sharma", "rohit@example.com")
    );

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
}