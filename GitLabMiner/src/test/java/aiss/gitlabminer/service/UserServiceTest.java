package aiss.gitlabminer.service;

import aiss.gitlabminer.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;
    @Test
    @DisplayName("Get all users")
    void findAllUsers() {
        List<User> users = service.findAllUsers();
        assertTrue(!users.isEmpty(), "No users");
        users.forEach(u -> {
            System.out.println(u);
            System.out.println("***********************************************************************************");
        });
    }
}
