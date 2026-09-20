package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.OrderContactMech} entity. This class is used
 * in {@link com.snp.web.rest.OrderContactMechResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-contact-meches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderContactMechCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private LongFilter orderId;

    private LongFilter contactMechId;

    private LongFilter contactMechPurposeId;

    private Boolean distinct;

    public OrderContactMechCriteria() {}

    public OrderContactMechCriteria(OrderContactMechCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.contactMechId = other.optionalContactMechId().map(LongFilter::copy).orElse(null);
        this.contactMechPurposeId = other.optionalContactMechPurposeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrderContactMechCriteria copy() {
        return new OrderContactMechCriteria(this);
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

    public LongFilter getOrderId() {
        return orderId;
    }

    public Optional<LongFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public LongFilter orderId() {
        if (orderId == null) {
            setOrderId(new LongFilter());
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getContactMechId() {
        return contactMechId;
    }

    public Optional<LongFilter> optionalContactMechId() {
        return Optional.ofNullable(contactMechId);
    }

    public LongFilter contactMechId() {
        if (contactMechId == null) {
            setContactMechId(new LongFilter());
        }
        return contactMechId;
    }

    public void setContactMechId(LongFilter contactMechId) {
        this.contactMechId = contactMechId;
    }

    public LongFilter getContactMechPurposeId() {
        return contactMechPurposeId;
    }

    public Optional<LongFilter> optionalContactMechPurposeId() {
        return Optional.ofNullable(contactMechPurposeId);
    }

    public LongFilter contactMechPurposeId() {
        if (contactMechPurposeId == null) {
            setContactMechPurposeId(new LongFilter());
        }
        return contactMechPurposeId;
    }

    public void setContactMechPurposeId(LongFilter contactMechPurposeId) {
        this.contactMechPurposeId = contactMechPurposeId;
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
        final OrderContactMechCriteria that = (OrderContactMechCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(contactMechId, that.contactMechId) &&
            Objects.equals(contactMechPurposeId, that.contactMechPurposeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, thruDate, orderId, contactMechId, contactMechPurposeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderContactMechCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalContactMechId().map(f -> "contactMechId=" + f + ", ").orElse("") +
            optionalContactMechPurposeId().map(f -> "contactMechPurposeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
