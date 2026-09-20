package com.snp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentGatewayResponse.
 */
@Entity
@Table(name = "payment_gateway_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentGatewayResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 25)
    @Column(name = "reference_number", length = 25)
    private String referenceNumber;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Size(max = 2000)
    @Column(name = "gateway_message", length = 2000)
    private String gatewayMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentGatewayConfig paymentGatewayConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethodType paymentMethodType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentGatewayResponse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public PaymentGatewayResponse referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public PaymentGatewayResponse amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getGatewayMessage() {
        return this.gatewayMessage;
    }

    public PaymentGatewayResponse gatewayMessage(String gatewayMessage) {
        this.setGatewayMessage(gatewayMessage);
        return this;
    }

    public void setGatewayMessage(String gatewayMessage) {
        this.gatewayMessage = gatewayMessage;
    }

    public PaymentGatewayConfig getPaymentGatewayConfig() {
        return this.paymentGatewayConfig;
    }

    public void setPaymentGatewayConfig(PaymentGatewayConfig paymentGatewayConfig) {
        this.paymentGatewayConfig = paymentGatewayConfig;
    }

    public PaymentGatewayResponse paymentGatewayConfig(PaymentGatewayConfig paymentGatewayConfig) {
        this.setPaymentGatewayConfig(paymentGatewayConfig);
        return this;
    }

    public PaymentMethodType getPaymentMethodType() {
        return this.paymentMethodType;
    }

    public void setPaymentMethodType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public PaymentGatewayResponse paymentMethodType(PaymentMethodType paymentMethodType) {
        this.setPaymentMethodType(paymentMethodType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentGatewayResponse)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentGatewayResponse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentGatewayResponse{" +
            "id=" + getId() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", amount=" + getAmount() +
            ", gatewayMessage='" + getGatewayMessage() + "'" +
            "}";
    }
}
