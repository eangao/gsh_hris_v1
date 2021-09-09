package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrainingHistory.
 */
@Entity
@Table(name = "training_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TrainingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_name")
    private String trainingName;

    @Column(name = "training_date")
    private LocalDate trainingDate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "user",
            "positions",
            "dutySchedules",
            "dailyTimeRecords",
            "benefits",
            "dependents",
            "educations",
            "trainingHistories",
            "leaves",
            "department",
            "employmentType",
        },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingHistory id(Long id) {
        this.id = id;
        return this;
    }

    public String getTrainingName() {
        return this.trainingName;
    }

    public TrainingHistory trainingName(String trainingName) {
        this.trainingName = trainingName;
        return this;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getTrainingDate() {
        return this.trainingDate;
    }

    public TrainingHistory trainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
        return this;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public TrainingHistory employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingHistory)) {
            return false;
        }
        return id != null && id.equals(((TrainingHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingHistory{" +
            "id=" + getId() +
            ", trainingName='" + getTrainingName() + "'" +
            ", trainingDate='" + getTrainingDate() + "'" +
            "}";
    }
}
