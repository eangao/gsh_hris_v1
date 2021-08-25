package com.gsh.hris.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gsh.hris.domain.TrainingHistory} entity.
 */
public class TrainingHistoryDTO implements Serializable {

    private Long id;

    private String trainingName;

    private LocalDate trainingDate;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
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
        if (!(o instanceof TrainingHistoryDTO)) {
            return false;
        }

        TrainingHistoryDTO trainingHistoryDTO = (TrainingHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainingHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingHistoryDTO{" +
            "id=" + getId() +
            ", trainingName='" + getTrainingName() + "'" +
            ", trainingDate='" + getTrainingDate() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
