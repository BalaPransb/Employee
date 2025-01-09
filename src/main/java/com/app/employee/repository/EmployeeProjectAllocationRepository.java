package com.app.employee.repository;

import com.app.employee.model.EmployeeProjectAllocation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmployeeProjectAllocationRepository extends ReactiveMongoRepository<EmployeeProjectAllocation, String> {

    Flux<EmployeeProjectAllocation> findByEmployeeId(String employeeId);

    Flux<EmployeeProjectAllocation> findByProjectId(String projectId);
}
