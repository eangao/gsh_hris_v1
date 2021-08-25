package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @OneToMany(mappedBy = "department")
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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "departments" }, allowSetters = true)
    private Cluster cluster;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Department name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public Department employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Department addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setDepartment(this);
        return this;
    }

    public Department removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setDepartment(null);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setDepartment(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setDepartment(this));
        }
        this.employees = employees;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public Department cluster(Cluster cluster) {
        this.setCluster(cluster);
        return this;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
