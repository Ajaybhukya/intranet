package com.intranet.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.intranet.dto.external.ProjectDTO;
import com.intranet.dto.external.TaskDTO;

@FeignClient(name = "project-management", url = "${project.service.url}",fallback = ProjectClientFallback.class)
public interface ProjectClient {

    @GetMapping("/projects/user/{userId}")
    List<ProjectDTO> getProjectsByUserId(@PathVariable("userId") Integer userId);

    @GetMapping("/tasks/project/{projectId}")
    List<TaskDTO> getTasksByProjectId(@PathVariable("projectId") Long projectId);
}
