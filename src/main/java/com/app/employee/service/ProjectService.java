package com.app.employee.service;

import com.app.employee.controller.request.ProjectRequest;
import com.app.employee.model.AccountName;
import com.app.employee.model.Employee;
import com.app.employee.model.Project;
import com.app.employee.repository.EmployeeProjectAllocationRepository;
import com.app.employee.repository.EmployeeRepository;
import com.app.employee.repository.ProjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeProjectAllocationRepository allocationRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          EmployeeProjectAllocationRepository allocationRepository,
                          EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.allocationRepository = allocationRepository;
        this.employeeRepository = employeeRepository;
    }

    public Mono<Project> addProject(ProjectRequest projectRequest) {
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);
        project.setAccountName(AccountName.valueOf(projectRequest.getAccountName()));
        return projectRepository.save(project);
    }

    public Mono<Project> getProject(String id) {
        return projectRepository.findById(id);
    }

    public Mono<Project> modifyProjectAllocation(String projectId, float newAllocation) {
        return projectRepository.findById(projectId)
                .flatMap(project -> {
                    project.setAllocation(newAllocation);
                    return projectRepository.save(project);
                });
    }

    public Mono<Employee> getSecondMostExperiencedEmployee(String projectId) {
        return allocationRepository.findByProjectId(projectId)
                .flatMap(allocation -> employeeRepository.findById(allocation.getEmployeeId()))
                .sort((e1, e2) -> e2.getOverallExperience() - e1.getOverallExperience())
                .skip(1)
                .next();
    }
}