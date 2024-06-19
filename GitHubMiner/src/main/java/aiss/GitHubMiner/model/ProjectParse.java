package aiss.GitHubMiner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.sound.midi.MidiMessage;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class ProjectParse {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    @NotEmpty(message = "The name of the project can not be empty")
    public String name;

    @JsonProperty("web_url")
    @NotEmpty(message = "The URL of the project can not be empty")
    public  String webUrl;

    @JsonProperty("commits")
    private List<CommitParse> commits;

    @JsonProperty("issues")
    @OneToMany(cascade = CascadeType.ALL)
    private  List<IssueParse> issues;

    public ProjectParse(){
        commits = new ArrayList<>();
        issues = new ArrayList<>();
    }

    public ProjectParse(String id, String name, String webUrl){
        this.id= id;
        this.name=name;
        this.webUrl=webUrl;
        this.commits=new ArrayList<>();
        this.issues= new ArrayList<>();
    }

    public ProjectParse(String id, String name, String webUrl, List<CommitParse> commits, List<IssueParse> issues){
        this.id= id;
        this.name=name;
        this.webUrl=webUrl;
        this.commits=commits;
        this.issues= issues;
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

    public List<CommitParse> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitParse> commits) {
        this.commits = commits;
    }

    public List<IssueParse> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueParse> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Project.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
