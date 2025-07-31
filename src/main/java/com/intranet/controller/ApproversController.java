package com.intranet.controller;

import com.intranet.dto.ApproverDTO;
import com.intranet.dto.UserSDTO;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/approversList")
public class ApproversController {

    @GetMapping("/mock")
    public List<ApproverDTO> getMockApprovers() {
        return Arrays.asList(
            new ApproverDTO(110L, "Alice Johnson"),
            new ApproverDTO(112L, "Bob Smith"),
            new ApproverDTO(113L, "Carol Williams"),
            new ApproverDTO(114L, "David Lee"),
            new ApproverDTO(115L, "Eva Brown"),
            new ApproverDTO(110L, "Alice Johnson"),
            new ApproverDTO(112L, "Bob Smith"),
            new ApproverDTO(113L, "Carol Williams"),
            new ApproverDTO(114L, "David Lee"),
            new ApproverDTO(115L, "Eva Brown")

        );
    }
}
