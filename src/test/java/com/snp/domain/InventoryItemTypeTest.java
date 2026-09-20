package com.snp.domain;

import static com.snp.domain.InventoryItemTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryItemTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItemType.class);
        InventoryItemType inventoryItemType1 = getInventoryItemTypeSample1();
        InventoryItemType inventoryItemType2 = new InventoryItemType();
        assertThat(inventoryItemType1).isNotEqualTo(inventoryItemType2);

        inventoryItemType2.setId(inventoryItemType1.getId());
        assertThat(inventoryItemType1).isEqualTo(inventoryItemType2);

        inventoryItemType2 = getInventoryItemTypeSample2();
        assertThat(inventoryItemType1).isNotEqualTo(inventoryItemType2);
    }
}
