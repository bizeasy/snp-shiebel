package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.PaymentApplication} entity. This class is used
 * in {@link com.snp.web.rest.PaymentApplicationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-applications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentApplicationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter amountApplied;

    private LongFilter paymentId;

    private LongFilter toPaymentId;

    private LongFilter invoiceId;

    private LongFilter invoiceItemId;

    private LongFilter orderId;

    private LongFilter orderItemId;

    private Boolean distinct;

    public PaymentApplicationCriteria() {}

    public PaymentApplicationCriteria(PaymentApplicationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.amountApplied = other.optionalAmountApplied().map(BigDecimalFilter::copy).orElse(null);
        this.paymentId = other.optionalPaymentId().map(LongFilter::copy).orElse(null);
        this.toPaymentId = other.optionalToPaymentId().map(LongFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(LongFilter::copy).orElse(null);
        this.invoiceItemId = other.optionalInvoiceItemId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentApplicationCriteria copy() {
        return new PaymentApplicationCriteria(this);
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

    public BigDecimalFilter getAmountApplied() {
        return amountApplied;
    }

    public Optional<BigDecimalFilter> optionalAmountApplied() {
        return Optional.ofNullable(amountApplied);
    }

    public BigDecimalFilter amountApplied() {
        if (amountApplied == null) {
            setAmountApplied(new BigDecimalFilter());
        }
        return amountApplied;
    }

    public void setAmountApplied(BigDecimalFilter amountApplied) {
        this.amountApplied = amountApplied;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public Optional<LongFilter> optionalPaymentId() {
        return Optional.ofNullable(paymentId);
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            setPaymentId(new LongFilter());
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getToPaymentId() {
        return toPaymentId;
    }

    public Optional<LongFilter> optionalToPaymentId() {
        return Optional.ofNullable(toPaymentId);
    }

    public LongFilter toPaymentId() {
        if (toPaymentId == null) {
            setToPaymentId(new LongFilter());
        }
        return toPaymentId;
    }

    public void setToPaymentId(LongFilter toPaymentId) {
        this.toPaymentId = toPaymentId;
    }

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public Optional<LongFilter> optionalInvoiceId() {
        return Optional.ofNullable(invoiceId);
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            setInvoiceId(new LongFilter());
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LongFilter getInvoiceItemId() {
        return invoiceItemId;
    }

    public Optional<LongFilter> optionalInvoiceItemId() {
        return Optional.ofNullable(invoiceItemId);
    }

    public LongFilter invoiceItemId() {
        if (invoiceItemId == null) {
            setInvoiceItemId(new LongFilter());
        }
        return invoiceItemId;
    }

    public void setInvoiceItemId(LongFilter invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
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
        final PaymentApplicationCriteria that = (PaymentApplicationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amountApplied, that.amountApplied) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(toPaymentId, that.toPaymentId) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(invoiceItemId, that.invoiceItemId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountApplied, paymentId, toPaymentId, invoiceId, invoiceItemId, orderId, orderItemId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentApplicationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAmountApplied().map(f -> "amountApplied=" + f + ", ").orElse("") +
            optionalPaymentId().map(f -> "paymentId=" + f + ", ").orElse("") +
            optionalToPaymentId().map(f -> "toPaymentId=" + f + ", ").orElse("") +
            optionalInvoiceId().map(f -> "invoiceId=" + f + ", ").orElse("") +
            optionalInvoiceItemId().map(f -> "invoiceItemId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
