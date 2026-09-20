package com.snp.domain;

import static com.snp.domain.TransactionCategoryTestSamples.*;
import static com.snp.domain.TransactionTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionType.class);
        TransactionType transactionType1 = getTransactionTypeSample1();
        TransactionType transactionType2 = new TransactionType();
        assertThat(transactionType1).isNotEqualTo(transactionType2);

        transactionType2.setId(transactionType1.getId());
        assertThat(transactionType1).isEqualTo(transactionType2);

        transactionType2 = getTransactionTypeSample2();
        assertThat(transactionType1).isNotEqualTo(transactionType2);
    }

    @Test
    void categoryTest() {
        TransactionType transactionType = getTransactionTypeRandomSampleGenerator();
        TransactionCategory transactionCategoryBack = getTransactionCategoryRandomSampleGenerator();

        transactionType.setCategory(transactionCategoryBack);
        assertThat(transactionType.getCategory()).isEqualTo(transactionCategoryBack);

        transactionType.category(null);
        assertThat(transactionType.getCategory()).isNull();
    }
}
