package com.example.UserPolicyManagerWithGUI.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(value = { "policyObjects" }, allowGetters = true)
public class User {
    private String name;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private List<String> organizationUnit;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registeredOn;

    private List<String> policies;
    //@JsonIgnore
    private List<Policy> policyObjects;

    public User () {
        // required by Jackson for deserialization
    }

    public User(String name, String firstName, String lastName, String emailAddress, List<String> organizationUnit, LocalDate birthDate, LocalDate registeredOn, List<String> policies, List<Policy> policyObjects) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.organizationUnit = organizationUnit;
        this.birthDate = birthDate;
        this.registeredOn = registeredOn;
        this.policies = policies;
        this.policyObjects = policyObjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<String> getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(List<String> organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public List<String> getPolicies() {
        return policies;
    }

    public void setPolicies(List<String> policies) {
        this.policies = policies;
    }

    public List<Policy> getPolicyObjects() {
        return policyObjects;
    }

    public void setPolicyObjects(List<Policy> policyObjects) {
        this.policyObjects = policyObjects;
    }
}
