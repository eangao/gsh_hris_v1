package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Leave.
 */
@Entity
@Table(name = "jhi_leave")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_apply", nullable = false)
    private LocalDate dateApply;

    @NotNull
    @Column(name = "date_start", nullable = false)
    private LocalDate dateStart;

    @NotNull
    @Column(name = "date_end", nullable = false)
    private LocalDate dateEnd;

    @NotNull
    @Column(name = "date_return", nullable = false)
    private LocalDate dateReturn;

    @Column(name = "checkup_date")
    private LocalDate checkupDate;

    @Column(name = "convalescing_period")
    private Integer convalescingPeriod;

    @Lob
    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "physician")
    private String physician;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "leaves" }, allowSetters = true)
    private LeaveType leaveType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Leave id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateApply() {
        return this.dateApply;
    }

    public Leave dateApply(LocalDate dateApply) {
        this.dateApply = dateApply;
        return this;
    }

    public void setDateApply(LocalDate dateApply) {
        this.dateApply = dateApply;
    }

    public LocalDate getDateStart() {
        return this.dateStart;
    }

    public Leave dateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return this.dateEnd;
    }

    public Leave dateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalDate getDateReturn() {
        return this.dateReturn;
    }

    public Leave dateReturn(LocalDate dateReturn) {
        this.dateReturn = dateReturn;
        return this;
    }

    public void setDateReturn(LocalDate dateReturn) {
        this.dateReturn = dateReturn;
    }

    public LocalDate getCheckupDate() {
        return this.checkupDate;
    }

    public Leave checkupDate(LocalDate checkupDate) {
        this.checkupDate = checkupDate;
        return this;
    }

    public void setCheckupDate(LocalDate checkupDate) {
        this.checkupDate = checkupDate;
    }

    public Integer getConvalescingPeriod() {
        return this.convalescingPeriod;
    }

    public Leave convalescingPeriod(Integer convalescingPeriod) {
        this.convalescingPeriod = convalescingPeriod;
        return this;
    }

    public void setConvalescingPeriod(Integer convalescingPeriod) {
        this.convalescingPeriod = convalescingPeriod;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public Leave diagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPhysician() {
        return this.physician;
    }

    public Leave physician(String physician) {
        this.physician = physician;
        return this;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public Leave employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public Leave leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Leave)) {
            return false;
        }
        return id != null && id.equals(((Leave) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Leave{" +
            "id=" + getId() +
            ", dateApply='" + getDateApply() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", dateReturn='" + getDateReturn() + "'" +
            ", checkupDate='" + getCheckupDate() + "'" +
            ", convalescingPeriod=" + getConvalescingPeriod() +
            ", diagnosis='" + getDiagnosis() + "'" +
            ", physician='" + getPhysician() + "'" +
            "}";
    }
}
