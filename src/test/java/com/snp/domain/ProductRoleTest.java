package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductRoleTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductRole.class);
        ProductRole productRole1 = getProductRoleSample1();
        ProductRole productRole2 = new ProductRole();
        assertThat(productRole1).isNotEqualTo(productRole2);

        productRole2.setId(productRole1.getId());
        assertThat(productRole1).isEqualTo(productRole2);

        productRole2 = getProductRoleSample2();
        assertThat(productRole1).isNotEqualTo(productRole2);
    }

    @Test
    void productTest() {
        ProductRole productRole = getProductRoleRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productRole.setProduct(productBack);
        assertThat(productRole.getProduct()).isEqualTo(productBack);

        productRole.product(null);
        assertThat(productRole.getProduct()).isNull();
    }

    @Test
    void partyTest() {
        ProductRole productRole = getProductRoleRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        productRole.setParty(partyBack);
        assertThat(productRole.getParty()).isEqualTo(partyBack);

        productRole.party(null);
        assertThat(productRole.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        ProductRole productRole = getProductRoleRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        productRole.setRoleType(roleTypeBack);
        assertThat(productRole.getRoleType()).isEqualTo(roleTypeBack);

        productRole.roleType(null);
        assertThat(productRole.getRoleType()).isNull();
    }
}
