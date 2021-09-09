package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaveType.
 */
@Entity
@Table(name = "leave_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaveType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "leaveType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee", "leaveType" }, allowSetters = true)
    private Set<Leave> leaves = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveType id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LeaveType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public LeaveType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Leave> getLeaves() {
        return this.leaves;
    }

    public LeaveType leaves(Set<Leave> leaves) {
        this.setLeaves(leaves);
        return this;
    }

    public LeaveType addLeave(Leave leave) {
        this.leaves.add(leave);
        leave.setLeaveType(this);
        return this;
    }

    public LeaveType removeLeave(Leave leave) {
        this.leaves.remove(leave);
        leave.setLeaveType(null);
        return this;
    }

    public void setLeaves(Set<Leave> leaves) {
        if (this.leaves != null) {
            this.leaves.forEach(i -> i.setLeaveType(null));
        }
        if (leaves != null) {
            leaves.forEach(i -> i.setLeaveType(this));
        }
        this.leaves = leaves;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveType)) {
            return false;
        }
        return id != null && id.equals(((LeaveType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
