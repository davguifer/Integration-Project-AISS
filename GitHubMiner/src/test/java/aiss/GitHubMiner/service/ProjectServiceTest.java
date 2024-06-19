package aiss.GitHubMiner.service;

import aiss.GitHubMiner.model.Project;
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
    void findAllProjectsOfOwner() {
        List<Project> projects = service.findAllProjectsOfOwner("octocat");
        assertTrue(!projects.isEmpty(),"The list is empty");
        System.out.println(projects);
        projects.forEach( p->{
            System.out.println(p);
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        });
    }

    @Test
    void findProjectByName() {
        Project project = service.findProjectByName("octocat","Hello-World");
        System.out.println(project);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    @Test
    void findProjectById() {
        Project project = service.findProjectById("1296269");
        System.out.println(project);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
    }
}