package com.app.employee.repository;

import com.app.employee.model.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

    Flux<Employee> findByPrimarySkillAndSecondarySkill(String primarySkill, String secondarySkill);

    Flux<Employee> findByPrimarySkill(String primarySkill);

//    Flux<Employee> findByProjectIdOrderByExperienceDesc(String projectId);
}

