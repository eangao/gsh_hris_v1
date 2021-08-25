package com.gsh.hris.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gsh.hris.domain.DailyTimeRecord} entity. This class is used
 * in {@link com.gsh.hris.web.rest.DailyTimeRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /daily-time-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DailyTimeRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateTimeIn;

    private InstantFilter dateTimeOut;

    private LongFilter employeeId;

    public DailyTimeRecordCriteria() {}

    public DailyTimeRecordCriteria(DailyTimeRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateTimeIn = other.dateTimeIn == null ? null : other.dateTimeIn.copy();
        this.dateTimeOut = other.dateTimeOut == null ? null : other.dateTimeOut.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
    }

    @Override
    public DailyTimeRecordCriteria copy() {
        return new DailyTimeRecordCriteria(this);
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

    public InstantFilter getDateTimeIn() {
        return dateTimeIn;
    }

    public InstantFilter dateTimeIn() {
        if (dateTimeIn == null) {
            dateTimeIn = new InstantFilter();
        }
        return dateTimeIn;
    }

    public void setDateTimeIn(InstantFilter dateTimeIn) {
        this.dateTimeIn = dateTimeIn;
    }

    public InstantFilter getDateTimeOut() {
        return dateTimeOut;
    }

    public InstantFilter dateTimeOut() {
        if (dateTimeOut == null) {
            dateTimeOut = new InstantFilter();
        }
        return dateTimeOut;
    }

    public void setDateTimeOut(InstantFilter dateTimeOut) {
        this.dateTimeOut = dateTimeOut;
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
        final DailyTimeRecordCriteria that = (DailyTimeRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateTimeIn, that.dateTimeIn) &&
            Objects.equals(dateTimeOut, that.dateTimeOut) &&
            Objects.equals(employeeId, that.employeeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTimeIn, dateTimeOut, employeeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyTimeRecordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateTimeIn != null ? "dateTimeIn=" + dateTimeIn + ", " : "") +
            (dateTimeOut != null ? "dateTimeOut=" + dateTimeOut + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            "}";
    }
}
