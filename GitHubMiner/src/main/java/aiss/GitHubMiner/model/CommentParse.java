package aiss.GitHubMiner.model;

public class CommentParse {
    private String id;
    private String body;
    private String created_at;
    private String updated_at;
    private UserParse author;

    public CommentParse(String id, String body, String created_at, String updated_at, UserParse author) {
        this.id = id;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.author = author;
    }
    public CommentParse(){
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public UserParse getAuthor() {
        return author;
    }

    public void setAuthor(UserParse author) {
        this.author = author;
    }
}
