package com.gsh.hris.service.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gsh.hris.domain.Employee} entity.
 */
public class EmployeeDTO implements Serializable {

    private Long id;

    private Integer employeeId;

    @Size(min = 6, max = 30)
    private String username;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String middleName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 15)
    private String nameSuffix;

    private LocalDate birthdate;

    private Boolean sex;

    private String mobileNumber;

    @Size(max = 50)
    private String email;

    private Boolean isNotLocked;

    private LocalDate dateHired;

    private LocalDate dateDeno;

    private Integer sickLeaveYearlyCredit;

    private Integer sickLeaveYearlyCreditUsed;

    private Integer leaveYearlyCredit;

    private Integer leaveYearlyCreditUsed;

    private UserDTO user;

    private DepartmentDTO department;

    private EmploymentTypeDTO employmentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsNotLocked() {
        return isNotLocked;
    }

    public void setIsNotLocked(Boolean isNotLocked) {
        this.isNotLocked = isNotLocked;
    }

    public LocalDate getDateHired() {
        return dateHired;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDate getDateDeno() {
        return dateDeno;
    }

    public void setDateDeno(LocalDate dateDeno) {
        this.dateDeno = dateDeno;
    }

    public Integer getSickLeaveYearlyCredit() {
        return sickLeaveYearlyCredit;
    }

    public void setSickLeaveYearlyCredit(Integer sickLeaveYearlyCredit) {
        this.sickLeaveYearlyCredit = sickLeaveYearlyCredit;
    }

    public Integer getSickLeaveYearlyCreditUsed() {
        return sickLeaveYearlyCreditUsed;
    }

    public void setSickLeaveYearlyCreditUsed(Integer sickLeaveYearlyCreditUsed) {
        this.sickLeaveYearlyCreditUsed = sickLeaveYearlyCreditUsed;
    }

    public Integer getLeaveYearlyCredit() {
        return leaveYearlyCredit;
    }

    public void setLeaveYearlyCredit(Integer leaveYearlyCredit) {
        this.leaveYearlyCredit = leaveYearlyCredit;
    }

    public Integer getLeaveYearlyCreditUsed() {
        return leaveYearlyCreditUsed;
    }

    public void setLeaveYearlyCreditUsed(Integer leaveYearlyCreditUsed) {
        this.leaveYearlyCreditUsed = leaveYearlyCreditUsed;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public EmploymentTypeDTO getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentTypeDTO employmentType) {
        this.employmentType = employmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", employeeId=" + getEmployeeId() +
            ", username='" + getUsername() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", nameSuffix='" + getNameSuffix() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", sex='" + getSex() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", isNotLocked='" + getIsNotLocked() + "'" +
            ", dateHired='" + getDateHired() + "'" +
            ", dateDeno='" + getDateDeno() + "'" +
            ", sickLeaveYearlyCredit=" + getSickLeaveYearlyCredit() +
            ", sickLeaveYearlyCreditUsed=" + getSickLeaveYearlyCreditUsed() +
            ", leaveYearlyCredit=" + getLeaveYearlyCredit() +
            ", leaveYearlyCreditUsed=" + getLeaveYearlyCreditUsed() +
            ", user=" + getUser() +
            ", department=" + getDepartment() +
            ", employmentType=" + getEmploymentType() +
            "}";
    }
}
