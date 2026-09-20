package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 60)
    @Column(name = "payment_ref_number", length = 60)
    private String paymentRefNumber;

    @Column(name = "effective_date")
    private Instant effectiveDate;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "actual_amount", precision = 21, scale = 2)
    private BigDecimal actualAmount;

    @Size(max = 60)
    @Column(name = "payment_status", length = 60)
    private String paymentStatus;

    @Size(max = 60)
    @Column(name = "mihpay_id", length = 60)
    private String mihpayId;

    @Size(max = 60)
    @Column(name = "txn_id", length = 60)
    private String txnId;

    @Size(min = 5, max = 75)
    @Column(name = "email", length = 75)
    private String email;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 255)
    @Column(name = "product_info", length = 255)
    private String productInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethodType paymentMethodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "paymentMethodType", "party" }, allowSetters = true)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "paymentGatewayConfig", "paymentMethodType" }, allowSetters = true)
    private PaymentGatewayResponse paymentGatewayResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party fromParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party toParty;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentRefNumber() {
        return this.paymentRefNumber;
    }

    public Payment paymentRefNumber(String paymentRefNumber) {
        this.setPaymentRefNumber(paymentRefNumber);
        return this;
    }

    public void setPaymentRefNumber(String paymentRefNumber) {
        this.paymentRefNumber = paymentRefNumber;
    }

    public Instant getEffectiveDate() {
        return this.effectiveDate;
    }

    public Payment effectiveDate(Instant effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(Instant effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getActualAmount() {
        return this.actualAmount;
    }

    public Payment actualAmount(BigDecimal actualAmount) {
        this.setActualAmount(actualAmount);
        return this;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public Payment paymentStatus(String paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getMihpayId() {
        return this.mihpayId;
    }

    public Payment mihpayId(String mihpayId) {
        this.setMihpayId(mihpayId);
        return this;
    }

    public void setMihpayId(String mihpayId) {
        this.mihpayId = mihpayId;
    }

    public String getTxnId() {
        return this.txnId;
    }

    public Payment txnId(String txnId) {
        this.setTxnId(txnId);
        return this;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getEmail() {
        return this.email;
    }

    public Payment email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Payment phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProductInfo() {
        return this.productInfo;
    }

    public Payment productInfo(String productInfo) {
        this.setProductInfo(productInfo);
        return this;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Payment paymentType(PaymentType paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public PaymentMethodType getPaymentMethodType() {
        return this.paymentMethodType;
    }

    public void setPaymentMethodType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public Payment paymentMethodType(PaymentMethodType paymentMethodType) {
        this.setPaymentMethodType(paymentMethodType);
        return this;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Payment paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public PaymentGatewayResponse getPaymentGatewayResponse() {
        return this.paymentGatewayResponse;
    }

    public void setPaymentGatewayResponse(PaymentGatewayResponse paymentGatewayResponse) {
        this.paymentGatewayResponse = paymentGatewayResponse;
    }

    public Payment paymentGatewayResponse(PaymentGatewayResponse paymentGatewayResponse) {
        this.setPaymentGatewayResponse(paymentGatewayResponse);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Party getFromParty() {
        return this.fromParty;
    }

    public void setFromParty(Party party) {
        this.fromParty = party;
    }

    public Payment fromParty(Party party) {
        this.setFromParty(party);
        return this;
    }

    public Party getToParty() {
        return this.toParty;
    }

    public void setToParty(Party party) {
        this.toParty = party;
    }

    public Payment toParty(Party party) {
        this.setToParty(party);
        return this;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Payment roleType(RoleType roleType) {
        this.setRoleType(roleType);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public Payment currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentRefNumber='" + getPaymentRefNumber() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amount=" + getAmount() +
            ", actualAmount=" + getActualAmount() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", mihpayId='" + getMihpayId() + "'" +
            ", txnId='" + getTxnId() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", productInfo='" + getProductInfo() + "'" +
            "}";
    }
}
