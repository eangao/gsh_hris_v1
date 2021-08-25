package com.gsh.hris.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gsh.hris.domain.Dependents} entity.
 */
public class DependentsDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String relation;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DependentsDTO)) {
            return false;
        }

        DependentsDTO dependentsDTO = (DependentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dependentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DependentsDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", relation='" + getRelation() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
