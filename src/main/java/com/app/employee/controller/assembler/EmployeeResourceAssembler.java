package com.app.employee.controller.assembler;

import com.app.employee.controller.EmployeeController;
import com.app.employee.controller.assembler.resource.EmployeeResource;
import com.app.employee.model.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeResourceAssembler extends RepresentationModelAssemblerSupport<Employee, EmployeeResource> {

    public EmployeeResourceAssembler() {
        super(EmployeeController.class, EmployeeResource.class);
    }

    public EmployeeResource toModel(Employee employee) {
        EmployeeResource resource = new EmployeeResource();
        BeanUtils.copyProperties(employee, resource);
        resource.setDesignation(employee.getDesignation().name());
        resource.setCapabilityCentre(employee.getCapabilityCentre().name());
        resource.setDateOfJoining(employee.getDateOfJoining().toString());
        resource.add(linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel());
        return resource;
    }

    public CollectionModel<EmployeeResource> toCollectionModel(List<Employee> employees) {
        CollectionModel<EmployeeResource> employeeResources = super.toCollectionModel(employees);
        employeeResources.forEach(employeeResource ->
                employeeResource.add(linkTo(methodOn(EmployeeController.class).getEmployeeById(employeeResource.getId())).withSelfRel())
        );
        return employeeResources;
    }
}
