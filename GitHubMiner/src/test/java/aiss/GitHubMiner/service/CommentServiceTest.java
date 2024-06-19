package aiss.GitHubMiner.service;

import aiss.GitHubMiner.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentService service;
    @Test
    @DisplayName("Get all comments of an issue")
    void findAllCommentsOfIssue() {
        List<Comment> comments= service.findAllCommentsOfIssue("octocat","Hello-World","2",10);
        assertTrue(!comments.isEmpty(), "The list is empty");
        comments.forEach( c->{
            System.out.println(c);
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        });
    }

    @Test
    @DisplayName("Search comment by Id")
    void findCommentOfIssueById() {
        Comment comment= service.findCommentOfIssueById("octocat","Hello-World","1146825");
        System.out.println(comment);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }

    @Test
    void findAllCommentsOfRepo() {
        List<Comment> comments= service.findAllCommentsOfRepo("octocat","Hello-World",10);
        assertTrue(!comments.isEmpty(), "The list is empty");
        comments.forEach( c->{
            System.out.println(c);
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        });
    }
}