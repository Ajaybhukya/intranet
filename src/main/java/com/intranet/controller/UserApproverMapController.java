package com.intranet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.intranet.dto.UserApproverMapDTO;
import com.intranet.service.UserApproverMapService;

@RestController
@RequestMapping("/api/user-approver-map")
public class UserApproverMapController {

    @Autowired
    private UserApproverMapService service;

    @GetMapping("/approver/{approverId}")
    public List<UserApproverMapDTO> getByApprover(@PathVariable Long approverId) {
        return service.getMappingsByApproverId(approverId);
    }

        @GetMapping("/user/{userId}")
    public List<UserApproverMapDTO> getByUser(@PathVariable Long userId) {
        return service.getMappingsByUserId(userId);
    }


    @PostMapping("/create")
    public UserApproverMapDTO create(@RequestBody UserApproverMapDTO dto) {
        return service.createMapping(dto);
    }
}
