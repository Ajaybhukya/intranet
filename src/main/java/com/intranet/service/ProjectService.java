package com.intranet.service;

import com.intranet.client.ProjectClient;
import com.intranet.dto.external.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectClient projectClient;

    public List<ProjectDTO> getUserProjects(Long userId) {
        return projectClient.getProjectsByUserId(userId);
    }
}
