package com.gsh.hris.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gsh.hris.domain.EmploymentType} entity.
 */
public class EmploymentTypeDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmploymentTypeDTO)) {
            return false;
        }

        EmploymentTypeDTO employmentTypeDTO = (EmploymentTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employmentTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
