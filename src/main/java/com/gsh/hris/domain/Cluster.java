package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cluster.
 */
@Entity
@Table(name = "cluster")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cluster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @OneToMany(mappedBy = "cluster")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employees", "cluster" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cluster id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Cluster name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public Cluster departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Cluster addDepartment(Department department) {
        this.departments.add(department);
        department.setCluster(this);
        return this;
    }

    public Cluster removeDepartment(Department department) {
        this.departments.remove(department);
        department.setCluster(null);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        if (this.departments != null) {
            this.departments.forEach(i -> i.setCluster(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setCluster(this));
        }
        this.departments = departments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cluster)) {
            return false;
        }
        return id != null && id.equals(((Cluster) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cluster{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
