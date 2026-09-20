package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ReturnItem} entity. This class is used
 * in {@link com.snp.web.rest.ReturnItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /return-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReturnItemCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private StringFilter description;

    private BigDecimalFilter returnQuantity;

    private BigDecimalFilter receivedQuantity;

    private BigDecimalFilter returnPrice;

    private StringFilter returnItemPurpose;

    private LongFilter returnHeaderId;

    private LongFilter returnTypeId;

    private LongFilter productId;

    private LongFilter orderItemId;

    private LongFilter statusId;

    private Boolean distinct;

    public ReturnItemCriteria() {}

    public ReturnItemCriteria(ReturnItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.returnQuantity = other.optionalReturnQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.receivedQuantity = other.optionalReceivedQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.returnPrice = other.optionalReturnPrice().map(BigDecimalFilter::copy).orElse(null);
        this.returnItemPurpose = other.optionalReturnItemPurpose().map(StringFilter::copy).orElse(null);
        this.returnHeaderId = other.optionalReturnHeaderId().map(LongFilter::copy).orElse(null);
        this.returnTypeId = other.optionalReturnTypeId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReturnItemCriteria copy() {
        return new ReturnItemCriteria(this);
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

    public BigDecimalFilter getReturnQuantity() {
        return returnQuantity;
    }

    public Optional<BigDecimalFilter> optionalReturnQuantity() {
        return Optional.ofNullable(returnQuantity);
    }

    public BigDecimalFilter returnQuantity() {
        if (returnQuantity == null) {
            setReturnQuantity(new BigDecimalFilter());
        }
        return returnQuantity;
    }

    public void setReturnQuantity(BigDecimalFilter returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public BigDecimalFilter getReceivedQuantity() {
        return receivedQuantity;
    }

    public Optional<BigDecimalFilter> optionalReceivedQuantity() {
        return Optional.ofNullable(receivedQuantity);
    }

    public BigDecimalFilter receivedQuantity() {
        if (receivedQuantity == null) {
            setReceivedQuantity(new BigDecimalFilter());
        }
        return receivedQuantity;
    }

    public void setReceivedQuantity(BigDecimalFilter receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public BigDecimalFilter getReturnPrice() {
        return returnPrice;
    }

    public Optional<BigDecimalFilter> optionalReturnPrice() {
        return Optional.ofNullable(returnPrice);
    }

    public BigDecimalFilter returnPrice() {
        if (returnPrice == null) {
            setReturnPrice(new BigDecimalFilter());
        }
        return returnPrice;
    }

    public void setReturnPrice(BigDecimalFilter returnPrice) {
        this.returnPrice = returnPrice;
    }

    public StringFilter getReturnItemPurpose() {
        return returnItemPurpose;
    }

    public Optional<StringFilter> optionalReturnItemPurpose() {
        return Optional.ofNullable(returnItemPurpose);
    }

    public StringFilter returnItemPurpose() {
        if (returnItemPurpose == null) {
            setReturnItemPurpose(new StringFilter());
        }
        return returnItemPurpose;
    }

    public void setReturnItemPurpose(StringFilter returnItemPurpose) {
        this.returnItemPurpose = returnItemPurpose;
    }

    public LongFilter getReturnHeaderId() {
        return returnHeaderId;
    }

    public Optional<LongFilter> optionalReturnHeaderId() {
        return Optional.ofNullable(returnHeaderId);
    }

    public LongFilter returnHeaderId() {
        if (returnHeaderId == null) {
            setReturnHeaderId(new LongFilter());
        }
        return returnHeaderId;
    }

    public void setReturnHeaderId(LongFilter returnHeaderId) {
        this.returnHeaderId = returnHeaderId;
    }

    public LongFilter getReturnTypeId() {
        return returnTypeId;
    }

    public Optional<LongFilter> optionalReturnTypeId() {
        return Optional.ofNullable(returnTypeId);
    }

    public LongFilter returnTypeId() {
        if (returnTypeId == null) {
            setReturnTypeId(new LongFilter());
        }
        return returnTypeId;
    }

    public void setReturnTypeId(LongFilter returnTypeId) {
        this.returnTypeId = returnTypeId;
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

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public Optional<LongFilter> optionalOrderItemId() {
        return Optional.ofNullable(orderItemId);
    }

    public LongFilter orderItemId() {
        if (orderItemId == null) {
            setOrderItemId(new LongFilter());
        }
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
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
        final ReturnItemCriteria that = (ReturnItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(returnQuantity, that.returnQuantity) &&
            Objects.equals(receivedQuantity, that.receivedQuantity) &&
            Objects.equals(returnPrice, that.returnPrice) &&
            Objects.equals(returnItemPurpose, that.returnItemPurpose) &&
            Objects.equals(returnHeaderId, that.returnHeaderId) &&
            Objects.equals(returnTypeId, that.returnTypeId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNo,
            description,
            returnQuantity,
            receivedQuantity,
            returnPrice,
            returnItemPurpose,
            returnHeaderId,
            returnTypeId,
            productId,
            orderItemId,
            statusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReturnItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalReturnQuantity().map(f -> "returnQuantity=" + f + ", ").orElse("") +
            optionalReceivedQuantity().map(f -> "receivedQuantity=" + f + ", ").orElse("") +
            optionalReturnPrice().map(f -> "returnPrice=" + f + ", ").orElse("") +
            optionalReturnItemPurpose().map(f -> "returnItemPurpose=" + f + ", ").orElse("") +
            optionalReturnHeaderId().map(f -> "returnHeaderId=" + f + ", ").orElse("") +
            optionalReturnTypeId().map(f -> "returnTypeId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
