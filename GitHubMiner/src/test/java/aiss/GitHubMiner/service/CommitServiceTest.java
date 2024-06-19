package aiss.GitHubMiner.service;



import aiss.GitHubMiner.model.Commit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CommitServiceTest {
    @Autowired
    CommitService service;

    @Test
    @DisplayName("Get all commits")
    void findAllCommits(){
        List<Commit> commits = service.findAllCommits("octocat", "Hello-World", 5);
        assertTrue(!commits.isEmpty(),"The list is empty");
        commits.forEach( c->{
            System.out.println(c);
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        });
    }
    @Test
    @DisplayName("Get commit by id")
    void findCommitById(){
        Commit commit = service.findCommitById("7fd1a60b01f91b314f59955a4e4d4e80d8edf11d" ,"octocat", "Hello-World");
        System.out.println(commit);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }
}