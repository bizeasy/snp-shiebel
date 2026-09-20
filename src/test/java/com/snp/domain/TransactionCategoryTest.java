package com.snp.domain;

import static com.snp.domain.TransactionCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionCategory.class);
        TransactionCategory transactionCategory1 = getTransactionCategorySample1();
        TransactionCategory transactionCategory2 = new TransactionCategory();
        assertThat(transactionCategory1).isNotEqualTo(transactionCategory2);

        transactionCategory2.setId(transactionCategory1.getId());
        assertThat(transactionCategory1).isEqualTo(transactionCategory2);

        transactionCategory2 = getTransactionCategorySample2();
        assertThat(transactionCategory1).isNotEqualTo(transactionCategory2);
    }
}
