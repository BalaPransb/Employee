package com.app.employee.repository;

import com.app.employee.model.Project;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {

}
