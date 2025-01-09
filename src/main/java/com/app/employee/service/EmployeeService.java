package com.app.employee.service;

import com.app.employee.controller.request.EmployeeRequest;
import com.app.employee.model.*;
import com.app.employee.repository.EmployeeProjectAllocationRepository;
import com.app.employee.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final ProjectService projectService;
    private final EmployeeProjectAllocationRepository allocationRepository;
    private final EmailService emailService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           ProjectService projectService,
                           EmployeeProjectAllocationRepository allocationRepository,
                           EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.projectService = projectService;
        this.allocationRepository = allocationRepository;
        this.emailService = emailService;
    }

    public Mono<Employee> addEmployee(EmployeeRequest employee) {
        if (employee.getId() != null) {
            return employeeRepository.findById(employee.getId())
                    .flatMap(existingEmployee -> {
                        if (existingEmployee != null) {
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Employee already exists"));
                        }
                        return Mono.empty();
                    });
        } else {
            Employee newEmployee = new Employee();
            BeanUtils.copyProperties(employee, newEmployee);
            newEmployee.setCapabilityCentre(employee.getCapabilityCentre()!= null ? CapabilityCentre.valueOf(employee.getCapabilityCentre()) : null);
            newEmployee.setDesignation(employee.getDesignation() != null ? Designation.valueOf(employee.getDesignation()) : null);
            if (employee.getDateOfJoining() == null) {
                newEmployee.setDateOfJoining(LocalDate.now());
            }

            return employeeRepository.save(newEmployee).log();
        }
    }

    public Mono<Employee> getEmployee(String id) {
        return employeeRepository.findById(id);
    }

    public Flux<Employee> findEmployeesBySkills(String primarySkill, String secondarySkill) {
        return employeeRepository.findByPrimarySkillAndSecondarySkill(primarySkill, secondarySkill);
    }

    public Flux<Employee> findUnallocatedEmployeesBySkill(String primarySkill) {
        return employeeRepository.findByPrimarySkill(primarySkill).filter(e -> allocationRepository.findByEmployeeId(e.getId()).count().blockOptional().orElse(0L) == 0);
    }

    public Mono<Employee> allocateProject(EmployeeProjectAllocation allocation) {
        return employeeRepository.findById(allocation.getEmployeeId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found")))
                .flatMap(employee ->
                        projectService.getProject(allocation.getProjectId())
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found")))
                                .flatMap(project ->
                                        allocationRepository.findByEmployeeId(allocation.getEmployeeId()).count()
                                                .flatMap(count -> {
                                                    if (count >= 3) {
                                                        log.info("Employee {} is already overloaded", employee.getId());
                                                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee already allocated to 3 projects"));
                                                    } else {
                                                        if (allocation.getAllocationDate() == null) {
                                                            allocation.setAllocationDate(LocalDate.now());
                                                        }
                                                        return allocationRepository.save(allocation)
                                                                .then(sendAllocationNotification(employee, project));
                                                    }
                                                })
                                )
                );
    }

    public Mono<Employee> sendAllocationNotification(Employee employee, Project project) {
        return emailService.sendEmail(employee.getEmail(), project.getName() + " Project Allocation",
                        "You have been allocated to a new project " + project.getName())
                        .then(Mono.just(employee));
    }
}
