package com.gsh.hris.service.dto;

import com.gsh.hris.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmploymentTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentTypeDTO.class);
        EmploymentTypeDTO employmentTypeDTO1 = new EmploymentTypeDTO();
        employmentTypeDTO1.setId(1L);
        EmploymentTypeDTO employmentTypeDTO2 = new EmploymentTypeDTO();
        assertThat(employmentTypeDTO1).isNotEqualTo(employmentTypeDTO2);
        employmentTypeDTO2.setId(employmentTypeDTO1.getId());
        assertThat(employmentTypeDTO1).isEqualTo(employmentTypeDTO2);
        employmentTypeDTO2.setId(2L);
        assertThat(employmentTypeDTO1).isNotEqualTo(employmentTypeDTO2);
        employmentTypeDTO1.setId(null);
        assertThat(employmentTypeDTO1).isNotEqualTo(employmentTypeDTO2);
    }
}
