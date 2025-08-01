package com.intranet.client;

import com.intranet.dto.external.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
    name = "projectClient",
    url = "${project.service.url}",
    fallback = ProjectClientFallback.class
)
public interface ProjectClient {

    @GetMapping("/projects/user/{userId}")
    List<ProjectDTO> getProjectsByUserId(@PathVariable("userId") Long userId);
}
