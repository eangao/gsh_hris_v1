package com.gsh.hris.service.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gsh.hris.domain.Benefits} entity.
 */
public class BenefitsDTO implements Serializable {

    private Long id;

    @Size(max = 60)
    private String name;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof BenefitsDTO)) {
            return false;
        }

        BenefitsDTO benefitsDTO = (BenefitsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, benefitsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BenefitsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
