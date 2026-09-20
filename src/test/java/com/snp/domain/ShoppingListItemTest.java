package com.snp.domain;

import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.ShoppingListItemTestSamples.*;
import static com.snp.domain.ShoppingListTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShoppingListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingListItem.class);
        ShoppingListItem shoppingListItem1 = getShoppingListItemSample1();
        ShoppingListItem shoppingListItem2 = new ShoppingListItem();
        assertThat(shoppingListItem1).isNotEqualTo(shoppingListItem2);

        shoppingListItem2.setId(shoppingListItem1.getId());
        assertThat(shoppingListItem1).isEqualTo(shoppingListItem2);

        shoppingListItem2 = getShoppingListItemSample2();
        assertThat(shoppingListItem1).isNotEqualTo(shoppingListItem2);
    }

    @Test
    void shoppingListTest() {
        ShoppingListItem shoppingListItem = getShoppingListItemRandomSampleGenerator();
        ShoppingList shoppingListBack = getShoppingListRandomSampleGenerator();

        shoppingListItem.setShoppingList(shoppingListBack);
        assertThat(shoppingListItem.getShoppingList()).isEqualTo(shoppingListBack);

        shoppingListItem.shoppingList(null);
        assertThat(shoppingListItem.getShoppingList()).isNull();
    }

    @Test
    void productTest() {
        ShoppingListItem shoppingListItem = getShoppingListItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        shoppingListItem.setProduct(productBack);
        assertThat(shoppingListItem.getProduct()).isEqualTo(productBack);

        shoppingListItem.product(null);
        assertThat(shoppingListItem.getProduct()).isNull();
    }

    @Test
    void statusTest() {
        ShoppingListItem shoppingListItem = getShoppingListItemRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        shoppingListItem.setStatus(statusBack);
        assertThat(shoppingListItem.getStatus()).isEqualTo(statusBack);

        shoppingListItem.status(null);
        assertThat(shoppingListItem.getStatus()).isNull();
    }
}
