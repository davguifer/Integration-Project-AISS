package aiss.GitHubMiner.service;



import aiss.GitHubMiner.model.Commit;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommitService {

    RestTemplate restTemplate =  new RestTemplate();
    public List<Commit> findAllCommits(String owner, String repo, Integer maxPages,Long sinceCommits) {
        String uri = "https://api.github.com/repos/"+ owner + "/" + repo + "/commits";
        String token = ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Commit[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Commit[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, Commit[].class);

        List<Commit> commits = new ArrayList<>(Arrays.asList(response.getBody()));

        String nextUrl = getNextPageUrl(response.getHeaders());
        int i = 1;
        while (nextUrl != null && i < maxPages) {
            response = restTemplate.exchange(nextUrl, HttpMethod.GET, request, Commit[].class);
            commits.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl = getNextPageUrl(response.getHeaders());
            System.out.println(response.getBody());
            i++;
        }
        commits = commits.stream()
                .filter(c -> LocalDateTime.parse(c.getCommit().getCommitter().getDate().substring(0,19))
                        .isAfter(LocalDateTime.now().minusDays(sinceCommits))).toList();
        return commits;
    }
    public Commit findCommitById(String id, String owner, String repo) {

        String token = ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB
        String uri = "https://api.github.com/repos/" + owner + "/" + repo + "/commits/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Commit> request = new HttpEntity<>(null, headers);

        ResponseEntity<Commit> response = restTemplate.exchange(uri, HttpMethod.GET, request, Commit.class);

        Commit commit = response.getBody();
        return commit;
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