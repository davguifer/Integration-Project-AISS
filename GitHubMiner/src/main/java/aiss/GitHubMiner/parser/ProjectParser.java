package aiss.GitHubMiner.parser;

import aiss.GitHubMiner.model.*;
import aiss.GitHubMiner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectParser {
    RestTemplate template= new RestTemplate();

    @Autowired
    ProjectService projectService;
    @Autowired
    CommitService commitService;
    @Autowired
    IssueService issueService;

    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    List<ProjectParse> projects = new ArrayList<>();


    public ProjectParse parseProject(Project project, Integer maxPages, Long sinceIssues, Long sinceCommits){
        List<Commit> commits= commitService.findAllCommits(project.getOwner().getLogin(),project.getName(),maxPages,sinceCommits);
        List<Issue> issues= issueService.findAllIssuesRepo(project.getOwner().getLogin(),project.getName(),maxPages,sinceIssues);

        List<CommitParse> parsedCommits= new ArrayList<>();
        List<IssueParse> parsedIssues= new ArrayList<>();

        for(Commit c: commits){
            parsedCommits.add(parseCommit(c));
        }
        for(Issue i: issues){
            parsedIssues.add(parseIssue(i,maxPages));
        }

        ProjectParse projectParse = new ProjectParse(
                project.getId().toString(),
                project.getName(),
                project.getHtmlUrl(),
                parsedCommits,
                parsedIssues
        );

        projects.add(projectParse);
        return projectParse;
    }

    public void update(ProjectParse updatedProject, String id){
        ProjectParse existingProject= findOne(id);
        int i= projects.indexOf(existingProject);
        updatedProject.setId(existingProject.getId());
        projects.set(i,updatedProject);
    }

    public void delete(String id){
        projects.removeIf(p -> p.getId().equals(id));
    }

    public List<ProjectParse> findAll(){return  projects;}

    public  ProjectParse findOne(String id){
        return projects.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }




    public CommitParse parseCommit(Commit commit){
        CommitParse res= new CommitParse(commit.getNodeId()
                ,commit.getCommit().getMessage()
                ,commit.getCommit().getMessage()
                ,commit.getCommit().getAuthor().getName()
                ,commit.getCommit().getAuthor().getEmail()
                ,commit.getCommit().getAuthor().getDate().toString()
                ,commit.getCommit().getCommitter().getName()
                ,commit.getCommit().getCommitter().getEmail()
                ,commit.getCommit().getCommitter().getDate().toString()
                ,commit.getHtmlUrl());
        return res;
    }

    public IssueParse parseIssue(Issue issue,Integer maxPages){
        List<CommentParse> parsedComments= new ArrayList<>();
        List<Comment> comments= commentService.findAllCommentsOfIssueWithLink(issue.getCommentsUrl(),maxPages);
        for(Comment c: comments){
            parsedComments.add(parseComment(c));
        }
        IssueParse res= new IssueParse(issue.getId().toString()
                ,issue.getNodeId()
                ,issue.getTitle(),
                issue.getBody(),
                issue.getState(),
                issue.getCreatedAt(),
                issue.getUpdatedAt(),
                issue.getClosedAt(),
                parseStringLabels(issue.getLabels()),
                parseUser(issue.getUser()),
                parseUser(issue.getAssignee()),
                issue.getReactions().getPositive(),
                issue.getReactions().getNegative(),
                parsedComments
        );
        return res;
    }

    public UserParse parseUser(User user){
        UserParse res;
        if(user ==null){
            res= new UserParse("Non available","Non available","Non available","Non available","Non available");
        }else {
            res = new UserParse(
                    user.getId().toString(),
                    user.getLogin(),
                    "Name not available in GitHub",
                    user.getAvatarUrl(),
                    user.getHtmlUrl()

            );
        }
        return res;
    }
    public CommentParse parseComment(Comment comment){
        CommentParse res= new CommentParse(
                comment.getId().toString(),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                parseUser(comment.getUser())
        );
        return res;
    }

    public List<String> parseStringLabels(List<Label> labels){
        List<String> res= new ArrayList<>();
        for(Label l:labels){
            String s= l.toString();
            res.add(s);
        }
        return res;
    }
}
