package aiss.GitHubMiner.service;

import aiss.GitHubMiner.model.Project;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class ProjectService {

    RestTemplate restTemplate = new RestTemplate();

    public List<Project> findAllProjectsOfOwner(String owner){
        String uri ="https://api.github.com/users/"+owner+"/repos";
        String token= ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB
 
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Project[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Project[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, Project[].class);

        List<Project> projects = new ArrayList<>(Arrays.asList(response.getBody()));

        String nextUrl = getNextPageUrl(response.getHeaders());
        int i = 1;
        while (nextUrl != null) {
            response = restTemplate.exchange(nextUrl, HttpMethod.GET, request, Project[].class);
            projects.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl = getNextPageUrl(response.getHeaders());
            i++;
        }
        return projects;
    }

    public Project findProjectByName(String owner, String repo){
        String uri ="https://api.github.com/repos/"+owner+"/"+repo;

        String token= ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Project> request = new HttpEntity<>(null, headers);

        ResponseEntity<Project> response = restTemplate.exchange(uri, HttpMethod.GET, request, Project.class);

        Project project= response.getBody();
        return  project;
    }

    public Project findProjectById(String id){
        String uri= "https://api.github.com/repositories/"+id;
        String token= ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Project> request = new HttpEntity<>(null, headers);

        ResponseEntity<Project> response = restTemplate.exchange(uri, HttpMethod.GET, request, Project.class);

        Project project= response.getBody();
        return  project;
    }
    private static String getNextPageUrl(HttpHeaders headers) {
        String result = null;

        // If there is no link header, return null
        List<String> linkHeader = headers.get("Link");
        if (linkHeader == null)
            return null;

        // If the header contains no links, return null
        String links = linkHeader.get(0);
        if (links == null || links.isEmpty())
            return null;

        // Return the next page URL or null if none.
        for (String token : links.split(", ")) {
            if (token.endsWith("rel=\"next\"")) {
                // Found the next page. This should look something like
                // <https://api.github.com/repos?page=3&per_page=100>; rel="next"
                int idx = token.indexOf('>');
                result = token.substring(1, idx);
                break;
            }
        }

        return result;
    }


}