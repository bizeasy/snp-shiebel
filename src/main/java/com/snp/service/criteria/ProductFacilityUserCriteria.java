package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductFacilityUser} entity. This class is used
 * in {@link com.snp.web.rest.ProductFacilityUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-facility-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFacilityUserCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private BigDecimalFilter costPerDay;

    private BigDecimalFilter totalCost;

    private BigDecimalFilter discount;

    private LongFilter productFacilityId;

    private LongFilter partyId;

    private LongFilter statusId;

    private Boolean distinct;

    public ProductFacilityUserCriteria() {}

    public ProductFacilityUserCriteria(ProductFacilityUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.costPerDay = other.optionalCostPerDay().map(BigDecimalFilter::copy).orElse(null);
        this.totalCost = other.optionalTotalCost().map(BigDecimalFilter::copy).orElse(null);
        this.discount = other.optionalDiscount().map(BigDecimalFilter::copy).orElse(null);
        this.productFacilityId = other.optionalProductFacilityId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductFacilityUserCriteria copy() {
        return new ProductFacilityUserCriteria(this);
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

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public Optional<InstantFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new InstantFilter());
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getThruDate() {
        return thruDate;
    }

    public Optional<InstantFilter> optionalThruDate() {
        return Optional.ofNullable(thruDate);
    }

    public InstantFilter thruDate() {
        if (thruDate == null) {
            setThruDate(new InstantFilter());
        }
        return thruDate;
    }

    public void setThruDate(InstantFilter thruDate) {
        this.thruDate = thruDate;
    }

    public BigDecimalFilter getCostPerDay() {
        return costPerDay;
    }

    public Optional<BigDecimalFilter> optionalCostPerDay() {
        return Optional.ofNullable(costPerDay);
    }

    public BigDecimalFilter costPerDay() {
        if (costPerDay == null) {
            setCostPerDay(new BigDecimalFilter());
        }
        return costPerDay;
    }

    public void setCostPerDay(BigDecimalFilter costPerDay) {
        this.costPerDay = costPerDay;
    }

    public BigDecimalFilter getTotalCost() {
        return totalCost;
    }

    public Optional<BigDecimalFilter> optionalTotalCost() {
        return Optional.ofNullable(totalCost);
    }

    public BigDecimalFilter totalCost() {
        if (totalCost == null) {
            setTotalCost(new BigDecimalFilter());
        }
        return totalCost;
    }

    public void setTotalCost(BigDecimalFilter totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimalFilter getDiscount() {
        return discount;
    }

    public Optional<BigDecimalFilter> optionalDiscount() {
        return Optional.ofNullable(discount);
    }

    public BigDecimalFilter discount() {
        if (discount == null) {
            setDiscount(new BigDecimalFilter());
        }
        return discount;
    }

    public void setDiscount(BigDecimalFilter discount) {
        this.discount = discount;
    }

    public LongFilter getProductFacilityId() {
        return productFacilityId;
    }

    public Optional<LongFilter> optionalProductFacilityId() {
        return Optional.ofNullable(productFacilityId);
    }

    public LongFilter productFacilityId() {
        if (productFacilityId == null) {
            setProductFacilityId(new LongFilter());
        }
        return productFacilityId;
    }

    public void setProductFacilityId(LongFilter productFacilityId) {
        this.productFacilityId = productFacilityId;
    }

    public LongFilter getPartyId() {
        return partyId;
    }

    public Optional<LongFilter> optionalPartyId() {
        return Optional.ofNullable(partyId);
    }

    public LongFilter partyId() {
        if (partyId == null) {
            setPartyId(new LongFilter());
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
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
        final ProductFacilityUserCriteria that = (ProductFacilityUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(costPerDay, that.costPerDay) &&
            Objects.equals(totalCost, that.totalCost) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(productFacilityId, that.productFacilityId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, thruDate, costPerDay, totalCost, discount, productFacilityId, partyId, statusId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFacilityUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalCostPerDay().map(f -> "costPerDay=" + f + ", ").orElse("") +
            optionalTotalCost().map(f -> "totalCost=" + f + ", ").orElse("") +
            optionalDiscount().map(f -> "discount=" + f + ", ").orElse("") +
            optionalProductFacilityId().map(f -> "productFacilityId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
