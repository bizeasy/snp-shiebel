package com.snp.domain;

import static com.snp.domain.CustomTimePeriodTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomTimePeriodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomTimePeriod.class);
        CustomTimePeriod customTimePeriod1 = getCustomTimePeriodSample1();
        CustomTimePeriod customTimePeriod2 = new CustomTimePeriod();
        assertThat(customTimePeriod1).isNotEqualTo(customTimePeriod2);

        customTimePeriod2.setId(customTimePeriod1.getId());
        assertThat(customTimePeriod1).isEqualTo(customTimePeriod2);

        customTimePeriod2 = getCustomTimePeriodSample2();
        assertThat(customTimePeriod1).isNotEqualTo(customTimePeriod2);
    }
}
