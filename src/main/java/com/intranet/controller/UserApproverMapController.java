package com.intranet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.intranet.dto.ApproverUserListDTO;
import com.intranet.dto.UserApproverIdListDTO;
import com.intranet.dto.UserApproverMapDTO;
import com.intranet.dto.UserApproverSummaryDTO;
import com.intranet.service.UserApproverMapService;

@RestController
@RequestMapping("/api/user-approver-map")
public class UserApproverMapController {

    @Autowired
    private UserApproverMapService service;

    @GetMapping("/approver/{approverId}")
    public ApproverUserListDTO getUsersByApprover(@PathVariable Long approverId) {
        return service.getUsersMappedToApprover(approverId);
    }


    @GetMapping("/user/{userId}")
    public UserApproverIdListDTO getApproverIds(@PathVariable Long userId) {
    return service.getApproverIdsByUserId(userId);
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public UserApproverMapDTO create(@RequestBody UserApproverMapDTO dto) {
        return service.createMapping(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUserMappings(@PathVariable Long userId) {
        service.deleteByUserId(userId);
        return ResponseEntity
                .ok("Deleted all UserApproverMap records and related TimeSheetApprovals for userId: " + userId);
    }

    @GetMapping("/api/user-approver-summary")
    public List<UserApproverSummaryDTO> getUserApproverSummary() {
        return service.getUserApproverSummary();
    }

    @DeleteMapping("/delete/{approverId}/{deleteId}")
    public ResponseEntity<String> deleteUserApproverMapping(
            @PathVariable Long approverId,
            @PathVariable Long deleteId) {
        
        boolean deleted = service.deleteMapping(approverId, deleteId);
        
        if (deleted) {
            return ResponseEntity.ok("Mapping deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Mapping not found.");
        }
    }
}