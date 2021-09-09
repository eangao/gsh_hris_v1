package com.gsh.hris.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmploymentTypeMapperTest {

    private EmploymentTypeMapper employmentTypeMapper;

    @BeforeEach
    public void setUp() {
        employmentTypeMapper = new EmploymentTypeMapperImpl();
    }
}
