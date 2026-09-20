package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentApplication.
 */
@Entity
@Table(name = "payment_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentApplication implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount_applied", precision = 21, scale = 2, nullable = false)
    private BigDecimal amountApplied;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "paymentType",
            "paymentMethodType",
            "paymentMethod",
            "paymentGatewayResponse",
            "status",
            "fromParty",
            "toParty",
            "roleType",
            "currencyUom",
        },
        allowSetters = true
    )
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "paymentType",
            "paymentMethodType",
            "paymentMethod",
            "paymentGatewayResponse",
            "status",
            "fromParty",
            "toParty",
            "roleType",
            "currencyUom",
        },
        allowSetters = true
    )
    private Payment toPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "invoiceType", "order", "fromParty", "toParty", "roleType", "status", "contactMech", "currencyUom" },
        allowSetters = true
    )
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "invoice",
            "invoiceItemType",
            "product",
            "uom",
            "productFeature",
            "productFacility",
            "fromInventoryItem",
            "taxAuthRate",
            "parentInvoice",
            "parentInvoiceItem",
        },
        allowSetters = true
    )
    private InvoiceItem invoiceItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "orderType",
            "salesChannel",
            "party",
            "supplier",
            "status",
            "currencyUom",
            "currencyConversion",
            "quote",
            "facility",
            "productStore",
            "department",
            "customTimePeriod",
        },
        allowSetters = true
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "order",
            "orderItemType",
            "product",
            "uom",
            "supplierProduct",
            "supplier",
            "productFeature",
            "productFacility",
            "fromInventoryItem",
            "shoppingListItem",
            "quoteItem",
            "status",
            "taxAuthRate",
        },
        allowSetters = true
    )
    private OrderItem orderItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmountApplied() {
        return this.amountApplied;
    }

    public PaymentApplication amountApplied(BigDecimal amountApplied) {
        this.setAmountApplied(amountApplied);
        return this;
    }

    public void setAmountApplied(BigDecimal amountApplied) {
        this.amountApplied = amountApplied;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentApplication payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public Payment getToPayment() {
        return this.toPayment;
    }

    public void setToPayment(Payment payment) {
        this.toPayment = payment;
    }

    public PaymentApplication toPayment(Payment payment) {
        this.setToPayment(payment);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public PaymentApplication invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public InvoiceItem getInvoiceItem() {
        return this.invoiceItem;
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public PaymentApplication invoiceItem(InvoiceItem invoiceItem) {
        this.setInvoiceItem(invoiceItem);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentApplication order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public PaymentApplication orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentApplication)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentApplication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentApplication{" +
            "id=" + getId() +
            ", amountApplied=" + getAmountApplied() +
            "}";
    }
}
