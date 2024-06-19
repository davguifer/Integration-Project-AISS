package aiss.GitHubMiner.controller;

import aiss.GitHubMiner.model.*;
import aiss.GitHubMiner.parser.ProjectParser;
import aiss.GitHubMiner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("/github")
public class ProjectController {

    private final ProjectParser repository;

    RestTemplate template= new RestTemplate();

    @Autowired
    ProjectService projectService;

    public  ProjectController(ProjectParser repository){this.repository=repository;}


    //Get http://localhost:8082/github/{owner}/{repoName}
    // For testing
    @GetMapping("/{owner}/{repoName}")
    public ProjectParse findOne(@PathVariable String owner, @PathVariable String repoName,
                                @RequestParam(defaultValue = "5") Long sinceCommits,
                                @RequestParam(defaultValue = "30") Long sinceIssues,
                                @RequestParam(defaultValue = "2")Integer maxPages){
        Project project= projectService.findProjectByName(owner,repoName);
        return repository.parseProject(project,maxPages,sinceCommits,sinceIssues);
    }

    //POST http://localhost:8082/github/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{owner}/{repoName}")
    public ProjectParse create(@PathVariable String owner, @PathVariable String repoName,@Valid @RequestBody Project project,
                               @RequestParam(defaultValue = "5") Long sinceCommits,
                               @RequestParam(defaultValue = "30") Long sinceIssues,
                               @RequestParam(defaultValue = "2")Integer maxPages) {
        Project projectOnline= projectService.findProjectByName(owner,repoName);
        String uri="http://localhost:8080/gitminer/projects";
        ProjectParse projectParse= repository.parseProject(projectOnline,maxPages,sinceCommits,sinceIssues);
        ProjectParse post= template.postForObject(uri,projectParse,ProjectParse.class);

        return post;
    }

}
