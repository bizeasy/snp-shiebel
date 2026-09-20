package com.snp.domain;

import static com.snp.domain.SystemParametersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemParametersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemParameters.class);
        SystemParameters systemParameters1 = getSystemParametersSample1();
        SystemParameters systemParameters2 = new SystemParameters();
        assertThat(systemParameters1).isNotEqualTo(systemParameters2);

        systemParameters2.setId(systemParameters1.getId());
        assertThat(systemParameters1).isEqualTo(systemParameters2);

        systemParameters2 = getSystemParametersSample2();
        assertThat(systemParameters1).isNotEqualTo(systemParameters2);
    }
}
