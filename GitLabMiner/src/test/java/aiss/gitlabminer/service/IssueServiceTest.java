package aiss.gitlabminer.service;

import aiss.gitlabminer.model.IssueGitLab;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class IssueServiceTest {

    @Autowired
    IssueService service;

    @Test
    @DisplayName("Get all issues of project 4207231")
    void findProjectCommits() {
        List<IssueGitLab> issues = service.findByProjectId("4207231", 1);
        assertTrue(!issues.isEmpty(), "Issues is empty!");
        issues.forEach(c-> {
            System.out.println(c);
            System.out.println("==========================================================================");
        });
    }

}
