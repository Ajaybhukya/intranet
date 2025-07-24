package com.intranet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dto.UserApproverMapDTO;
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
    
}
