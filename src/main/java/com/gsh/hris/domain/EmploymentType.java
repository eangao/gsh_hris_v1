package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmploymentType.
 */
@Entity
@Table(name = "employment_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmploymentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @OneToMany(mappedBy = "employmentType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmploymentType id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public EmploymentType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public EmploymentType employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public EmploymentType addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setEmploymentType(this);
        return this;
    }

    public EmploymentType removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setEmploymentType(null);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setEmploymentType(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setEmploymentType(this));
        }
        this.employees = employees;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmploymentType)) {
            return false;
        }
        return id != null && id.equals(((EmploymentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
