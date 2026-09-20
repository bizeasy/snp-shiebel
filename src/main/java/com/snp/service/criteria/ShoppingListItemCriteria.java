package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ShoppingListItem} entity. This class is used
 * in {@link com.snp.web.rest.ShoppingListItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shopping-list-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShoppingListItemCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private BigDecimalFilter quantity;

    private InstantFilter requiredByDate;

    private BigDecimalFilter selectedAmount;

    private BigDecimalFilter maxAmount;

    private StringFilter description;

    private StringFilter story;

    private LongFilter shoppingListId;

    private LongFilter productId;

    private LongFilter statusId;

    private Boolean distinct;

    public ShoppingListItemCriteria() {}

    public ShoppingListItemCriteria(ShoppingListItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.requiredByDate = other.optionalRequiredByDate().map(InstantFilter::copy).orElse(null);
        this.selectedAmount = other.optionalSelectedAmount().map(BigDecimalFilter::copy).orElse(null);
        this.maxAmount = other.optionalMaxAmount().map(BigDecimalFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.story = other.optionalStory().map(StringFilter::copy).orElse(null);
        this.shoppingListId = other.optionalShoppingListId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ShoppingListItemCriteria copy() {
        return new ShoppingListItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getSequenceNo() {
        return sequenceNo;
    }

    public Optional<IntegerFilter> optionalSequenceNo() {
        return Optional.ofNullable(sequenceNo);
    }

    public IntegerFilter sequenceNo() {
        if (sequenceNo == null) {
            setSequenceNo(new IntegerFilter());
        }
        return sequenceNo;
    }

    public void setSequenceNo(IntegerFilter sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimalFilter getQuantity() {
        return quantity;
    }

    public Optional<BigDecimalFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public BigDecimalFilter quantity() {
        if (quantity == null) {
            setQuantity(new BigDecimalFilter());
        }
        return quantity;
    }

    public void setQuantity(BigDecimalFilter quantity) {
        this.quantity = quantity;
    }

    public InstantFilter getRequiredByDate() {
        return requiredByDate;
    }

    public Optional<InstantFilter> optionalRequiredByDate() {
        return Optional.ofNullable(requiredByDate);
    }

    public InstantFilter requiredByDate() {
        if (requiredByDate == null) {
            setRequiredByDate(new InstantFilter());
        }
        return requiredByDate;
    }

    public void setRequiredByDate(InstantFilter requiredByDate) {
        this.requiredByDate = requiredByDate;
    }

    public BigDecimalFilter getSelectedAmount() {
        return selectedAmount;
    }

    public Optional<BigDecimalFilter> optionalSelectedAmount() {
        return Optional.ofNullable(selectedAmount);
    }

    public BigDecimalFilter selectedAmount() {
        if (selectedAmount == null) {
            setSelectedAmount(new BigDecimalFilter());
        }
        return selectedAmount;
    }

    public void setSelectedAmount(BigDecimalFilter selectedAmount) {
        this.selectedAmount = selectedAmount;
    }

    public BigDecimalFilter getMaxAmount() {
        return maxAmount;
    }

    public Optional<BigDecimalFilter> optionalMaxAmount() {
        return Optional.ofNullable(maxAmount);
    }

    public BigDecimalFilter maxAmount() {
        if (maxAmount == null) {
            setMaxAmount(new BigDecimalFilter());
        }
        return maxAmount;
    }

    public void setMaxAmount(BigDecimalFilter maxAmount) {
        this.maxAmount = maxAmount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getStory() {
        return story;
    }

    public Optional<StringFilter> optionalStory() {
        return Optional.ofNullable(story);
    }

    public StringFilter story() {
        if (story == null) {
            setStory(new StringFilter());
        }
        return story;
    }

    public void setStory(StringFilter story) {
        this.story = story;
    }

    public LongFilter getShoppingListId() {
        return shoppingListId;
    }

    public Optional<LongFilter> optionalShoppingListId() {
        return Optional.ofNullable(shoppingListId);
    }

    public LongFilter shoppingListId() {
        if (shoppingListId == null) {
            setShoppingListId(new LongFilter());
        }
        return shoppingListId;
    }

    public void setShoppingListId(LongFilter shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public Optional<LongFilter> optionalStatusId() {
        return Optional.ofNullable(statusId);
    }

    public LongFilter statusId() {
        if (statusId == null) {
            setStatusId(new LongFilter());
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShoppingListItemCriteria that = (ShoppingListItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(requiredByDate, that.requiredByDate) &&
            Objects.equals(selectedAmount, that.selectedAmount) &&
            Objects.equals(maxAmount, that.maxAmount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(story, that.story) &&
            Objects.equals(shoppingListId, that.shoppingListId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNo,
            quantity,
            requiredByDate,
            selectedAmount,
            maxAmount,
            description,
            story,
            shoppingListId,
            productId,
            statusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingListItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalRequiredByDate().map(f -> "requiredByDate=" + f + ", ").orElse("") +
            optionalSelectedAmount().map(f -> "selectedAmount=" + f + ", ").orElse("") +
            optionalMaxAmount().map(f -> "maxAmount=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStory().map(f -> "story=" + f + ", ").orElse("") +
            optionalShoppingListId().map(f -> "shoppingListId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
