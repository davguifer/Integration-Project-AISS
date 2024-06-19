package aiss.gitlabminer.service;


import aiss.gitlabminer.model.CommentGitLab;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService service;

    @Test
    @DisplayName("Get all comments of issue 127556169 (iid = 2395) of project 4207231")
    void findIssueComments() {
        List<CommentGitLab> comments = service.findByProjectIssue("4207231", "2395", 1);
        assertTrue(!comments.isEmpty(), "Comments is empty!");
        comments.forEach(c-> {
            System.out.println(c);
            System.out.println("==========================================================================");
        });
    }
}
