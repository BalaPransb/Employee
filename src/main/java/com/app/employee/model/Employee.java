package com.app.employee.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Document(collection = "employees")
public class Employee {

    @Id
    private String id;
    @NonNull
    private String name;
    private CapabilityCentre capabilityCentre;
    @NonNull
    private LocalDate dateOfJoining;
    private Designation designation;
    @NonNull
    private String primarySkill;
    private String secondarySkill;
    private Integer overallExperience;
    @NonNull
    private String email;

    public Employee() {}

    public CapabilityCentre getCapabilityCentre() {
        return capabilityCentre;
    }

    public void setCapabilityCentre(CapabilityCentre capabilityCentre) {
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

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
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
