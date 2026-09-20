package com.snp.domain;

import static com.snp.domain.CustRequestItemTestSamples.*;
import static com.snp.domain.CustRequestRoleTestSamples.*;
import static com.snp.domain.CustRequestTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustRequestRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustRequestRole.class);
        CustRequestRole custRequestRole1 = getCustRequestRoleSample1();
        CustRequestRole custRequestRole2 = new CustRequestRole();
        assertThat(custRequestRole1).isNotEqualTo(custRequestRole2);

        custRequestRole2.setId(custRequestRole1.getId());
        assertThat(custRequestRole1).isEqualTo(custRequestRole2);

        custRequestRole2 = getCustRequestRoleSample2();
        assertThat(custRequestRole1).isNotEqualTo(custRequestRole2);
    }

    @Test
    void custRequestTest() {
        CustRequestRole custRequestRole = getCustRequestRoleRandomSampleGenerator();
        CustRequest custRequestBack = getCustRequestRandomSampleGenerator();

        custRequestRole.setCustRequest(custRequestBack);
        assertThat(custRequestRole.getCustRequest()).isEqualTo(custRequestBack);

        custRequestRole.custRequest(null);
        assertThat(custRequestRole.getCustRequest()).isNull();
    }

    @Test
    void custRequestItemTest() {
        CustRequestRole custRequestRole = getCustRequestRoleRandomSampleGenerator();
        CustRequestItem custRequestItemBack = getCustRequestItemRandomSampleGenerator();

        custRequestRole.setCustRequestItem(custRequestItemBack);
        assertThat(custRequestRole.getCustRequestItem()).isEqualTo(custRequestItemBack);

        custRequestRole.custRequestItem(null);
        assertThat(custRequestRole.getCustRequestItem()).isNull();
    }

    @Test
    void partyTest() {
        CustRequestRole custRequestRole = getCustRequestRoleRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        custRequestRole.setParty(partyBack);
        assertThat(custRequestRole.getParty()).isEqualTo(partyBack);

        custRequestRole.party(null);
        assertThat(custRequestRole.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        CustRequestRole custRequestRole = getCustRequestRoleRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        custRequestRole.setRoleType(roleTypeBack);
        assertThat(custRequestRole.getRoleType()).isEqualTo(roleTypeBack);

        custRequestRole.roleType(null);
        assertThat(custRequestRole.getRoleType()).isNull();
    }
}
