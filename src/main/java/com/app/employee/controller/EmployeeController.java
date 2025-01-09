package com.app.employee.controller;

import com.app.employee.controller.assembler.EmployeeResourceAssembler;
import com.app.employee.controller.assembler.resource.EmployeeResource;
import com.app.employee.controller.request.EmployeeRequest;
import com.app.employee.model.Employee;
import com.app.employee.model.EmployeeProjectAllocation;
import com.app.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeResourceAssembler employeeResourceAssembler;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              EmployeeResourceAssembler employeeResourceAssembler) {
        this.employeeService = employeeService;
        this.employeeResourceAssembler = employeeResourceAssembler;
    }

    @PostMapping
    public Mono<ResponseEntity<EmployeeResource>> createEmployee(@RequestBody EmployeeRequest employee) {
        return employeeService.addEmployee(employee)
                .map(savedEmployee -> ResponseEntity.status(HttpStatus.CREATED).body(employeeResourceAssembler.toModel(savedEmployee)));
    }

    @GetMapping("/skills")
    public Flux<CollectionModel<EmployeeResource>> getEmployeesBySkills(@RequestParam String primarySkill, @RequestParam String secondarySkill) {
        return employeeService.findEmployeesBySkills(primarySkill, secondarySkill).map(employees -> employeeResourceAssembler.toCollectionModel(Collections.singletonList(employees)));
    }

    @GetMapping("/skills/unallocated")
    public Flux<CollectionModel<EmployeeResource>> getUnAllocatedEmployeesBySkills(@RequestParam String primarySkill) {
        return employeeService.findUnallocatedEmployeesBySkill(primarySkill).map(employees -> employeeResourceAssembler.toCollectionModel(Collections.singletonList(employees)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeResource>> getEmployeeById(@PathVariable("id") String id) {
        return employeeService.getEmployee(id)
                .map(employee -> ResponseEntity.ok(employeeResourceAssembler.toModel(employee)));
    }

    @PostMapping("/allocate")
    public Mono<ResponseEntity<EmployeeResource>> allocateEmployeeToProject(@RequestBody EmployeeProjectAllocation allocation) {
        return employeeService.allocateProject(allocation)
                .map(employee -> ResponseEntity.status(HttpStatus.CREATED).body(employeeResourceAssembler.toModel(employee)));
    }
}
