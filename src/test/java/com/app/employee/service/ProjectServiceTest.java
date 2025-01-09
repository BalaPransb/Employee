package com.app.employee.service;

import com.app.employee.controller.request.ProjectRequest;
import com.app.employee.model.*;
import com.app.employee.repository.EmployeeProjectAllocationRepository;
import com.app.employee.repository.EmployeeRepository;
import com.app.employee.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeProjectAllocationRepository allocationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProject_createsNewProject() {
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setAccountName("ANCESTRY");
        Project project = new Project();
        project.setId("1");
        when(projectRepository.save(any(Project.class))).thenReturn(Mono.just(project));

        Mono<Project> result = projectService.addProject(projectRequest);

        StepVerifier.create(result)
                .expectNextMatches(savedProject -> savedProject.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void getProject_returnsProject() {
        Project project = new Project();
        project.setId("1");
        when(projectRepository.findById("1")).thenReturn(Mono.just(project));

        Mono<Project> result = projectService.getProject("1");

        StepVerifier.create(result)
                .expectNextMatches(foundProject -> foundProject.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void getProject_returnsEmptyIfNotFound() {
        when(projectRepository.findById("1")).thenReturn(Mono.empty());

        Mono<Project> result = projectService.getProject("1");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void modifyProjectAllocation_updatesAllocation() {
        Project project = new Project();
        project.setId("1");
        project.setAllocation(50.0f);
        when(projectRepository.findById("1")).thenReturn(Mono.just(project));
        when(projectRepository.save(any(Project.class))).thenReturn(Mono.just(project));

        Mono<Project> result = projectService.modifyProjectAllocation("1", 75.0f);

        StepVerifier.create(result)
                .expectNextMatches(updatedProject -> updatedProject.getAllocation() == 75.0f)
                .verifyComplete();
    }

    @Test
    void getSecondMostExperiencedEmployee_returnsSecondMostExperiencedEmployee() {
        Employee firstEmployee = new Employee();
        firstEmployee.setId("1");
        firstEmployee.setOverallExperience(10);
        Employee secondEmployee = new Employee();
        secondEmployee.setId("2");
        secondEmployee.setOverallExperience(8);
        Employee thirdEmployee = new Employee();
        thirdEmployee.setId("3");
        thirdEmployee.setOverallExperience(5);

        when(allocationRepository.findByProjectId("1")).thenReturn(Flux.just(
                new EmployeeProjectAllocation("1", "1"),
                new EmployeeProjectAllocation("2", "1"),
                new EmployeeProjectAllocation("3", "1")
        ));
        when(employeeRepository.findById("1")).thenReturn(Mono.just(firstEmployee));
        when(employeeRepository.findById("2")).thenReturn(Mono.just(secondEmployee));
        when(employeeRepository.findById("3")).thenReturn(Mono.just(thirdEmployee));

        Mono<Employee> result = projectService.getSecondMostExperiencedEmployee("1");

        StepVerifier.create(result)
                .expectNextMatches(employee -> employee.getId().equals("2"))
                .verifyComplete();
    }
}