package com.gsh.hris.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gsh.hris.domain.Holiday} entity.
 */
public class HolidayDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HolidayDTO)) {
            return false;
        }

        HolidayDTO holidayDTO = (HolidayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, holidayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HolidayDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
