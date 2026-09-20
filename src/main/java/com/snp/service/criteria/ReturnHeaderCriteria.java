package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ReturnHeader} entity. This class is used
 * in {@link com.snp.web.rest.ReturnHeaderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /return-headers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReturnHeaderCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter needsInventoryReceive;

    private StringFilter supplierRmaId;

    private LongFilter returnTypeId;

    private LongFilter statusId;

    private LongFilter fromPartyId;

    private LongFilter toPartyId;

    private LongFilter paymentMethodId;

    private LongFilter originContactMechId;

    private LongFilter destinationFacilityId;

    private LongFilter currencyUomId;

    private Boolean distinct;

    public ReturnHeaderCriteria() {}

    public ReturnHeaderCriteria(ReturnHeaderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.needsInventoryReceive = other.optionalNeedsInventoryReceive().map(BooleanFilter::copy).orElse(null);
        this.supplierRmaId = other.optionalSupplierRmaId().map(StringFilter::copy).orElse(null);
        this.returnTypeId = other.optionalReturnTypeId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.fromPartyId = other.optionalFromPartyId().map(LongFilter::copy).orElse(null);
        this.toPartyId = other.optionalToPartyId().map(LongFilter::copy).orElse(null);
        this.paymentMethodId = other.optionalPaymentMethodId().map(LongFilter::copy).orElse(null);
        this.originContactMechId = other.optionalOriginContactMechId().map(LongFilter::copy).orElse(null);
        this.destinationFacilityId = other.optionalDestinationFacilityId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReturnHeaderCriteria copy() {
        return new ReturnHeaderCriteria(this);
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

    public BooleanFilter getNeedsInventoryReceive() {
        return needsInventoryReceive;
    }

    public Optional<BooleanFilter> optionalNeedsInventoryReceive() {
        return Optional.ofNullable(needsInventoryReceive);
    }

    public BooleanFilter needsInventoryReceive() {
        if (needsInventoryReceive == null) {
            setNeedsInventoryReceive(new BooleanFilter());
        }
        return needsInventoryReceive;
    }

    public void setNeedsInventoryReceive(BooleanFilter needsInventoryReceive) {
        this.needsInventoryReceive = needsInventoryReceive;
    }

    public StringFilter getSupplierRmaId() {
        return supplierRmaId;
    }

    public Optional<StringFilter> optionalSupplierRmaId() {
        return Optional.ofNullable(supplierRmaId);
    }

    public StringFilter supplierRmaId() {
        if (supplierRmaId == null) {
            setSupplierRmaId(new StringFilter());
        }
        return supplierRmaId;
    }

    public void setSupplierRmaId(StringFilter supplierRmaId) {
        this.supplierRmaId = supplierRmaId;
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

    public LongFilter getFromPartyId() {
        return fromPartyId;
    }

    public Optional<LongFilter> optionalFromPartyId() {
        return Optional.ofNullable(fromPartyId);
    }

    public LongFilter fromPartyId() {
        if (fromPartyId == null) {
            setFromPartyId(new LongFilter());
        }
        return fromPartyId;
    }

    public void setFromPartyId(LongFilter fromPartyId) {
        this.fromPartyId = fromPartyId;
    }

    public LongFilter getToPartyId() {
        return toPartyId;
    }

    public Optional<LongFilter> optionalToPartyId() {
        return Optional.ofNullable(toPartyId);
    }

    public LongFilter toPartyId() {
        if (toPartyId == null) {
            setToPartyId(new LongFilter());
        }
        return toPartyId;
    }

    public void setToPartyId(LongFilter toPartyId) {
        this.toPartyId = toPartyId;
    }

    public LongFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public Optional<LongFilter> optionalPaymentMethodId() {
        return Optional.ofNullable(paymentMethodId);
    }

    public LongFilter paymentMethodId() {
        if (paymentMethodId == null) {
            setPaymentMethodId(new LongFilter());
        }
        return paymentMethodId;
    }

    public void setPaymentMethodId(LongFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public LongFilter getOriginContactMechId() {
        return originContactMechId;
    }

    public Optional<LongFilter> optionalOriginContactMechId() {
        return Optional.ofNullable(originContactMechId);
    }

    public LongFilter originContactMechId() {
        if (originContactMechId == null) {
            setOriginContactMechId(new LongFilter());
        }
        return originContactMechId;
    }

    public void setOriginContactMechId(LongFilter originContactMechId) {
        this.originContactMechId = originContactMechId;
    }

    public LongFilter getDestinationFacilityId() {
        return destinationFacilityId;
    }

    public Optional<LongFilter> optionalDestinationFacilityId() {
        return Optional.ofNullable(destinationFacilityId);
    }

    public LongFilter destinationFacilityId() {
        if (destinationFacilityId == null) {
            setDestinationFacilityId(new LongFilter());
        }
        return destinationFacilityId;
    }

    public void setDestinationFacilityId(LongFilter destinationFacilityId) {
        this.destinationFacilityId = destinationFacilityId;
    }

    public LongFilter getCurrencyUomId() {
        return currencyUomId;
    }

    public Optional<LongFilter> optionalCurrencyUomId() {
        return Optional.ofNullable(currencyUomId);
    }

    public LongFilter currencyUomId() {
        if (currencyUomId == null) {
            setCurrencyUomId(new LongFilter());
        }
        return currencyUomId;
    }

    public void setCurrencyUomId(LongFilter currencyUomId) {
        this.currencyUomId = currencyUomId;
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
        final ReturnHeaderCriteria that = (ReturnHeaderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(needsInventoryReceive, that.needsInventoryReceive) &&
            Objects.equals(supplierRmaId, that.supplierRmaId) &&
            Objects.equals(returnTypeId, that.returnTypeId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(fromPartyId, that.fromPartyId) &&
            Objects.equals(toPartyId, that.toPartyId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(originContactMechId, that.originContactMechId) &&
            Objects.equals(destinationFacilityId, that.destinationFacilityId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            needsInventoryReceive,
            supplierRmaId,
            returnTypeId,
            statusId,
            fromPartyId,
            toPartyId,
            paymentMethodId,
            originContactMechId,
            destinationFacilityId,
            currencyUomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReturnHeaderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNeedsInventoryReceive().map(f -> "needsInventoryReceive=" + f + ", ").orElse("") +
            optionalSupplierRmaId().map(f -> "supplierRmaId=" + f + ", ").orElse("") +
            optionalReturnTypeId().map(f -> "returnTypeId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalFromPartyId().map(f -> "fromPartyId=" + f + ", ").orElse("") +
            optionalToPartyId().map(f -> "toPartyId=" + f + ", ").orElse("") +
            optionalPaymentMethodId().map(f -> "paymentMethodId=" + f + ", ").orElse("") +
            optionalOriginContactMechId().map(f -> "originContactMechId=" + f + ", ").orElse("") +
            optionalDestinationFacilityId().map(f -> "destinationFacilityId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
