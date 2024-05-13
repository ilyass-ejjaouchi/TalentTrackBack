package com.talenttrack.service;

import com.talenttrack.entities.Project;
import com.talenttrack.entities.User;
import com.talenttrack.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Integer id, Project project) {
        project.setId(id);
        return projectRepository.save(project);
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }

    public Project addUserToProject(User user, Integer projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.getUsers().add(user);
            return projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Project not found");
        }
    }
}
