package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.CommitRepository;
import aiss.gitminer.repository.ProjectRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository commitRepository;

    // GET http://localhost:8080/gitminer/commits
    @Operation(
            summary = "Retrieve a list of commits",
            description = "Get a list of commits",
            tags = {"commits", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of commits",
                    content = {@Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Commits not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping
    public List<Commit> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String author_email, @RequestParam(required = false) String order) {
        Pageable paging; // Constains the information of pagination and ordering

        // Ordering and paging
        if(order != null) {
            if (order.startsWith("-"))
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());

        }
        else
            paging = PageRequest.of(page, size); // Default value for the sort parameter

        //Filtering
        Page<Commit> pageCommit;
        if (author_email != null)
            pageCommit = commitRepository.findByAuthorEmail(author_email, paging);
        else
            pageCommit = commitRepository.findAll(paging);
        return pageCommit.getContent(); // Listing results of actual page
    }

    // GET http://localhost:8080/gitminer/commits/{id}
    @Operation(
            summary = "Retrieve a commit by id",
            description = "Get a commit by specifying its id",
            tags = {"commit", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit found",
                    content = { @Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Commit not found",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public Commit findOne(@Parameter(description = "id of commit to be searched") @PathVariable String id) throws CommentNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        return commit.get();
    }

}
