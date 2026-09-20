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
 * A OrderItemBilling.
 */
@Entity
@Table(name = "order_item_billing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItemBilling implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @NotNull
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

    @ManyToOne(optional = false)
    @NotNull
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
        value = { "shipment", "shipmentItem", "order", "orderItem", "inventoryItem", "product", "receivedBy" },
        allowSetters = true
    )
    private ShipmentReceipt shipmentReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "inventoryItem", "order", "orderItem", "shipment", "shipmentItem", "issuedByParty" },
        allowSetters = true
    )
    private ItemIssuance itemIssuance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItemBilling id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public OrderItemBilling quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public OrderItemBilling amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public OrderItemBilling orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public InvoiceItem getInvoiceItem() {
        return this.invoiceItem;
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public OrderItemBilling invoiceItem(InvoiceItem invoiceItem) {
        this.setInvoiceItem(invoiceItem);
        return this;
    }

    public ShipmentReceipt getShipmentReceipt() {
        return this.shipmentReceipt;
    }

    public void setShipmentReceipt(ShipmentReceipt shipmentReceipt) {
        this.shipmentReceipt = shipmentReceipt;
    }

    public OrderItemBilling shipmentReceipt(ShipmentReceipt shipmentReceipt) {
        this.setShipmentReceipt(shipmentReceipt);
        return this;
    }

    public ItemIssuance getItemIssuance() {
        return this.itemIssuance;
    }

    public void setItemIssuance(ItemIssuance itemIssuance) {
        this.itemIssuance = itemIssuance;
    }

    public OrderItemBilling itemIssuance(ItemIssuance itemIssuance) {
        this.setItemIssuance(itemIssuance);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemBilling)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderItemBilling) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemBilling{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", amount=" + getAmount() +
            "}";
    }
}
