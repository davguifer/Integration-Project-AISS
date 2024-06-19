package aiss.GitHubMiner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import aiss.GitHubMiner.model.Issue;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {

    RestTemplate restTemplate = new RestTemplate();

    public Issue findIssueByNumber(String owner, String repo,String number) {
        String uri = "https://api.github.com/repos/" + owner + "/" + repo + "/issues/"+number;
        String token = ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + token);
        HttpEntity<Issue> request = new HttpEntity<>(null, headers);

        ResponseEntity<Issue> response = restTemplate.exchange(uri, HttpMethod.GET, request, Issue.class);

        Issue issue = response.getBody();
        return issue;
    }

    public List<Issue> findAllIssuesRepo(String owner, String repo, Integer maxPages, Long sinceIssues){

        String uri = "https://api.github.com/repos/" + owner + "/" + repo + "/issues";

        String token = ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + token);
        HttpEntity<Issue[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Issue[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, Issue[].class);

        List<Issue> issues = new ArrayList<>(Arrays.asList(response.getBody()));

        String nextUrl = getNextPageUrl(response.getHeaders());
        int i = 1;
        while (nextUrl != null && i < maxPages) {
            response = restTemplate.exchange(nextUrl, HttpMethod.GET, request, Issue[].class);
            issues.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl = getNextPageUrl(response.getHeaders());
            i++;
        }
        issues = issues.stream()
                .filter(is -> LocalDateTime.parse(is.getCreatedAt().substring(0,19))
                        .isAfter(LocalDateTime.now().minusDays(sinceIssues))).toList();
        return issues;
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
