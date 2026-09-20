package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Payment} entity. This class is used
 * in {@link com.snp.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentRefNumber;

    private InstantFilter effectiveDate;

    private InstantFilter paymentDate;

    private BigDecimalFilter amount;

    private BigDecimalFilter actualAmount;

    private StringFilter paymentStatus;

    private StringFilter mihpayId;

    private StringFilter txnId;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter productInfo;

    private LongFilter paymentTypeId;

    private LongFilter paymentMethodTypeId;

    private LongFilter paymentMethodId;

    private LongFilter paymentGatewayResponseId;

    private LongFilter statusId;

    private LongFilter fromPartyId;

    private LongFilter toPartyId;

    private LongFilter roleTypeId;

    private LongFilter currencyUomId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.paymentRefNumber = other.optionalPaymentRefNumber().map(StringFilter::copy).orElse(null);
        this.effectiveDate = other.optionalEffectiveDate().map(InstantFilter::copy).orElse(null);
        this.paymentDate = other.optionalPaymentDate().map(InstantFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.actualAmount = other.optionalActualAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentStatus = other.optionalPaymentStatus().map(StringFilter::copy).orElse(null);
        this.mihpayId = other.optionalMihpayId().map(StringFilter::copy).orElse(null);
        this.txnId = other.optionalTxnId().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.productInfo = other.optionalProductInfo().map(StringFilter::copy).orElse(null);
        this.paymentTypeId = other.optionalPaymentTypeId().map(LongFilter::copy).orElse(null);
        this.paymentMethodTypeId = other.optionalPaymentMethodTypeId().map(LongFilter::copy).orElse(null);
        this.paymentMethodId = other.optionalPaymentMethodId().map(LongFilter::copy).orElse(null);
        this.paymentGatewayResponseId = other.optionalPaymentGatewayResponseId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.fromPartyId = other.optionalFromPartyId().map(LongFilter::copy).orElse(null);
        this.toPartyId = other.optionalToPartyId().map(LongFilter::copy).orElse(null);
        this.roleTypeId = other.optionalRoleTypeId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public StringFilter getPaymentRefNumber() {
        return paymentRefNumber;
    }

    public Optional<StringFilter> optionalPaymentRefNumber() {
        return Optional.ofNullable(paymentRefNumber);
    }

    public StringFilter paymentRefNumber() {
        if (paymentRefNumber == null) {
            setPaymentRefNumber(new StringFilter());
        }
        return paymentRefNumber;
    }

    public void setPaymentRefNumber(StringFilter paymentRefNumber) {
        this.paymentRefNumber = paymentRefNumber;
    }

    public InstantFilter getEffectiveDate() {
        return effectiveDate;
    }

    public Optional<InstantFilter> optionalEffectiveDate() {
        return Optional.ofNullable(effectiveDate);
    }

    public InstantFilter effectiveDate() {
        if (effectiveDate == null) {
            setEffectiveDate(new InstantFilter());
        }
        return effectiveDate;
    }

    public void setEffectiveDate(InstantFilter effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public InstantFilter getPaymentDate() {
        return paymentDate;
    }

    public Optional<InstantFilter> optionalPaymentDate() {
        return Optional.ofNullable(paymentDate);
    }

    public InstantFilter paymentDate() {
        if (paymentDate == null) {
            setPaymentDate(new InstantFilter());
        }
        return paymentDate;
    }

    public void setPaymentDate(InstantFilter paymentDate) {
        this.paymentDate = paymentDate;
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

    public BigDecimalFilter getActualAmount() {
        return actualAmount;
    }

    public Optional<BigDecimalFilter> optionalActualAmount() {
        return Optional.ofNullable(actualAmount);
    }

    public BigDecimalFilter actualAmount() {
        if (actualAmount == null) {
            setActualAmount(new BigDecimalFilter());
        }
        return actualAmount;
    }

    public void setActualAmount(BigDecimalFilter actualAmount) {
        this.actualAmount = actualAmount;
    }

    public StringFilter getPaymentStatus() {
        return paymentStatus;
    }

    public Optional<StringFilter> optionalPaymentStatus() {
        return Optional.ofNullable(paymentStatus);
    }

    public StringFilter paymentStatus() {
        if (paymentStatus == null) {
            setPaymentStatus(new StringFilter());
        }
        return paymentStatus;
    }

    public void setPaymentStatus(StringFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public StringFilter getMihpayId() {
        return mihpayId;
    }

    public Optional<StringFilter> optionalMihpayId() {
        return Optional.ofNullable(mihpayId);
    }

    public StringFilter mihpayId() {
        if (mihpayId == null) {
            setMihpayId(new StringFilter());
        }
        return mihpayId;
    }

    public void setMihpayId(StringFilter mihpayId) {
        this.mihpayId = mihpayId;
    }

    public StringFilter getTxnId() {
        return txnId;
    }

    public Optional<StringFilter> optionalTxnId() {
        return Optional.ofNullable(txnId);
    }

    public StringFilter txnId() {
        if (txnId == null) {
            setTxnId(new StringFilter());
        }
        return txnId;
    }

    public void setTxnId(StringFilter txnId) {
        this.txnId = txnId;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getProductInfo() {
        return productInfo;
    }

    public Optional<StringFilter> optionalProductInfo() {
        return Optional.ofNullable(productInfo);
    }

    public StringFilter productInfo() {
        if (productInfo == null) {
            setProductInfo(new StringFilter());
        }
        return productInfo;
    }

    public void setProductInfo(StringFilter productInfo) {
        this.productInfo = productInfo;
    }

    public LongFilter getPaymentTypeId() {
        return paymentTypeId;
    }

    public Optional<LongFilter> optionalPaymentTypeId() {
        return Optional.ofNullable(paymentTypeId);
    }

    public LongFilter paymentTypeId() {
        if (paymentTypeId == null) {
            setPaymentTypeId(new LongFilter());
        }
        return paymentTypeId;
    }

    public void setPaymentTypeId(LongFilter paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
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

    public LongFilter getPaymentGatewayResponseId() {
        return paymentGatewayResponseId;
    }

    public Optional<LongFilter> optionalPaymentGatewayResponseId() {
        return Optional.ofNullable(paymentGatewayResponseId);
    }

    public LongFilter paymentGatewayResponseId() {
        if (paymentGatewayResponseId == null) {
            setPaymentGatewayResponseId(new LongFilter());
        }
        return paymentGatewayResponseId;
    }

    public void setPaymentGatewayResponseId(LongFilter paymentGatewayResponseId) {
        this.paymentGatewayResponseId = paymentGatewayResponseId;
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

    public LongFilter getRoleTypeId() {
        return roleTypeId;
    }

    public Optional<LongFilter> optionalRoleTypeId() {
        return Optional.ofNullable(roleTypeId);
    }

    public LongFilter roleTypeId() {
        if (roleTypeId == null) {
            setRoleTypeId(new LongFilter());
        }
        return roleTypeId;
    }

    public void setRoleTypeId(LongFilter roleTypeId) {
        this.roleTypeId = roleTypeId;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentRefNumber, that.paymentRefNumber) &&
            Objects.equals(effectiveDate, that.effectiveDate) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(actualAmount, that.actualAmount) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(mihpayId, that.mihpayId) &&
            Objects.equals(txnId, that.txnId) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(productInfo, that.productInfo) &&
            Objects.equals(paymentTypeId, that.paymentTypeId) &&
            Objects.equals(paymentMethodTypeId, that.paymentMethodTypeId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(paymentGatewayResponseId, that.paymentGatewayResponseId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(fromPartyId, that.fromPartyId) &&
            Objects.equals(toPartyId, that.toPartyId) &&
            Objects.equals(roleTypeId, that.roleTypeId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentRefNumber,
            effectiveDate,
            paymentDate,
            amount,
            actualAmount,
            paymentStatus,
            mihpayId,
            txnId,
            email,
            phone,
            productInfo,
            paymentTypeId,
            paymentMethodTypeId,
            paymentMethodId,
            paymentGatewayResponseId,
            statusId,
            fromPartyId,
            toPartyId,
            roleTypeId,
            currencyUomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPaymentRefNumber().map(f -> "paymentRefNumber=" + f + ", ").orElse("") +
            optionalEffectiveDate().map(f -> "effectiveDate=" + f + ", ").orElse("") +
            optionalPaymentDate().map(f -> "paymentDate=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalActualAmount().map(f -> "actualAmount=" + f + ", ").orElse("") +
            optionalPaymentStatus().map(f -> "paymentStatus=" + f + ", ").orElse("") +
            optionalMihpayId().map(f -> "mihpayId=" + f + ", ").orElse("") +
            optionalTxnId().map(f -> "txnId=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalProductInfo().map(f -> "productInfo=" + f + ", ").orElse("") +
            optionalPaymentTypeId().map(f -> "paymentTypeId=" + f + ", ").orElse("") +
            optionalPaymentMethodTypeId().map(f -> "paymentMethodTypeId=" + f + ", ").orElse("") +
            optionalPaymentMethodId().map(f -> "paymentMethodId=" + f + ", ").orElse("") +
            optionalPaymentGatewayResponseId().map(f -> "paymentGatewayResponseId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalFromPartyId().map(f -> "fromPartyId=" + f + ", ").orElse("") +
            optionalToPartyId().map(f -> "toPartyId=" + f + ", ").orElse("") +
            optionalRoleTypeId().map(f -> "roleTypeId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
