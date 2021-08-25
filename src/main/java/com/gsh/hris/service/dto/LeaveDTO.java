package com.gsh.hris.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gsh.hris.domain.Leave} entity.
 */
public class LeaveDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateApply;

    @NotNull
    private LocalDate dateStart;

    @NotNull
    private LocalDate dateEnd;

    @NotNull
    private LocalDate dateReturn;

    private LocalDate checkupDate;

    private Integer convalescingPeriod;

    @Lob
    private String diagnosis;

    private String physician;

    private EmployeeDTO employee;

    private LeaveTypeDTO leaveType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateApply() {
        return dateApply;
    }

    public void setDateApply(LocalDate dateApply) {
        this.dateApply = dateApply;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalDate getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(LocalDate dateReturn) {
        this.dateReturn = dateReturn;
    }

    public LocalDate getCheckupDate() {
        return checkupDate;
    }

    public void setCheckupDate(LocalDate checkupDate) {
        this.checkupDate = checkupDate;
    }

    public Integer getConvalescingPeriod() {
        return convalescingPeriod;
    }

    public void setConvalescingPeriod(Integer convalescingPeriod) {
        this.convalescingPeriod = convalescingPeriod;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPhysician() {
        return physician;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public LeaveTypeDTO getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveTypeDTO leaveType) {
        this.leaveType = leaveType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveDTO)) {
            return false;
        }

        LeaveDTO leaveDTO = (LeaveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveDTO{" +
            "id=" + getId() +
            ", dateApply='" + getDateApply() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", dateReturn='" + getDateReturn() + "'" +
            ", checkupDate='" + getCheckupDate() + "'" +
            ", convalescingPeriod=" + getConvalescingPeriod() +
            ", diagnosis='" + getDiagnosis() + "'" +
            ", physician='" + getPhysician() + "'" +
            ", employee=" + getEmployee() +
            ", leaveType=" + getLeaveType() +
            "}";
    }
}
