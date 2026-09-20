package com.snp.domain;

import static com.snp.domain.ReturnHeaderTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReturnHeaderTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnHeaderType.class);
        ReturnHeaderType returnHeaderType1 = getReturnHeaderTypeSample1();
        ReturnHeaderType returnHeaderType2 = new ReturnHeaderType();
        assertThat(returnHeaderType1).isNotEqualTo(returnHeaderType2);

        returnHeaderType2.setId(returnHeaderType1.getId());
        assertThat(returnHeaderType1).isEqualTo(returnHeaderType2);

        returnHeaderType2 = getReturnHeaderTypeSample2();
        assertThat(returnHeaderType1).isNotEqualTo(returnHeaderType2);
    }
}
