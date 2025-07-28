package com.intranet.client;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.intranet.dto.external.ProjectDTO;
import com.intranet.dto.external.TaskDTO;

@Component
@Profile("mock")
public class ProjectClientFallback implements ProjectClient {

    @Override
    public List<ProjectDTO> getProjectsByUserId(Long userId) {
        ProjectDTO mockProject = new ProjectDTO();
        mockProject.setId(1L);
        mockProject.setName("Mock Project");
        return Collections.singletonList(mockProject);
    }

    @Override
    public List<TaskDTO> getTasksByProjectId(Long projectId) {
        TaskDTO mockTask = new TaskDTO();
        mockTask.setId(1L);
        mockTask.setName("Mock Task");
        return Collections.singletonList(mockTask);
    }
}
