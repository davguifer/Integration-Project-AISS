package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name="Project", description = "Project management API")
@RestController
@RequestMapping("/gitminer")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    // GET http://localhost:8080/gitminer/projects
    @Operation(
            summary = "Retrieve a list of projects",
            description = "Get a list of projects",
            tags = {"projects", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of projects",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Projects not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/projects")
    public List<Project> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(required = false) String name, @RequestParam(required = false) String order) {
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
        Page<Project> pageProjects;
        if (name != null)
            pageProjects = projectRepository.findByName(name, paging);
        else
            pageProjects = projectRepository.findAll(paging);
        return pageProjects.getContent();
    }

    // GET http://localhost:8080/gitminer/projects/{id}
    @Operation(
            summary = "Retrieve a project by id",
            description = "Get a project by specifying its id",
            tags = {"projects", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project found",
                    content = { @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description="Project not found",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/projects/{id}")
    public Project findOne(@Parameter(description = "id of project to be searched") @PathVariable String id)
            throws ProjectNotFoundException{
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    // POST http://localhost:8080/gitminer/projects
    @Operation(
            summary = "Create new project",
            description = "Create a new project",
            tags = {"projects", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Project not created",
                    content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/projects")
    public Project create(@RequestBody @Valid Project project) {
        Project _project = projectRepository.save(new Project(project.getId(), project.getName(), project.getWebUrl(),
                project.getCommits(), project.getIssues()));
        return _project;
    }

    // PUT http://localhost:8080/api/projects/{id}
    @Operation(
            summary = "Update project",
            description = "Modify an existing project",
            tags = {"projects", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project updated",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/projects/{id}")
    public void update(@Valid @RequestBody Project updatedProject,
                       @Parameter(description = "id of project to be updated") @PathVariable String id) throws  ProjectNotFoundException {
        Optional<Project> projectData = projectRepository.findById(id);

        Project _project = projectData.get();
        _project.setName(updatedProject.getName());
        _project.setWebUrl(updatedProject.getWebUrl());
        projectRepository.save(_project);
    }

    // DELETE https://localhost:8080/api/projects/{id}
    @Operation(
            summary = "Delete project",
            description = "Delete an existing project by specifying its id",
            tags = {"projects", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project delete",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/projects/{id}")
    public void delete(@Parameter(description = "id of project to be deleted") @PathVariable String id) throws  ProjectNotFoundException{
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        }
    }
}
