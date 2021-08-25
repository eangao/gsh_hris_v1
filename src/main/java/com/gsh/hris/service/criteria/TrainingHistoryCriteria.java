package com.gsh.hris.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gsh.hris.domain.TrainingHistory} entity. This class is used
 * in {@link com.gsh.hris.web.rest.TrainingHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /training-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TrainingHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter trainingName;

    private LocalDateFilter trainingDate;

    private LongFilter employeeId;

    public TrainingHistoryCriteria() {}

    public TrainingHistoryCriteria(TrainingHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.trainingName = other.trainingName == null ? null : other.trainingName.copy();
        this.trainingDate = other.trainingDate == null ? null : other.trainingDate.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
    }

    @Override
    public TrainingHistoryCriteria copy() {
        return new TrainingHistoryCriteria(this);
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

    public StringFilter getTrainingName() {
        return trainingName;
    }

    public StringFilter trainingName() {
        if (trainingName == null) {
            trainingName = new StringFilter();
        }
        return trainingName;
    }

    public void setTrainingName(StringFilter trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDateFilter getTrainingDate() {
        return trainingDate;
    }

    public LocalDateFilter trainingDate() {
        if (trainingDate == null) {
            trainingDate = new LocalDateFilter();
        }
        return trainingDate;
    }

    public void setTrainingDate(LocalDateFilter trainingDate) {
        this.trainingDate = trainingDate;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TrainingHistoryCriteria that = (TrainingHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(trainingName, that.trainingName) &&
            Objects.equals(trainingDate, that.trainingDate) &&
            Objects.equals(employeeId, that.employeeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainingName, trainingDate, employeeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingHistoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (trainingName != null ? "trainingName=" + trainingName + ", " : "") +
            (trainingDate != null ? "trainingDate=" + trainingDate + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            "}";
    }
}
