package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.PaymentGatewayResponse} entity. This class is used
 * in {@link com.snp.web.rest.PaymentGatewayResponseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-gateway-responses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentGatewayResponseCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter referenceNumber;

    private BigDecimalFilter amount;

    private StringFilter gatewayMessage;

    private LongFilter paymentGatewayConfigId;

    private LongFilter paymentMethodTypeId;

    private Boolean distinct;

    public PaymentGatewayResponseCriteria() {}

    public PaymentGatewayResponseCriteria(PaymentGatewayResponseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.referenceNumber = other.optionalReferenceNumber().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.gatewayMessage = other.optionalGatewayMessage().map(StringFilter::copy).orElse(null);
        this.paymentGatewayConfigId = other.optionalPaymentGatewayConfigId().map(LongFilter::copy).orElse(null);
        this.paymentMethodTypeId = other.optionalPaymentMethodTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentGatewayResponseCriteria copy() {
        return new PaymentGatewayResponseCriteria(this);
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

    public StringFilter getReferenceNumber() {
        return referenceNumber;
    }

    public Optional<StringFilter> optionalReferenceNumber() {
        return Optional.ofNullable(referenceNumber);
    }

    public StringFilter referenceNumber() {
        if (referenceNumber == null) {
            setReferenceNumber(new StringFilter());
        }
        return referenceNumber;
    }

    public void setReferenceNumber(StringFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getGatewayMessage() {
        return gatewayMessage;
    }

    public Optional<StringFilter> optionalGatewayMessage() {
        return Optional.ofNullable(gatewayMessage);
    }

    public StringFilter gatewayMessage() {
        if (gatewayMessage == null) {
            setGatewayMessage(new StringFilter());
        }
        return gatewayMessage;
    }

    public void setGatewayMessage(StringFilter gatewayMessage) {
        this.gatewayMessage = gatewayMessage;
    }

    public LongFilter getPaymentGatewayConfigId() {
        return paymentGatewayConfigId;
    }

    public Optional<LongFilter> optionalPaymentGatewayConfigId() {
        return Optional.ofNullable(paymentGatewayConfigId);
    }

    public LongFilter paymentGatewayConfigId() {
        if (paymentGatewayConfigId == null) {
            setPaymentGatewayConfigId(new LongFilter());
        }
        return paymentGatewayConfigId;
    }

    public void setPaymentGatewayConfigId(LongFilter paymentGatewayConfigId) {
        this.paymentGatewayConfigId = paymentGatewayConfigId;
    }

    public LongFilter getPaymentMethodTypeId() {
        return paymentMethodTypeId;
    }

    public Optional<LongFilter> optionalPaymentMethodTypeId() {
        return Optional.ofNullable(paymentMethodTypeId);
    }

    public LongFilter paymentMethodTypeId() {
        if (paymentMethodTypeId == null) {
            setPaymentMethodTypeId(new LongFilter());
        }
        return paymentMethodTypeId;
    }

    public void setPaymentMethodTypeId(LongFilter paymentMethodTypeId) {
        this.paymentMethodTypeId = paymentMethodTypeId;
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
        final PaymentGatewayResponseCriteria that = (PaymentGatewayResponseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(gatewayMessage, that.gatewayMessage) &&
            Objects.equals(paymentGatewayConfigId, that.paymentGatewayConfigId) &&
            Objects.equals(paymentMethodTypeId, that.paymentMethodTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referenceNumber, amount, gatewayMessage, paymentGatewayConfigId, paymentMethodTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentGatewayResponseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalReferenceNumber().map(f -> "referenceNumber=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalGatewayMessage().map(f -> "gatewayMessage=" + f + ", ").orElse("") +
            optionalPaymentGatewayConfigId().map(f -> "paymentGatewayConfigId=" + f + ", ").orElse("") +
            optionalPaymentMethodTypeId().map(f -> "paymentMethodTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
