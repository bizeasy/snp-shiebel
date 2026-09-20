package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductStoreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductStore.class);
        ProductStore productStore1 = getProductStoreSample1();
        ProductStore productStore2 = new ProductStore();
        assertThat(productStore1).isNotEqualTo(productStore2);

        productStore2.setId(productStore1.getId());
        assertThat(productStore1).isEqualTo(productStore2);

        productStore2 = getProductStoreSample2();
        assertThat(productStore1).isNotEqualTo(productStore2);
    }

    @Test
    void ownerTest() {
        ProductStore productStore = getProductStoreRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        productStore.setOwner(partyBack);
        assertThat(productStore.getOwner()).isEqualTo(partyBack);

        productStore.owner(null);
        assertThat(productStore.getOwner()).isNull();
    }

    @Test
    void adminTest() {
        ProductStore productStore = getProductStoreRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        productStore.setAdmin(partyBack);
        assertThat(productStore.getAdmin()).isEqualTo(partyBack);

        productStore.admin(null);
        assertThat(productStore.getAdmin()).isNull();
    }
}
