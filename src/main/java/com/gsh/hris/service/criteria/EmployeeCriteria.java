package com.gsh.hris.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gsh.hris.domain.Employee} entity. This class is used
 * in {@link com.gsh.hris.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter employeeId;

    private StringFilter username;

    private StringFilter firstName;

    private StringFilter middleName;

    private StringFilter lastName;

    private StringFilter nameSuffix;

    private LocalDateFilter birthdate;

    private BooleanFilter sex;

    private StringFilter mobileNumber;

    private StringFilter email;

    private BooleanFilter isNotLocked;

    private LocalDateFilter dateHired;

    private LocalDateFilter dateDeno;

    private IntegerFilter sickLeaveYearlyCredit;

    private IntegerFilter sickLeaveYearlyCreditUsed;

    private IntegerFilter leaveYearlyCredit;

    private IntegerFilter leaveYearlyCreditUsed;

    private LongFilter userId;

    private LongFilter positionId;

    private LongFilter dutyScheduleId;

    private LongFilter dailyTimeRecordId;

    private LongFilter benefitsId;

    private LongFilter dependentsId;

    private LongFilter educationId;

    private LongFilter trainingHistoryId;

    private LongFilter leaveId;

    private LongFilter departmentId;

    private LongFilter employmentTypeId;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.middleName = other.middleName == null ? null : other.middleName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.nameSuffix = other.nameSuffix == null ? null : other.nameSuffix.copy();
        this.birthdate = other.birthdate == null ? null : other.birthdate.copy();
        this.sex = other.sex == null ? null : other.sex.copy();
        this.mobileNumber = other.mobileNumber == null ? null : other.mobileNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.isNotLocked = other.isNotLocked == null ? null : other.isNotLocked.copy();
        this.dateHired = other.dateHired == null ? null : other.dateHired.copy();
        this.dateDeno = other.dateDeno == null ? null : other.dateDeno.copy();
        this.sickLeaveYearlyCredit = other.sickLeaveYearlyCredit == null ? null : other.sickLeaveYearlyCredit.copy();
        this.sickLeaveYearlyCreditUsed = other.sickLeaveYearlyCreditUsed == null ? null : other.sickLeaveYearlyCreditUsed.copy();
        this.leaveYearlyCredit = other.leaveYearlyCredit == null ? null : other.leaveYearlyCredit.copy();
        this.leaveYearlyCreditUsed = other.leaveYearlyCreditUsed == null ? null : other.leaveYearlyCreditUsed.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.positionId = other.positionId == null ? null : other.positionId.copy();
        this.dutyScheduleId = other.dutyScheduleId == null ? null : other.dutyScheduleId.copy();
        this.dailyTimeRecordId = other.dailyTimeRecordId == null ? null : other.dailyTimeRecordId.copy();
        this.benefitsId = other.benefitsId == null ? null : other.benefitsId.copy();
        this.dependentsId = other.dependentsId == null ? null : other.dependentsId.copy();
        this.educationId = other.educationId == null ? null : other.educationId.copy();
        this.trainingHistoryId = other.trainingHistoryId == null ? null : other.trainingHistoryId.copy();
        this.leaveId = other.leaveId == null ? null : other.leaveId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.employmentTypeId = other.employmentTypeId == null ? null : other.employmentTypeId.copy();
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getEmployeeId() {
        return employeeId;
    }

    public IntegerFilter employeeId() {
        if (employeeId == null) {
            employeeId = new IntegerFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(IntegerFilter employeeId) {
        this.employeeId = employeeId;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public StringFilter middleName() {
        if (middleName == null) {
            middleName = new StringFilter();
        }
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getNameSuffix() {
        return nameSuffix;
    }

    public StringFilter nameSuffix() {
        if (nameSuffix == null) {
            nameSuffix = new StringFilter();
        }
        return nameSuffix;
    }

    public void setNameSuffix(StringFilter nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public LocalDateFilter getBirthdate() {
        return birthdate;
    }

    public LocalDateFilter birthdate() {
        if (birthdate == null) {
            birthdate = new LocalDateFilter();
        }
        return birthdate;
    }

    public void setBirthdate(LocalDateFilter birthdate) {
        this.birthdate = birthdate;
    }

    public BooleanFilter getSex() {
        return sex;
    }

    public BooleanFilter sex() {
        if (sex == null) {
            sex = new BooleanFilter();
        }
        return sex;
    }

    public void setSex(BooleanFilter sex) {
        this.sex = sex;
    }

    public StringFilter getMobileNumber() {
        return mobileNumber;
    }

    public StringFilter mobileNumber() {
        if (mobileNumber == null) {
            mobileNumber = new StringFilter();
        }
        return mobileNumber;
    }

    public void setMobileNumber(StringFilter mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public BooleanFilter getIsNotLocked() {
        return isNotLocked;
    }

    public BooleanFilter isNotLocked() {
        if (isNotLocked == null) {
            isNotLocked = new BooleanFilter();
        }
        return isNotLocked;
    }

    public void setIsNotLocked(BooleanFilter isNotLocked) {
        this.isNotLocked = isNotLocked;
    }

    public LocalDateFilter getDateHired() {
        return dateHired;
    }

    public LocalDateFilter dateHired() {
        if (dateHired == null) {
            dateHired = new LocalDateFilter();
        }
        return dateHired;
    }

    public void setDateHired(LocalDateFilter dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDateFilter getDateDeno() {
        return dateDeno;
    }

    public LocalDateFilter dateDeno() {
        if (dateDeno == null) {
            dateDeno = new LocalDateFilter();
        }
        return dateDeno;
    }

    public void setDateDeno(LocalDateFilter dateDeno) {
        this.dateDeno = dateDeno;
    }

    public IntegerFilter getSickLeaveYearlyCredit() {
        return sickLeaveYearlyCredit;
    }

    public IntegerFilter sickLeaveYearlyCredit() {
        if (sickLeaveYearlyCredit == null) {
            sickLeaveYearlyCredit = new IntegerFilter();
        }
        return sickLeaveYearlyCredit;
    }

    public void setSickLeaveYearlyCredit(IntegerFilter sickLeaveYearlyCredit) {
        this.sickLeaveYearlyCredit = sickLeaveYearlyCredit;
    }

    public IntegerFilter getSickLeaveYearlyCreditUsed() {
        return sickLeaveYearlyCreditUsed;
    }

    public IntegerFilter sickLeaveYearlyCreditUsed() {
        if (sickLeaveYearlyCreditUsed == null) {
            sickLeaveYearlyCreditUsed = new IntegerFilter();
        }
        return sickLeaveYearlyCreditUsed;
    }

    public void setSickLeaveYearlyCreditUsed(IntegerFilter sickLeaveYearlyCreditUsed) {
        this.sickLeaveYearlyCreditUsed = sickLeaveYearlyCreditUsed;
    }

    public IntegerFilter getLeaveYearlyCredit() {
        return leaveYearlyCredit;
    }

    public IntegerFilter leaveYearlyCredit() {
        if (leaveYearlyCredit == null) {
            leaveYearlyCredit = new IntegerFilter();
        }
        return leaveYearlyCredit;
    }

    public void setLeaveYearlyCredit(IntegerFilter leaveYearlyCredit) {
        this.leaveYearlyCredit = leaveYearlyCredit;
    }

    public IntegerFilter getLeaveYearlyCreditUsed() {
        return leaveYearlyCreditUsed;
    }

    public IntegerFilter leaveYearlyCreditUsed() {
        if (leaveYearlyCreditUsed == null) {
            leaveYearlyCreditUsed = new IntegerFilter();
        }
        return leaveYearlyCreditUsed;
    }

    public void setLeaveYearlyCreditUsed(IntegerFilter leaveYearlyCreditUsed) {
        this.leaveYearlyCreditUsed = leaveYearlyCreditUsed;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public LongFilter positionId() {
        if (positionId == null) {
            positionId = new LongFilter();
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
    }

    public LongFilter getDutyScheduleId() {
        return dutyScheduleId;
    }

    public LongFilter dutyScheduleId() {
        if (dutyScheduleId == null) {
            dutyScheduleId = new LongFilter();
        }
        return dutyScheduleId;
    }

    public void setDutyScheduleId(LongFilter dutyScheduleId) {
        this.dutyScheduleId = dutyScheduleId;
    }

    public LongFilter getDailyTimeRecordId() {
        return dailyTimeRecordId;
    }

    public LongFilter dailyTimeRecordId() {
        if (dailyTimeRecordId == null) {
            dailyTimeRecordId = new LongFilter();
        }
        return dailyTimeRecordId;
    }

    public void setDailyTimeRecordId(LongFilter dailyTimeRecordId) {
        this.dailyTimeRecordId = dailyTimeRecordId;
    }

    public LongFilter getBenefitsId() {
        return benefitsId;
    }

    public LongFilter benefitsId() {
        if (benefitsId == null) {
            benefitsId = new LongFilter();
        }
        return benefitsId;
    }

    public void setBenefitsId(LongFilter benefitsId) {
        this.benefitsId = benefitsId;
    }

    public LongFilter getDependentsId() {
        return dependentsId;
    }

    public LongFilter dependentsId() {
        if (dependentsId == null) {
            dependentsId = new LongFilter();
        }
        return dependentsId;
    }

    public void setDependentsId(LongFilter dependentsId) {
        this.dependentsId = dependentsId;
    }

    public LongFilter getEducationId() {
        return educationId;
    }

    public LongFilter educationId() {
        if (educationId == null) {
            educationId = new LongFilter();
        }
        return educationId;
    }

    public void setEducationId(LongFilter educationId) {
        this.educationId = educationId;
    }

    public LongFilter getTrainingHistoryId() {
        return trainingHistoryId;
    }

    public LongFilter trainingHistoryId() {
        if (trainingHistoryId == null) {
            trainingHistoryId = new LongFilter();
        }
        return trainingHistoryId;
    }

    public void setTrainingHistoryId(LongFilter trainingHistoryId) {
        this.trainingHistoryId = trainingHistoryId;
    }

    public LongFilter getLeaveId() {
        return leaveId;
    }

    public LongFilter leaveId() {
        if (leaveId == null) {
            leaveId = new LongFilter();
        }
        return leaveId;
    }

    public void setLeaveId(LongFilter leaveId) {
        this.leaveId = leaveId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getEmploymentTypeId() {
        return employmentTypeId;
    }

    public LongFilter employmentTypeId() {
        if (employmentTypeId == null) {
            employmentTypeId = new LongFilter();
        }
        return employmentTypeId;
    }

    public void setEmploymentTypeId(LongFilter employmentTypeId) {
        this.employmentTypeId = employmentTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(username, that.username) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(nameSuffix, that.nameSuffix) &&
            Objects.equals(birthdate, that.birthdate) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(mobileNumber, that.mobileNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(isNotLocked, that.isNotLocked) &&
            Objects.equals(dateHired, that.dateHired) &&
            Objects.equals(dateDeno, that.dateDeno) &&
            Objects.equals(sickLeaveYearlyCredit, that.sickLeaveYearlyCredit) &&
            Objects.equals(sickLeaveYearlyCreditUsed, that.sickLeaveYearlyCreditUsed) &&
            Objects.equals(leaveYearlyCredit, that.leaveYearlyCredit) &&
            Objects.equals(leaveYearlyCreditUsed, that.leaveYearlyCreditUsed) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(dutyScheduleId, that.dutyScheduleId) &&
            Objects.equals(dailyTimeRecordId, that.dailyTimeRecordId) &&
            Objects.equals(benefitsId, that.benefitsId) &&
            Objects.equals(dependentsId, that.dependentsId) &&
            Objects.equals(educationId, that.educationId) &&
            Objects.equals(trainingHistoryId, that.trainingHistoryId) &&
            Objects.equals(leaveId, that.leaveId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(employmentTypeId, that.employmentTypeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeId,
            username,
            firstName,
            middleName,
            lastName,
            nameSuffix,
            birthdate,
            sex,
            mobileNumber,
            email,
            isNotLocked,
            dateHired,
            dateDeno,
            sickLeaveYearlyCredit,
            sickLeaveYearlyCreditUsed,
            leaveYearlyCredit,
            leaveYearlyCreditUsed,
            userId,
            positionId,
            dutyScheduleId,
            dailyTimeRecordId,
            benefitsId,
            dependentsId,
            educationId,
            trainingHistoryId,
            leaveId,
            departmentId,
            employmentTypeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (middleName != null ? "middleName=" + middleName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (nameSuffix != null ? "nameSuffix=" + nameSuffix + ", " : "") +
            (birthdate != null ? "birthdate=" + birthdate + ", " : "") +
            (sex != null ? "sex=" + sex + ", " : "") +
            (mobileNumber != null ? "mobileNumber=" + mobileNumber + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (isNotLocked != null ? "isNotLocked=" + isNotLocked + ", " : "") +
            (dateHired != null ? "dateHired=" + dateHired + ", " : "") +
            (dateDeno != null ? "dateDeno=" + dateDeno + ", " : "") +
            (sickLeaveYearlyCredit != null ? "sickLeaveYearlyCredit=" + sickLeaveYearlyCredit + ", " : "") +
            (sickLeaveYearlyCreditUsed != null ? "sickLeaveYearlyCreditUsed=" + sickLeaveYearlyCreditUsed + ", " : "") +
            (leaveYearlyCredit != null ? "leaveYearlyCredit=" + leaveYearlyCredit + ", " : "") +
            (leaveYearlyCreditUsed != null ? "leaveYearlyCreditUsed=" + leaveYearlyCreditUsed + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (positionId != null ? "positionId=" + positionId + ", " : "") +
            (dutyScheduleId != null ? "dutyScheduleId=" + dutyScheduleId + ", " : "") +
            (dailyTimeRecordId != null ? "dailyTimeRecordId=" + dailyTimeRecordId + ", " : "") +
            (benefitsId != null ? "benefitsId=" + benefitsId + ", " : "") +
            (dependentsId != null ? "dependentsId=" + dependentsId + ", " : "") +
            (educationId != null ? "educationId=" + educationId + ", " : "") +
            (trainingHistoryId != null ? "trainingHistoryId=" + trainingHistoryId + ", " : "") +
            (leaveId != null ? "leaveId=" + leaveId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (employmentTypeId != null ? "employmentTypeId=" + employmentTypeId + ", " : "") +
            "}";
    }
}
