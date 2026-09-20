package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ShoppingListTestSamples.*;
import static com.snp.domain.ShoppingListTypeTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShoppingListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingList.class);
        ShoppingList shoppingList1 = getShoppingListSample1();
        ShoppingList shoppingList2 = new ShoppingList();
        assertThat(shoppingList1).isNotEqualTo(shoppingList2);

        shoppingList2.setId(shoppingList1.getId());
        assertThat(shoppingList1).isEqualTo(shoppingList2);

        shoppingList2 = getShoppingListSample2();
        assertThat(shoppingList1).isNotEqualTo(shoppingList2);
    }

    @Test
    void shoppingListTypeTest() {
        ShoppingList shoppingList = getShoppingListRandomSampleGenerator();
        ShoppingListType shoppingListTypeBack = getShoppingListTypeRandomSampleGenerator();

        shoppingList.setShoppingListType(shoppingListTypeBack);
        assertThat(shoppingList.getShoppingListType()).isEqualTo(shoppingListTypeBack);

        shoppingList.shoppingListType(null);
        assertThat(shoppingList.getShoppingListType()).isNull();
    }

    @Test
    void partyTest() {
        ShoppingList shoppingList = getShoppingListRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        shoppingList.setParty(partyBack);
        assertThat(shoppingList.getParty()).isEqualTo(partyBack);

        shoppingList.party(null);
        assertThat(shoppingList.getParty()).isNull();
    }

    @Test
    void statusTest() {
        ShoppingList shoppingList = getShoppingListRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        shoppingList.setStatus(statusBack);
        assertThat(shoppingList.getStatus()).isEqualTo(statusBack);

        shoppingList.status(null);
        assertThat(shoppingList.getStatus()).isNull();
    }
}
