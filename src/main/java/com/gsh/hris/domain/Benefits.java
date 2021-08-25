package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A Benefits.
 */
@Entity
@Table(name = "benefits")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Benefits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 60)
    @Column(name = "name", length = 60)
    private String name;

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

    public Benefits id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Benefits name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public Benefits employee(Employee employee) {
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
        if (!(o instanceof Benefits)) {
            return false;
        }
        return id != null && id.equals(((Benefits) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Benefits{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
