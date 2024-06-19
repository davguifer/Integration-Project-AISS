package aiss.GitHubMiner.service;

import aiss.GitHubMiner.model.Comment;
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
public class CommentService {

    RestTemplate restTemplate = new RestTemplate();

    public List<Comment> findAllCommentsOfRepo(String owner, String repo, Integer maxPages){
        String uri = "https://api.github.com/repos/"+owner+"/"+repo+"/issues/comments";

        String token =""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " +token);
        HttpEntity<Comment[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Comment[]> response = restTemplate.exchange(uri, HttpMethod.GET,request, Comment[].class);

        List<Comment> comments= new ArrayList<>(Arrays.asList(response.getBody()));
        String nextUrl = getNextPageUrl(response.getHeaders());
        int i=1;
        while (nextUrl != null && i< maxPages){
            response= restTemplate.exchange(nextUrl,HttpMethod.GET,request,Comment[].class);
            comments.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl= getNextPageUrl(response.getHeaders());
            i++;
        }
        return comments;
    }
    public List<Comment> findAllCommentsOfIssue(String owner, String repo, String issueId, Integer maxPages){
        String uri = "https://api.github.com/repos/"+owner+"/"+repo+"/issues/"+ issueId+"/comments";

        String token =""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " +token);
        HttpEntity<Comment[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Comment[]> response = restTemplate.exchange(uri, HttpMethod.GET,request, Comment[].class);

        List<Comment> comments= new ArrayList<>(Arrays.asList(response.getBody()));
        String nextUrl = getNextPageUrl(response.getHeaders());
        int i=1;
        while (nextUrl != null && i< maxPages){
            response= restTemplate.exchange(nextUrl,HttpMethod.GET,request,Comment[].class);
            comments.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl= getNextPageUrl(response.getHeaders());
            i++;
        }
        return comments;

    }

    public List<Comment> findAllCommentsOfIssueWithLink(String link, Integer maxPages) {
        String uri =link ;

        String token = ""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Comment[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Comment[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, Comment[].class);

        List<Comment> comments = new ArrayList<>(Arrays.asList(response.getBody()));
        String nextUrl = getNextPageUrl(response.getHeaders());
        int i = 1;
        while (nextUrl != null && i < maxPages) {
            response = restTemplate.exchange(nextUrl, HttpMethod.GET, request, Comment[].class);
            comments.addAll(new ArrayList<>(Arrays.asList(response.getBody())));
            nextUrl = getNextPageUrl(response.getHeaders());
            i++;
        }
        return comments;
    }
    public Comment findCommentOfIssueById(String owner, String repo,String commentId) {
        String uri = "https://api.github.com/repos/"+owner+"/"+repo+"/issues/comments/" + commentId;
        String token =""; // AQUÍ HAY QUE RELLENAR CON SU TOKEN DE AUTENTICACIÓN DE GITHUB

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " +token);
        HttpEntity<Comment> request = new HttpEntity<>(null, headers);

        ResponseEntity<Comment> response = restTemplate.exchange(uri, HttpMethod.GET,request, Comment.class);
        Comment comment = response.getBody();
        return comment;
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