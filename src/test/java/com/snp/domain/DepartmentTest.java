package com.snp.domain;

import static com.snp.domain.DepartmentTestSamples.*;
import static com.snp.domain.DepartmentTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductStoreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Department.class);
        Department department1 = getDepartmentSample1();
        Department department2 = new Department();
        assertThat(department1).isNotEqualTo(department2);

        department2.setId(department1.getId());
        assertThat(department1).isEqualTo(department2);

        department2 = getDepartmentSample2();
        assertThat(department1).isNotEqualTo(department2);
    }

    @Test
    void parentTest() {
        Department department = getDepartmentRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        department.setParent(departmentBack);
        assertThat(department.getParent()).isEqualTo(departmentBack);

        department.parent(null);
        assertThat(department.getParent()).isNull();
    }

    @Test
    void headTest() {
        Department department = getDepartmentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        department.setHead(partyBack);
        assertThat(department.getHead()).isEqualTo(partyBack);

        department.head(null);
        assertThat(department.getHead()).isNull();
    }

    @Test
    void productStoreTest() {
        Department department = getDepartmentRandomSampleGenerator();
        ProductStore productStoreBack = getProductStoreRandomSampleGenerator();

        department.setProductStore(productStoreBack);
        assertThat(department.getProductStore()).isEqualTo(productStoreBack);

        department.productStore(null);
        assertThat(department.getProductStore()).isNull();
    }
}
