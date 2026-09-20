package com.snp.domain;

import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.StatusValidChangeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusValidChangeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusValidChange.class);
        StatusValidChange statusValidChange1 = getStatusValidChangeSample1();
        StatusValidChange statusValidChange2 = new StatusValidChange();
        assertThat(statusValidChange1).isNotEqualTo(statusValidChange2);

        statusValidChange2.setId(statusValidChange1.getId());
        assertThat(statusValidChange1).isEqualTo(statusValidChange2);

        statusValidChange2 = getStatusValidChangeSample2();
        assertThat(statusValidChange1).isNotEqualTo(statusValidChange2);
    }

    @Test
    void statusTest() {
        StatusValidChange statusValidChange = getStatusValidChangeRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        statusValidChange.setStatus(statusBack);
        assertThat(statusValidChange.getStatus()).isEqualTo(statusBack);

        statusValidChange.status(null);
        assertThat(statusValidChange.getStatus()).isNull();
    }

    @Test
    void statusToTest() {
        StatusValidChange statusValidChange = getStatusValidChangeRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        statusValidChange.setStatusTo(statusBack);
        assertThat(statusValidChange.getStatusTo()).isEqualTo(statusBack);

        statusValidChange.statusTo(null);
        assertThat(statusValidChange.getStatusTo()).isNull();
    }
}
