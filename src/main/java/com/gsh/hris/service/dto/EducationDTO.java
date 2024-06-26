package com.gsh.hris.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gsh.hris.domain.Education} entity.
 */
public class EducationDTO implements Serializable {

    private Long id;

    private String bachelorDegree;

    private Integer yearGraduated;

    private String school;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBachelorDegree() {
        return bachelorDegree;
    }

    public void setBachelorDegree(String bachelorDegree) {
        this.bachelorDegree = bachelorDegree;
    }

    public Integer getYearGraduated() {
        return yearGraduated;
    }

    public void setYearGraduated(Integer yearGraduated) {
        this.yearGraduated = yearGraduated;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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
        if (!(o instanceof EducationDTO)) {
            return false;
        }

        EducationDTO educationDTO = (EducationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, educationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EducationDTO{" +
            "id=" + getId() +
            ", bachelorDegree='" + getBachelorDegree() + "'" +
            ", yearGraduated=" + getYearGraduated() +
            ", school='" + getSchool() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
