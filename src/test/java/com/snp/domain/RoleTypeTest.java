package com.snp.domain;

import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleType.class);
        RoleType roleType1 = getRoleTypeSample1();
        RoleType roleType2 = new RoleType();
        assertThat(roleType1).isNotEqualTo(roleType2);

        roleType2.setId(roleType1.getId());
        assertThat(roleType1).isEqualTo(roleType2);

        roleType2 = getRoleTypeSample2();
        assertThat(roleType1).isNotEqualTo(roleType2);
    }
}
