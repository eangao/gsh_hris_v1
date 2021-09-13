package com.gsh.hris.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "employee_id", nullable = false, unique = true)
    private Integer employeeId;

    @NotNull
    @Size(min = 6, max = 50)
    @Column(name = "username", length = 30, nullable = false, unique = true)
    private String username;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(max = 15)
    @Column(name = "name_suffix", length = 15)
    private String nameSuffix;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "sex")
    private Boolean sex;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Email
    @Size(min = 5, max = 254)
    @Column(name = "email", length = 254, unique = true)
    private String email;

    @Column(name = "is_not_locked")
    private Boolean isNotLocked;

    @Column(name = "date_hired")
    private LocalDate dateHired;

    @Column(name = "date_deno")
    private LocalDate dateDeno;

    @Column(name = "sick_leave_yearly_credit")
    private Integer sickLeaveYearlyCredit;

    @Column(name = "sick_leave_yearly_credit_used")
    private Integer sickLeaveYearlyCreditUsed;

    @Column(name = "leave_yearly_credit")
    private Integer leaveYearlyCredit;

    @Column(name = "leave_yearly_credit_used")
    private Integer leaveYearlyCreditUsed;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Position> positions = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<DutySchedule> dutySchedules = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<DailyTimeRecord> dailyTimeRecords = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Benefits> benefits = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Dependents> dependents = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<TrainingHistory> trainingHistories = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee", "leaveType" }, allowSetters = true)
    private Set<Leave> leaves = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "employees", "cluster" }, allowSetters = true)
    private Department department;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private EmploymentType employmentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public Employee employeeId(Integer employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return this.username;
    }

    public Employee username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Employee middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameSuffix() {
        return this.nameSuffix;
    }

    public Employee nameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
        return this;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public Employee birthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getSex() {
        return this.sex;
    }

    public Employee sex(Boolean sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public Employee mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsNotLocked() {
        return this.isNotLocked;
    }

    public Employee isNotLocked(Boolean isNotLocked) {
        this.isNotLocked = isNotLocked;
        return this;
    }

    public void setIsNotLocked(Boolean isNotLocked) {
        this.isNotLocked = isNotLocked;
    }

    public LocalDate getDateHired() {
        return this.dateHired;
    }

    public Employee dateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
        return this;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDate getDateDeno() {
        return this.dateDeno;
    }

    public Employee dateDeno(LocalDate dateDeno) {
        this.dateDeno = dateDeno;
        return this;
    }

    public void setDateDeno(LocalDate dateDeno) {
        this.dateDeno = dateDeno;
    }

    public Integer getSickLeaveYearlyCredit() {
        return this.sickLeaveYearlyCredit;
    }

    public Employee sickLeaveYearlyCredit(Integer sickLeaveYearlyCredit) {
        this.sickLeaveYearlyCredit = sickLeaveYearlyCredit;
        return this;
    }

    public void setSickLeaveYearlyCredit(Integer sickLeaveYearlyCredit) {
        this.sickLeaveYearlyCredit = sickLeaveYearlyCredit;
    }

    public Integer getSickLeaveYearlyCreditUsed() {
        return this.sickLeaveYearlyCreditUsed;
    }

    public Employee sickLeaveYearlyCreditUsed(Integer sickLeaveYearlyCreditUsed) {
        this.sickLeaveYearlyCreditUsed = sickLeaveYearlyCreditUsed;
        return this;
    }

    public void setSickLeaveYearlyCreditUsed(Integer sickLeaveYearlyCreditUsed) {
        this.sickLeaveYearlyCreditUsed = sickLeaveYearlyCreditUsed;
    }

    public Integer getLeaveYearlyCredit() {
        return this.leaveYearlyCredit;
    }

    public Employee leaveYearlyCredit(Integer leaveYearlyCredit) {
        this.leaveYearlyCredit = leaveYearlyCredit;
        return this;
    }

    public void setLeaveYearlyCredit(Integer leaveYearlyCredit) {
        this.leaveYearlyCredit = leaveYearlyCredit;
    }

    public Integer getLeaveYearlyCreditUsed() {
        return this.leaveYearlyCreditUsed;
    }

    public Employee leaveYearlyCreditUsed(Integer leaveYearlyCreditUsed) {
        this.leaveYearlyCreditUsed = leaveYearlyCreditUsed;
        return this;
    }

    public void setLeaveYearlyCreditUsed(Integer leaveYearlyCreditUsed) {
        this.leaveYearlyCreditUsed = leaveYearlyCreditUsed;
    }

    public User getUser() {
        return this.user;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Position> getPositions() {
        return this.positions;
    }

    public Employee positions(Set<Position> positions) {
        this.setPositions(positions);
        return this;
    }

    public Employee addPosition(Position position) {
        this.positions.add(position);
        position.setEmployee(this);
        return this;
    }

    public Employee removePosition(Position position) {
        this.positions.remove(position);
        position.setEmployee(null);
        return this;
    }

    public void setPositions(Set<Position> positions) {
        if (this.positions != null) {
            this.positions.forEach(i -> i.setEmployee(null));
        }
        if (positions != null) {
            positions.forEach(i -> i.setEmployee(this));
        }
        this.positions = positions;
    }

    public Set<DutySchedule> getDutySchedules() {
        return this.dutySchedules;
    }

    public Employee dutySchedules(Set<DutySchedule> dutySchedules) {
        this.setDutySchedules(dutySchedules);
        return this;
    }

    public Employee addDutySchedule(DutySchedule dutySchedule) {
        this.dutySchedules.add(dutySchedule);
        dutySchedule.setEmployee(this);
        return this;
    }

    public Employee removeDutySchedule(DutySchedule dutySchedule) {
        this.dutySchedules.remove(dutySchedule);
        dutySchedule.setEmployee(null);
        return this;
    }

    public void setDutySchedules(Set<DutySchedule> dutySchedules) {
        if (this.dutySchedules != null) {
            this.dutySchedules.forEach(i -> i.setEmployee(null));
        }
        if (dutySchedules != null) {
            dutySchedules.forEach(i -> i.setEmployee(this));
        }
        this.dutySchedules = dutySchedules;
    }

    public Set<DailyTimeRecord> getDailyTimeRecords() {
        return this.dailyTimeRecords;
    }

    public Employee dailyTimeRecords(Set<DailyTimeRecord> dailyTimeRecords) {
        this.setDailyTimeRecords(dailyTimeRecords);
        return this;
    }

    public Employee addDailyTimeRecord(DailyTimeRecord dailyTimeRecord) {
        this.dailyTimeRecords.add(dailyTimeRecord);
        dailyTimeRecord.setEmployee(this);
        return this;
    }

    public Employee removeDailyTimeRecord(DailyTimeRecord dailyTimeRecord) {
        this.dailyTimeRecords.remove(dailyTimeRecord);
        dailyTimeRecord.setEmployee(null);
        return this;
    }

    public void setDailyTimeRecords(Set<DailyTimeRecord> dailyTimeRecords) {
        if (this.dailyTimeRecords != null) {
            this.dailyTimeRecords.forEach(i -> i.setEmployee(null));
        }
        if (dailyTimeRecords != null) {
            dailyTimeRecords.forEach(i -> i.setEmployee(this));
        }
        this.dailyTimeRecords = dailyTimeRecords;
    }

    public Set<Benefits> getBenefits() {
        return this.benefits;
    }

    public Employee benefits(Set<Benefits> benefits) {
        this.setBenefits(benefits);
        return this;
    }

    public Employee addBenefits(Benefits benefits) {
        this.benefits.add(benefits);
        benefits.setEmployee(this);
        return this;
    }

    public Employee removeBenefits(Benefits benefits) {
        this.benefits.remove(benefits);
        benefits.setEmployee(null);
        return this;
    }

    public void setBenefits(Set<Benefits> benefits) {
        if (this.benefits != null) {
            this.benefits.forEach(i -> i.setEmployee(null));
        }
        if (benefits != null) {
            benefits.forEach(i -> i.setEmployee(this));
        }
        this.benefits = benefits;
    }

    public Set<Dependents> getDependents() {
        return this.dependents;
    }

    public Employee dependents(Set<Dependents> dependents) {
        this.setDependents(dependents);
        return this;
    }

    public Employee addDependents(Dependents dependents) {
        this.dependents.add(dependents);
        dependents.setEmployee(this);
        return this;
    }

    public Employee removeDependents(Dependents dependents) {
        this.dependents.remove(dependents);
        dependents.setEmployee(null);
        return this;
    }

    public void setDependents(Set<Dependents> dependents) {
        if (this.dependents != null) {
            this.dependents.forEach(i -> i.setEmployee(null));
        }
        if (dependents != null) {
            dependents.forEach(i -> i.setEmployee(this));
        }
        this.dependents = dependents;
    }

    public Set<Education> getEducations() {
        return this.educations;
    }

    public Employee educations(Set<Education> educations) {
        this.setEducations(educations);
        return this;
    }

    public Employee addEducation(Education education) {
        this.educations.add(education);
        education.setEmployee(this);
        return this;
    }

    public Employee removeEducation(Education education) {
        this.educations.remove(education);
        education.setEmployee(null);
        return this;
    }

    public void setEducations(Set<Education> educations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setEmployee(null));
        }
        if (educations != null) {
            educations.forEach(i -> i.setEmployee(this));
        }
        this.educations = educations;
    }

    public Set<TrainingHistory> getTrainingHistories() {
        return this.trainingHistories;
    }

    public Employee trainingHistories(Set<TrainingHistory> trainingHistories) {
        this.setTrainingHistories(trainingHistories);
        return this;
    }

    public Employee addTrainingHistory(TrainingHistory trainingHistory) {
        this.trainingHistories.add(trainingHistory);
        trainingHistory.setEmployee(this);
        return this;
    }

    public Employee removeTrainingHistory(TrainingHistory trainingHistory) {
        this.trainingHistories.remove(trainingHistory);
        trainingHistory.setEmployee(null);
        return this;
    }

    public void setTrainingHistories(Set<TrainingHistory> trainingHistories) {
        if (this.trainingHistories != null) {
            this.trainingHistories.forEach(i -> i.setEmployee(null));
        }
        if (trainingHistories != null) {
            trainingHistories.forEach(i -> i.setEmployee(this));
        }
        this.trainingHistories = trainingHistories;
    }

    public Set<Leave> getLeaves() {
        return this.leaves;
    }

    public Employee leaves(Set<Leave> leaves) {
        this.setLeaves(leaves);
        return this;
    }

    public Employee addLeave(Leave leave) {
        this.leaves.add(leave);
        leave.setEmployee(this);
        return this;
    }

    public Employee removeLeave(Leave leave) {
        this.leaves.remove(leave);
        leave.setEmployee(null);
        return this;
    }

    public void setLeaves(Set<Leave> leaves) {
        if (this.leaves != null) {
            this.leaves.forEach(i -> i.setEmployee(null));
        }
        if (leaves != null) {
            leaves.forEach(i -> i.setEmployee(this));
        }
        this.leaves = leaves;
    }

    public Department getDepartment() {
        return this.department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public EmploymentType getEmploymentType() {
        return this.employmentType;
    }

    public Employee employmentType(EmploymentType employmentType) {
        this.setEmploymentType(employmentType);
        return this;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
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
            "}";
    }
}
