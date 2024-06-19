package aiss.GitHubMiner.service;

import aiss.GitHubMiner.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService service;

    @Test
    @DisplayName("Get all users")
    void findAllUsers() {
        List<User> users = service.findAllUsers();
        assertTrue(!users.isEmpty(), "The list is empty");
        users.forEach(u ->{
            System.out.println(u);
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        });
    }

    @Test
    void findUser(){
        User user = service.findUser("octocat");
        System.out.println(user);
    }
}