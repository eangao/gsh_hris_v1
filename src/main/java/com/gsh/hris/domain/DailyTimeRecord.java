package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DailyTimeRecord.
 */
@Entity
@Table(name = "daily_time_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DailyTimeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time_in")
    private Instant dateTimeIn;

    @Column(name = "date_time_out")
    private Instant dateTimeOut;

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

    public DailyTimeRecord id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDateTimeIn() {
        return this.dateTimeIn;
    }

    public DailyTimeRecord dateTimeIn(Instant dateTimeIn) {
        this.dateTimeIn = dateTimeIn;
        return this;
    }

    public void setDateTimeIn(Instant dateTimeIn) {
        this.dateTimeIn = dateTimeIn;
    }

    public Instant getDateTimeOut() {
        return this.dateTimeOut;
    }

    public DailyTimeRecord dateTimeOut(Instant dateTimeOut) {
        this.dateTimeOut = dateTimeOut;
        return this;
    }

    public void setDateTimeOut(Instant dateTimeOut) {
        this.dateTimeOut = dateTimeOut;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public DailyTimeRecord employee(Employee employee) {
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
        if (!(o instanceof DailyTimeRecord)) {
            return false;
        }
        return id != null && id.equals(((DailyTimeRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyTimeRecord{" +
            "id=" + getId() +
            ", dateTimeIn='" + getDateTimeIn() + "'" +
            ", dateTimeOut='" + getDateTimeOut() + "'" +
            "}";
    }
}
