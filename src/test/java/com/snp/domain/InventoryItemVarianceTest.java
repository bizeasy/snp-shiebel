package com.snp.domain;

import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.InventoryItemVarianceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryItemVarianceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItemVariance.class);
        InventoryItemVariance inventoryItemVariance1 = getInventoryItemVarianceSample1();
        InventoryItemVariance inventoryItemVariance2 = new InventoryItemVariance();
        assertThat(inventoryItemVariance1).isNotEqualTo(inventoryItemVariance2);

        inventoryItemVariance2.setId(inventoryItemVariance1.getId());
        assertThat(inventoryItemVariance1).isEqualTo(inventoryItemVariance2);

        inventoryItemVariance2 = getInventoryItemVarianceSample2();
        assertThat(inventoryItemVariance1).isNotEqualTo(inventoryItemVariance2);
    }

    @Test
    void inventoryItemTest() {
        InventoryItemVariance inventoryItemVariance = getInventoryItemVarianceRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        inventoryItemVariance.setInventoryItem(inventoryItemBack);
        assertThat(inventoryItemVariance.getInventoryItem()).isEqualTo(inventoryItemBack);

        inventoryItemVariance.inventoryItem(null);
        assertThat(inventoryItemVariance.getInventoryItem()).isNull();
    }
}
