package com.app.employee.controller.assembler;

import com.app.employee.controller.ProjectController;
import com.app.employee.controller.assembler.resource.ProjectResource;
import com.app.employee.model.Project;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectResourceAssembler extends RepresentationModelAssemblerSupport<Project, ProjectResource> {

    public ProjectResourceAssembler() {
        super(ProjectController.class, ProjectResource.class);
    }

    public ProjectResource toModel(Project project) {
        ProjectResource resource = new ProjectResource();
        BeanUtils.copyProperties(project, resource);
        resource.setAccountName(project.getAccountName().name());
        resource.setProjectStartDate(project.getProjectStartDate().toString());
        resource.setProjectEndDate(project.getProjectEndDate().toString());
        resource.add(linkTo(methodOn(ProjectController.class).getProject(project.getId())).withSelfRel());
        return resource;
    }

    public CollectionModel<ProjectResource> toCollectionModel(List<Project> projects) {
        CollectionModel<ProjectResource> projectResources = super.toCollectionModel(projects);
        projectResources.forEach(employeeResource ->
                employeeResource.add(linkTo(methodOn(ProjectController.class).getProject(employeeResource.getId())).withSelfRel())
        );
        return projectResources;
    }
}
