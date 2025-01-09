package com.app.employee.controller.assembler.resource;

import org.springframework.hateoas.RepresentationModel;

public class EmployeeResource extends RepresentationModel<EmployeeResource> {

    private String id;
    private String name;
    private String capabilityCentre;
    private String dateOfJoining;
    private String designation;
    private String primarySkill;
    private String secondarySkill;
    private Integer overallExperience;
    private String email;

    public String getCapabilityCentre() {
        return capabilityCentre;
    }

    public void setCapabilityCentre(String capabilityCentre) {
        this.capabilityCentre = capabilityCentre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPrimarySkill() {
        return primarySkill;
    }

    public void setPrimarySkill(String primarySkill) {
        this.primarySkill = primarySkill;
    }

    public String getSecondarySkill() {
        return secondarySkill;
    }

    public void setSecondarySkill(String secondarySkill) {
        this.secondarySkill = secondarySkill;
    }

    public Integer getOverallExperience() {
        return overallExperience;
    }

    public void setOverallExperience(Integer overallExperience) {
        this.overallExperience = overallExperience;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
