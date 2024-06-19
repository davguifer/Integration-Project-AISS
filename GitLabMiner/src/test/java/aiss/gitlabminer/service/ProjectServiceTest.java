package aiss.gitlabminer.service;

import aiss.gitlabminer.model.ProjectMiner;
import aiss.gitlabminer.model.ProjectGitLab;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService service;

    @Test
    @DisplayName("Get all projects")
    void findAllProjects() {
        List<ProjectGitLab> projects = service.findAllProjects();
        assertTrue(!projects.isEmpty(), "Projects is empty!");
        projects.forEach(p -> {
            System.out.println(p);
            System.out.println("==========================================================================");
        });
    }

    @Test
    @DisplayName("Get project with id = 45790011")
    void findProject() {
        ProjectGitLab project = service.findOne(45790011);
        System.out.println(project);
    }

    @Test
    @DisplayName("Get complete project with id = 4207231")
    void getProjectTest() {
        ProjectMiner project = service.getProject(4207231, 1);
        System.out.println(project);
    }
}