package com.gsh.hris.domain;

import com.gsh.hris.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LeaveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Leave.class);
        Leave leave1 = new Leave();
        leave1.setId(1L);
        Leave leave2 = new Leave();
        leave2.setId(leave1.getId());
        assertThat(leave1).isEqualTo(leave2);
        leave2.setId(2L);
        assertThat(leave1).isNotEqualTo(leave2);
        leave1.setId(null);
        assertThat(leave1).isNotEqualTo(leave2);
    }
}
