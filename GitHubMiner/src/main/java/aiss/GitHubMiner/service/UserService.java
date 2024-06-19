package aiss.GitHubMiner.service;


import aiss.GitHubMiner.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    RestTemplate restTemplate = new RestTemplate();

    public List<User> findAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ""); // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB
        String uri = "https://api.github.com/users";
        HttpEntity<User[]> request = new HttpEntity<>(headers);
        ResponseEntity<User[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, User[].class);
        User[] users = response.getBody();
        return Arrays.stream(users).toList();
    }

    public User findUser(String username){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ""); // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB
        String uri = "https://api.github.com/users/"+username;
        HttpEntity<User> request = new HttpEntity<>(headers);
        ResponseEntity<User> response = restTemplate.exchange(uri, HttpMethod.GET, request, User.class);
        User user = response.getBody();
        return user;
    }
}
