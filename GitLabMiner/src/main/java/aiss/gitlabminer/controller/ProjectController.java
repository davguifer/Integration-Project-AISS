package aiss.gitlabminer.controller;

import aiss.gitlabminer.model.CommitMiner;
import aiss.gitlabminer.model.IssueMiner;
import aiss.gitlabminer.model.ProjectMiner;
import aiss.gitlabminer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/gitlabminer")
public class ProjectController {

    @Autowired
    RestTemplate template;
    @Autowired
    ProjectService projectService;


    //POST http://localhost:8081/gitlabminer/projects/{id}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/projects/{id}")
    public ProjectMiner create(@PathVariable Integer id, @RequestParam(defaultValue = "2") Long sinceCommits,
                               @RequestParam(defaultValue = "20") Long sinceIssues,
                               @RequestParam(defaultValue = "2") Integer maxPages) {

        ProjectMiner proj = projectService.getProject(id, maxPages);

        List<CommitMiner> commits = proj.getCommits().stream()
                .filter(c -> LocalDateTime.parse(c.getCommitted_date().substring(0,23))
                        .isAfter(LocalDateTime.now().minusDays(sinceCommits))).toList();

        List<IssueMiner> issues = proj.getIssues().stream()
                .filter(i -> LocalDateTime.parse(i.getUpdatedAt().substring(0,23))
                        .isAfter(LocalDateTime.now().minusDays(sinceIssues))).toList();

        ProjectMiner filtredProj = new ProjectMiner(proj.getId(), proj.getName(), proj.getWebUrl(), commits, issues);
        String uri = "http://localhost:8080/gitminer/projects";
        ProjectMiner createdProj = template.postForObject(uri, filtredProj, ProjectMiner.class);
        return createdProj;

    }

    //GET http://localhost:8081/gitlabminer/projects/{id}
    //For testing
    @GetMapping("/projects/{id}")
    public ProjectMiner findProject(@PathVariable Integer id, @RequestParam(defaultValue = "2") Long sinceCommits,
                                    @RequestParam(defaultValue = "20") Long sinceIssues,
                                    @RequestParam(defaultValue = "2") Integer maxPages) {
        ProjectMiner proj = projectService.getProject(id, maxPages);

        List<CommitMiner> commits = proj.getCommits().stream()
               .filter(c -> LocalDateTime.parse(c.getCommitted_date().substring(0,23))
                       .isAfter(LocalDateTime.now().minusDays(sinceCommits))).toList();

        List<IssueMiner> issues = proj.getIssues().stream()
                .filter(i -> LocalDateTime.parse(i.getUpdatedAt().substring(0,23))
                        .isAfter(LocalDateTime.now().minusDays(sinceIssues))).toList();

        ProjectMiner filtredProj = new ProjectMiner(proj.getId(), proj.getName(), proj.getWebUrl(), commits, issues);

        return filtredProj;
    }


}
