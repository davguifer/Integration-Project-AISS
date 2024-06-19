package aiss.gitlabminer.service;


import aiss.gitlabminer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RestTemplate restTemplate;
    public List<User> findAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+""); // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITLAB
        String uri = "https://gitlab.com/api/v4/users";
        HttpEntity<User[]> request = new HttpEntity<User[]>(headers);
        ResponseEntity<User[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, User[].class);
        User[] users = response.getBody();
        return Arrays.stream(users).toList();
    }

}
