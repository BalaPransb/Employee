package com.app.employee.controller.assembler.resource;

import org.springframework.hateoas.RepresentationModel;

public class ProjectResource extends RepresentationModel<ProjectResource> {

    private String id;
    private String name;
    private String accountName; // Enum
    private float allocation; // 0.1 to 1.0
    private String projectStartDate;
    private String projectEndDate;
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAllocation() {
        return allocation;
    }

    public void setAllocation(float allocation) {
        this.allocation = allocation;
    }

    public String getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(String projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public String getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(String projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}

