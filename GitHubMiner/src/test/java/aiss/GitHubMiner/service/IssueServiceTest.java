package aiss.GitHubMiner.service;

import aiss.GitHubMiner.model.Issue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class IssueServiceTest {
    @Autowired
    IssueService service;
    @Test
    @DisplayName("Get all issues")
    void findAllIssuesRepo() {
        List<Issue> issues= service.findAllIssuesRepo("octocat","Hello-World",10);
        assertTrue(!issues.isEmpty(),"The list is empty");
        issues.forEach(i->{
            System.out.println(i);
            System.out.println("-------------------------------------------------------------------------------------------------------------");
        });
    }

    @Test
    void findIssueByNumber() {
        Issue issue= service.findIssueByNumber("octocat","Hello-World","2634");
        System.out.println(issue);
        System.out.println("----------------------------------------------------------------------------------------------------------------");
    }
}