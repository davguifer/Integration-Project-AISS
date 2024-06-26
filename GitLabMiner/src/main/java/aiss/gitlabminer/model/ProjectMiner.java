package aiss.gitlabminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ProjectMiner {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    //@NotEmpty(message = "The name of the project cannot be empty")
    public String name;

    @JsonProperty("web_url")
    //@NotEmpty(message = "The URL of the project cannot be empty")
    public String webUrl;
    @JsonProperty("commits")
    private List<CommitMiner> commits;

    @JsonProperty("issues")
   // @OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "projectId")
    private List<IssueMiner> issues;

    public ProjectMiner() {
        commits = new ArrayList<>();
        issues = new ArrayList<>();
    }

    public ProjectMiner(String id, String name, String web_url) {
        this.id = id;
        this.name = name;
        this.webUrl = web_url;
        this.commits = new ArrayList<>();
        this.issues = new ArrayList<>();
    }

    public ProjectMiner(String id, String name, String web_url, List<CommitMiner> commits, List<IssueMiner> issues) {
        this.id = id;
        this.name = name;
        this.webUrl = web_url;
        this.commits = new ArrayList<>(commits);
        this.issues = new ArrayList<>(issues);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<CommitMiner> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitMiner> commits) {
        this.commits = commits;
    }

    public List<IssueMiner> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueMiner> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ProjectMiner.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("commits");
        sb.append('=');
        sb.append(((this.commits == null)?"<null>":this.commits));
        sb.append(',');
        sb.append("issues");
        sb.append('=');
        sb.append(((this.issues == null)?"<null>":this.issues));
        sb.append(',');

        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
