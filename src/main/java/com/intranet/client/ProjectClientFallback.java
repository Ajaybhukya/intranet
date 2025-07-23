package com.intranet.client;

import com.intranet.dto.external.ProjectDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
// @Profile("mock")
public class ProjectClientFallback implements ProjectClient {

    @Override
    public List<ProjectDTO> getProjectsByUserId(Long userId) {
        System.out.println("Fallback triggered for userId: " + userId);  // Optional: for debugging
        return Arrays.asList(
            new ProjectDTO(101L, "Mock Project A", "Description A"),
            new ProjectDTO(102L, "Mock Project B", "Description B")
        );
    }
}
