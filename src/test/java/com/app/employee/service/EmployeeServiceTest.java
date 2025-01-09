package com.app.employee.service;

import com.app.employee.controller.request.EmployeeRequest;
import com.app.employee.model.*;
import com.app.employee.repository.EmployeeProjectAllocationRepository;
import com.app.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private EmployeeProjectAllocationRepository allocationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEmployee_createsNewEmployee() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("John Doe");
        Employee employee = new Employee();
        employee.setId("1");
        when(employeeRepository.save(any(Employee.class))).thenReturn(Mono.just(employee));

        Mono<Employee> result = employeeService.addEmployee(employeeRequest);

        StepVerifier.create(result)
                .expectNextMatches(savedEmployee -> savedEmployee.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void addEmployee_throwsConflictIfEmployeeExists() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setId("1");
        when(employeeRepository.findById("1")).thenReturn(Mono.just(new Employee()));

        Mono<Employee> result = employeeService.addEmployee(employeeRequest);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.CONFLICT)
                .verify();
    }

    @Test
    void getEmployee_returnsEmployee() {
        Employee employee = new Employee();
        employee.setId("1");
        when(employeeRepository.findById("1")).thenReturn(Mono.just(employee));

        Mono<Employee> result = employeeService.getEmployee("1");

        StepVerifier.create(result)
                .expectNextMatches(foundEmployee -> foundEmployee.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void getEmployee_returnsEmptyIfNotFound() {
        when(employeeRepository.findById("1")).thenReturn(Mono.empty());

        Mono<Employee> result = employeeService.getEmployee("1");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void allocateProject_allocatesProjectToEmployee() {
        Employee employee = new Employee();
        employee.setId("1");
        Project project = new Project();
        project.setId("1");
        EmployeeProjectAllocation allocation = new EmployeeProjectAllocation("1", "1");

        when(employeeRepository.findById("1")).thenReturn(Mono.just(employee));
        when(projectService.getProject("1")).thenReturn(Mono.just(project));
        when(allocationRepository.findByEmployeeId("1")).thenReturn(Flux.empty());
        when(allocationRepository.save(any(EmployeeProjectAllocation.class))).thenReturn(Mono.just(allocation));
        when(emailService.sendEmail(any(), any(), any())).thenReturn(Mono.empty());

        Mono<Employee> result = employeeService.allocateProject(allocation);

        StepVerifier.create(result)
                .expectNextMatches(allocatedEmployee -> allocatedEmployee.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void allocateProject_throwsNotFoundIfEmployeeNotFound() {
        EmployeeProjectAllocation allocation = new EmployeeProjectAllocation();
        allocation.setEmployeeId("1");

        when(employeeRepository.findById("1")).thenReturn(Mono.empty());

        Mono<Employee> result = employeeService.allocateProject(allocation);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.NOT_FOUND)
                .verify();
    }

    @Test
    void allocateProject_throwsNotFoundIfProjectNotFound() {
        Employee employee = new Employee();
        employee.setId("1");
        EmployeeProjectAllocation allocation = new EmployeeProjectAllocation();
        allocation.setEmployeeId("1");
        allocation.setProjectId("1");

        when(employeeRepository.findById("1")).thenReturn(Mono.just(employee));
        when(projectService.getProject("1")).thenReturn(Mono.empty());

        Mono<Employee> result = employeeService.allocateProject(allocation);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.NOT_FOUND)
                .verify();
    }

    @Test
    void allocateProject_throwsBadRequestIfEmployeeOverloaded() {
        Employee employee = new Employee();
        employee.setId("1");
        Project project = new Project();
        project.setId("1");
        EmployeeProjectAllocation allocation = new EmployeeProjectAllocation();
        allocation.setEmployeeId("1");
        allocation.setProjectId("1");

        when(employeeRepository.findById("1")).thenReturn(Mono.just(employee));
        when(projectService.getProject("1")).thenReturn(Mono.just(project));
        when(allocationRepository.findByEmployeeId("1")).thenReturn(Flux.just(new EmployeeProjectAllocation(), new EmployeeProjectAllocation(), new EmployeeProjectAllocation()));

        Mono<Employee> result = employeeService.allocateProject(allocation);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.BAD_REQUEST)
                .verify();
    }
}