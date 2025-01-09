package com.app.employee.controller.request;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EmployeeRequest {

    private String id;
    @NotNull
    private String name;
    private String capabilityCentre;
    @NotNull
    private LocalDate dateOfJoining;
    private String designation;
    @NotNull
    private String primarySkill;
    private String secondarySkill;
    private Integer overallExperience;
    @NotNull
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCapabilityCentre() {
        return capabilityCentre;
    }

    public void setCapabilityCentre(String capabilityCentre) {
        this.capabilityCentre = capabilityCentre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
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
