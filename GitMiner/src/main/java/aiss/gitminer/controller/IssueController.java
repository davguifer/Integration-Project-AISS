package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    // GET http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Retrieve a list of issues",
            description = "Get a list of issues",
            tags = {"issues", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of issues",
                    content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Issues not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping
    public List<Issue> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String authorId, @RequestParam(required = false) String state,
                               @RequestParam(required = false) String order) {
        Pageable paging;

        // Ordering and paging
        if(order != null) {
            if (order.startsWith("-"))
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        }
        else
            paging = PageRequest.of(page, size);

        //Filtering
        Page<Issue> pageIssue;
        if (authorId != null && state != null)
            pageIssue = issueRepository.findByAuthorIdAndState(authorId, state, paging);
        else if (authorId != null)
            pageIssue = issueRepository.findByAuthorId(authorId, paging);
        else if (state != null)
            pageIssue = issueRepository.findByState(state, paging);
        else
            pageIssue = issueRepository.findAll(paging);

        return pageIssue.getContent();
    }

    // GET http://localhost:8080/gitminer/issues/{id}
    @Operation(
            summary = "Retrieve an issue by id",
            description = "Get a issue by specifying its id",
            tags = {"issue", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue found",
                    content = { @Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Issue not found",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public Issue findOne(@Parameter(description = "id of issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        return issue.get();
    }

    // GET http://localhost:8080/gitminer/issues/{id}/comments
    @Operation(
            summary = "Retrieve comments of an issue",
            description = "Get a list of comments from an issue by specifying its id",
            tags = {"issue", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue comments found",
                    content = { @Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Issue comments not found",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}/comments")
    public List<Comment> getComments(@Parameter(description = "id of issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        Issue _issue = issue.get();
        return issue.get().getComments();
    }


}
