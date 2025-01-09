package com.app.employee.controller;

import com.app.employee.controller.assembler.ProjectResourceAssembler;
import com.app.employee.controller.assembler.resource.ProjectResource;
import com.app.employee.controller.request.ProjectRequest;
import com.app.employee.model.Employee;
import com.app.employee.model.EmployeeProjectAllocation;
import com.app.employee.model.Project;
import com.app.employee.service.ProjectService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectResourceAssembler projectResourceAssembler;

    @Autowired
    public ProjectController(ProjectService projectService,
                             ProjectResourceAssembler projectResourceAssembler) {
        this.projectService = projectService;
        this.projectResourceAssembler = projectResourceAssembler;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProjectResource>> getProject(@PathVariable("id") String id) {
        return projectService.getProject(id)
                .map(project -> ResponseEntity.ok(projectResourceAssembler.toModel(project)));
    }

    @PostMapping
    public Mono<ResponseEntity<ProjectResource>> createProject(@RequestBody ProjectRequest project) {
        return projectService.addProject(project)
                .map(savedProject -> ResponseEntity.status(HttpStatus.CREATED).body(projectResourceAssembler.toModel(savedProject)));
    }

    @PatchMapping("/{projectId}/allocation")
    public Mono<ResponseEntity<Project>> modifyProjectAllocation(@PathVariable String projectId, @RequestParam float newAllocation) {
        return projectService.modifyProjectAllocation(projectId, newAllocation)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{projectId}/second-most-experienced")
    public Mono<ResponseEntity<Employee>> getSecondMostExperiencedEmployee(@PathVariable String projectId) {
        return projectService.getSecondMostExperiencedEmployee(projectId)
                .map(ResponseEntity::ok);
    }
}