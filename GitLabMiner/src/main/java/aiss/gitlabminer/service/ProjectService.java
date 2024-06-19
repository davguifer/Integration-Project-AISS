package aiss.gitlabminer.service;

import aiss.gitlabminer.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CommitService commitService;

    @Autowired
    IssueService issueService;

    @Autowired
    CommentService commentService;

    public List<ProjectGitLab> findAllProjects() {

        String uri = "https://gitlab.com/api/v4/projects";
        ProjectGitLab[] projectsGitLab = restTemplate.getForObject(uri, ProjectGitLab[].class);
        return Arrays.stream(projectsGitLab).toList();
    }

    public ProjectGitLab findOne(Integer id) {
        String uri = "https://gitlab.com/api/v4/projects/" + id.toString();
        ProjectGitLab projectsGitLab = restTemplate.getForObject(uri, ProjectGitLab.class);
        return projectsGitLab;
    }

    public ProjectMiner getProject(Integer id, Integer maxPages) {

        String uri = "https://gitlab.com/api/v4/projects/" + id.toString();
        ProjectGitLab projectsGitLab = restTemplate.getForObject(uri, ProjectGitLab.class);
        ProjectMiner projectMiner = new ProjectMiner(projectsGitLab.getId().toString(), projectsGitLab.getName(), projectsGitLab.getWebUrl());

        //Add commits
        List<CommitGitLab> commitsGitLab = commitService.findProjectCommits(projectMiner.getId(), maxPages);
        List<CommitMiner> commits = new ArrayList<>();
        for (CommitGitLab commitGitLab: commitsGitLab) {
            CommitMiner newCommit = new CommitMiner(
                    commitGitLab.getId(),
                    commitGitLab.getTitle(),
                    commitGitLab.getMessage(),
                    commitGitLab.getAuthorName(),
                    commitGitLab.getAuthorEmail(),
                    commitGitLab.getAuthoredDate(),
                    commitGitLab.getCommitterName(),
                    commitGitLab.getCommitterEmail(),
                    commitGitLab.getCommittedDate(),
                    commitGitLab.getWebUrl()
            );
            commits.add(newCommit);
        }
        projectMiner.setCommits(commits);

        //Add issues

        List<IssueGitLab> issuesGitLab = issueService.findByProjectId(projectMiner.getId(), maxPages);
        List<IssueMiner> issues = new ArrayList<>();
        for (IssueGitLab issueGitLab: issuesGitLab) {
            String closed = null;
            if (issueGitLab.getClosedAt() != null){
                closed = issueGitLab.getClosedAt().toString();
            }
            IssueMiner newIssue = new IssueMiner(issueGitLab.getId().toString(),
                    issueGitLab.getIid().toString(),
                    issueGitLab.getTitle(),
                    issueGitLab.getDescription(),
                    issueGitLab.getState(),
                    issueGitLab.getCreatedAt(),
                    issueGitLab.getUpdatedAt(),
                    closed,
                    issueGitLab.getLabels(),
                    issueGitLab.getAuthor(),
                    issueGitLab.getAssignee(),
                    issueGitLab.getUpvotes(),
                    issueGitLab.getDownvotes(),
                    issueGitLab.getWebUrl());
            //Add comments
            List<CommentGitLab> commentsGitLab = commentService.findByProjectIssue(projectMiner.getId(), newIssue.getRefId().toString(), maxPages);
            List<CommentMiner> comments = new ArrayList<>();
            for (CommentGitLab commentGitLab: commentsGitLab) {
                CommentMiner newComment = new CommentMiner(
                        commentGitLab.getId().toString(),
                        commentGitLab.getBody(),
                        commentGitLab.getCreatedAt(),
                        commentGitLab.getUpdatedAt(),
                        commentGitLab.getAuthor()
                );
                comments.add(newComment);
            }
            newIssue.setComments(comments);
            issues.add(newIssue);
        }
        projectMiner.setIssues(issues);
        return projectMiner;
    }

}
