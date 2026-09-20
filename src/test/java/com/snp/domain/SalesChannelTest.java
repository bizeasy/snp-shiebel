package com.snp.domain;

import static com.snp.domain.SalesChannelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalesChannelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesChannel.class);
        SalesChannel salesChannel1 = getSalesChannelSample1();
        SalesChannel salesChannel2 = new SalesChannel();
        assertThat(salesChannel1).isNotEqualTo(salesChannel2);

        salesChannel2.setId(salesChannel1.getId());
        assertThat(salesChannel1).isEqualTo(salesChannel2);

        salesChannel2 = getSalesChannelSample2();
        assertThat(salesChannel1).isNotEqualTo(salesChannel2);
    }
}
