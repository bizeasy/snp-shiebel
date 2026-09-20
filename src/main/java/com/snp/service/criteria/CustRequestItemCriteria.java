package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.CustRequestItem} entity. This class is used
 * in {@link com.snp.web.rest.CustRequestItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cust-request-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustRequestItemCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private IntegerFilter priority;

    private InstantFilter requiredByDate;

    private BigDecimalFilter quantity;

    private BigDecimalFilter selectedAmount;

    private BigDecimalFilter maxAmount;

    private StringFilter description;

    private StringFilter story;

    private LongFilter custRequestId;

    private LongFilter statusId;

    private LongFilter productId;

    private LongFilter uomId;

    private LongFilter shoppingListItemId;

    private Boolean distinct;

    public CustRequestItemCriteria() {}

    public CustRequestItemCriteria(CustRequestItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.requiredByDate = other.optionalRequiredByDate().map(InstantFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.selectedAmount = other.optionalSelectedAmount().map(BigDecimalFilter::copy).orElse(null);
        this.maxAmount = other.optionalMaxAmount().map(BigDecimalFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.story = other.optionalStory().map(StringFilter::copy).orElse(null);
        this.custRequestId = other.optionalCustRequestId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(LongFilter::copy).orElse(null);
        this.shoppingListItemId = other.optionalShoppingListItemId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustRequestItemCriteria copy() {
        return new CustRequestItemCriteria(this);
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

    public IntegerFilter getPriority() {
        return priority;
    }

    public Optional<IntegerFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public IntegerFilter priority() {
        if (priority == null) {
            setPriority(new IntegerFilter());
        }
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
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

    public LongFilter getCustRequestId() {
        return custRequestId;
    }

    public Optional<LongFilter> optionalCustRequestId() {
        return Optional.ofNullable(custRequestId);
    }

    public LongFilter custRequestId() {
        if (custRequestId == null) {
            setCustRequestId(new LongFilter());
        }
        return custRequestId;
    }

    public void setCustRequestId(LongFilter custRequestId) {
        this.custRequestId = custRequestId;
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

    public LongFilter getUomId() {
        return uomId;
    }

    public Optional<LongFilter> optionalUomId() {
        return Optional.ofNullable(uomId);
    }

    public LongFilter uomId() {
        if (uomId == null) {
            setUomId(new LongFilter());
        }
        return uomId;
    }

    public void setUomId(LongFilter uomId) {
        this.uomId = uomId;
    }

    public LongFilter getShoppingListItemId() {
        return shoppingListItemId;
    }

    public Optional<LongFilter> optionalShoppingListItemId() {
        return Optional.ofNullable(shoppingListItemId);
    }

    public LongFilter shoppingListItemId() {
        if (shoppingListItemId == null) {
            setShoppingListItemId(new LongFilter());
        }
        return shoppingListItemId;
    }

    public void setShoppingListItemId(LongFilter shoppingListItemId) {
        this.shoppingListItemId = shoppingListItemId;
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
        final CustRequestItemCriteria that = (CustRequestItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(requiredByDate, that.requiredByDate) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(selectedAmount, that.selectedAmount) &&
            Objects.equals(maxAmount, that.maxAmount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(story, that.story) &&
            Objects.equals(custRequestId, that.custRequestId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(uomId, that.uomId) &&
            Objects.equals(shoppingListItemId, that.shoppingListItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNo,
            priority,
            requiredByDate,
            quantity,
            selectedAmount,
            maxAmount,
            description,
            story,
            custRequestId,
            statusId,
            productId,
            uomId,
            shoppingListItemId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustRequestItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalRequiredByDate().map(f -> "requiredByDate=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalSelectedAmount().map(f -> "selectedAmount=" + f + ", ").orElse("") +
            optionalMaxAmount().map(f -> "maxAmount=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStory().map(f -> "story=" + f + ", ").orElse("") +
            optionalCustRequestId().map(f -> "custRequestId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalUomId().map(f -> "uomId=" + f + ", ").orElse("") +
            optionalShoppingListItemId().map(f -> "shoppingListItemId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
