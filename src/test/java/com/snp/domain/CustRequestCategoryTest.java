package com.snp.domain;

import static com.snp.domain.CustRequestCategoryTestSamples.*;
import static com.snp.domain.CustRequestTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustRequestCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustRequestCategory.class);
        CustRequestCategory custRequestCategory1 = getCustRequestCategorySample1();
        CustRequestCategory custRequestCategory2 = new CustRequestCategory();
        assertThat(custRequestCategory1).isNotEqualTo(custRequestCategory2);

        custRequestCategory2.setId(custRequestCategory1.getId());
        assertThat(custRequestCategory1).isEqualTo(custRequestCategory2);

        custRequestCategory2 = getCustRequestCategorySample2();
        assertThat(custRequestCategory1).isNotEqualTo(custRequestCategory2);
    }

    @Test
    void custRequestTypeTest() {
        CustRequestCategory custRequestCategory = getCustRequestCategoryRandomSampleGenerator();
        CustRequestType custRequestTypeBack = getCustRequestTypeRandomSampleGenerator();

        custRequestCategory.setCustRequestType(custRequestTypeBack);
        assertThat(custRequestCategory.getCustRequestType()).isEqualTo(custRequestTypeBack);

        custRequestCategory.custRequestType(null);
        assertThat(custRequestCategory.getCustRequestType()).isNull();
    }
}
