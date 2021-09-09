package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Education.
 */
@Entity
@Table(name = "education")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bachelor_degree")
    private String bachelorDegree;

    @Column(name = "year_graduated")
    private Integer yearGraduated;

    @Column(name = "school")
    private String school;

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

    public Education id(Long id) {
        this.id = id;
        return this;
    }

    public String getBachelorDegree() {
        return this.bachelorDegree;
    }

    public Education bachelorDegree(String bachelorDegree) {
        this.bachelorDegree = bachelorDegree;
        return this;
    }

    public void setBachelorDegree(String bachelorDegree) {
        this.bachelorDegree = bachelorDegree;
    }

    public Integer getYearGraduated() {
        return this.yearGraduated;
    }

    public Education yearGraduated(Integer yearGraduated) {
        this.yearGraduated = yearGraduated;
        return this;
    }

    public void setYearGraduated(Integer yearGraduated) {
        this.yearGraduated = yearGraduated;
    }

    public String getSchool() {
        return this.school;
    }

    public Education school(String school) {
        this.school = school;
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public Education employee(Employee employee) {
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
        if (!(o instanceof Education)) {
            return false;
        }
        return id != null && id.equals(((Education) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Education{" +
            "id=" + getId() +
            ", bachelorDegree='" + getBachelorDegree() + "'" +
            ", yearGraduated=" + getYearGraduated() +
            ", school='" + getSchool() + "'" +
            "}";
    }
}
