package com.snp.domain;

import static com.snp.domain.ShoppingListTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShoppingListTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingListType.class);
        ShoppingListType shoppingListType1 = getShoppingListTypeSample1();
        ShoppingListType shoppingListType2 = new ShoppingListType();
        assertThat(shoppingListType1).isNotEqualTo(shoppingListType2);

        shoppingListType2.setId(shoppingListType1.getId());
        assertThat(shoppingListType1).isEqualTo(shoppingListType2);

        shoppingListType2 = getShoppingListTypeSample2();
        assertThat(shoppingListType1).isNotEqualTo(shoppingListType2);
    }
}
