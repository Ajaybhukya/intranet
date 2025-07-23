package com.intranet.controller;

import com.intranet.dto.external.ProjectDTO;
import com.intranet.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/user/{userId}")
    public List<ProjectDTO> getUserProjects(@PathVariable Long userId) {
        return projectService.getUserProjects(userId);
    }
}
