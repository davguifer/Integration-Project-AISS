package aiss.gitlabminer.service;

import aiss.gitlabminer.model.CommitGitLab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    public List<CommitGitLab> findProjectCommits(String id, Integer maxPages) {
        String uri = "https://gitlab.com/api/v4/projects/" + id + "/repository/commits";
        String token = ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITLAB
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + token);
        HttpEntity<CommitGitLab[]> request = new HttpEntity<>(null, headers);
        ResponseEntity<CommitGitLab[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, CommitGitLab[].class);
        List<CommitGitLab> commits = new ArrayList<>(Arrays.asList(response.getBody()));
        String nextUrl = getNextPageUrl(response.getHeaders());
        int i = 1;
        while (nextUrl != null && i < maxPages) {
            response = restTemplate.exchange(nextUrl, HttpMethod.GET, request, CommitGitLab[].class);
            commits.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl = getNextPageUrl(response.getHeaders());
            i++;
        }
        return commits;
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
