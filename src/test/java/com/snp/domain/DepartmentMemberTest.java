package com.snp.domain;

import static com.snp.domain.DepartmentMemberTestSamples.*;
import static com.snp.domain.DepartmentTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartmentMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartmentMember.class);
        DepartmentMember departmentMember1 = getDepartmentMemberSample1();
        DepartmentMember departmentMember2 = new DepartmentMember();
        assertThat(departmentMember1).isNotEqualTo(departmentMember2);

        departmentMember2.setId(departmentMember1.getId());
        assertThat(departmentMember1).isEqualTo(departmentMember2);

        departmentMember2 = getDepartmentMemberSample2();
        assertThat(departmentMember1).isNotEqualTo(departmentMember2);
    }

    @Test
    void departmentTest() {
        DepartmentMember departmentMember = getDepartmentMemberRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        departmentMember.setDepartment(departmentBack);
        assertThat(departmentMember.getDepartment()).isEqualTo(departmentBack);

        departmentMember.department(null);
        assertThat(departmentMember.getDepartment()).isNull();
    }

    @Test
    void partyTest() {
        DepartmentMember departmentMember = getDepartmentMemberRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        departmentMember.setParty(partyBack);
        assertThat(departmentMember.getParty()).isEqualTo(partyBack);

        departmentMember.party(null);
        assertThat(departmentMember.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        DepartmentMember departmentMember = getDepartmentMemberRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        departmentMember.setRoleType(roleTypeBack);
        assertThat(departmentMember.getRoleType()).isEqualTo(roleTypeBack);

        departmentMember.roleType(null);
        assertThat(departmentMember.getRoleType()).isNull();
    }
}
