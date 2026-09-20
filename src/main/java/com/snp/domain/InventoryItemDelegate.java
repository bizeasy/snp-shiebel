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
 * A InventoryItemDelegate.
 */
@Entity
@Table(name = "inventory_item_delegate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItemDelegate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private Instant effectiveDate;

    @Column(name = "quantity_on_hand_diff", precision = 21, scale = 2)
    private BigDecimal quantityOnHandDiff;

    @Column(name = "available_to_promise_diff", precision = 21, scale = 2)
    private BigDecimal availableToPromiseDiff;

    @Column(name = "accounting_quantity_diff", precision = 21, scale = 2)
    private BigDecimal accountingQuantityDiff;

    @Column(name = "unit_cost", precision = 21, scale = 2)
    private BigDecimal unitCost;

    @Size(max = 255)
    @Column(name = "remarks", length = 255)
    private String remarks;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "inventoryItemType",
            "product",
            "supplier",
            "ownerParty",
            "status",
            "facility",
            "facilityLocation",
            "uom",
            "currencyUom",
            "lot",
        },
        allowSetters = true
    )
    private InventoryItem inventoryItem;

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
            "shipmentType",
            "shipmentMethodType",
            "status",
            "order",
            "orderItem",
            "originFacility",
            "destinationFacility",
            "fromParty",
            "toParty",
            "carrierParty",
            "contactMech",
        },
        allowSetters = true
    )
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "shipment", "product" }, allowSetters = true)
    private ShipmentItem shipmentItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "inventoryItem", "order", "orderItem", "shipment", "shipmentItem", "issuedByParty" },
        allowSetters = true
    )
    private ItemIssuance itemIssuance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "shipment", "shipmentItem", "order", "orderItem", "inventoryItem", "product", "receivedBy" },
        allowSetters = true
    )
    private ShipmentReceipt receipt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InventoryItemDelegate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public InventoryItemDelegate sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Instant getEffectiveDate() {
        return this.effectiveDate;
    }

    public InventoryItemDelegate effectiveDate(Instant effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(Instant effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getQuantityOnHandDiff() {
        return this.quantityOnHandDiff;
    }

    public InventoryItemDelegate quantityOnHandDiff(BigDecimal quantityOnHandDiff) {
        this.setQuantityOnHandDiff(quantityOnHandDiff);
        return this;
    }

    public void setQuantityOnHandDiff(BigDecimal quantityOnHandDiff) {
        this.quantityOnHandDiff = quantityOnHandDiff;
    }

    public BigDecimal getAvailableToPromiseDiff() {
        return this.availableToPromiseDiff;
    }

    public InventoryItemDelegate availableToPromiseDiff(BigDecimal availableToPromiseDiff) {
        this.setAvailableToPromiseDiff(availableToPromiseDiff);
        return this;
    }

    public void setAvailableToPromiseDiff(BigDecimal availableToPromiseDiff) {
        this.availableToPromiseDiff = availableToPromiseDiff;
    }

    public BigDecimal getAccountingQuantityDiff() {
        return this.accountingQuantityDiff;
    }

    public InventoryItemDelegate accountingQuantityDiff(BigDecimal accountingQuantityDiff) {
        this.setAccountingQuantityDiff(accountingQuantityDiff);
        return this;
    }

    public void setAccountingQuantityDiff(BigDecimal accountingQuantityDiff) {
        this.accountingQuantityDiff = accountingQuantityDiff;
    }

    public BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public InventoryItemDelegate unitCost(BigDecimal unitCost) {
        this.setUnitCost(unitCost);
        return this;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public InventoryItemDelegate remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public InventoryItem getInventoryItem() {
        return this.inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public InventoryItemDelegate inventoryItem(InventoryItem inventoryItem) {
        this.setInventoryItem(inventoryItem);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public InventoryItemDelegate order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public InventoryItemDelegate orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InventoryItemDelegate invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public InvoiceItem getInvoiceItem() {
        return this.invoiceItem;
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public InventoryItemDelegate invoiceItem(InvoiceItem invoiceItem) {
        this.setInvoiceItem(invoiceItem);
        return this;
    }

    public Shipment getShipment() {
        return this.shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public InventoryItemDelegate shipment(Shipment shipment) {
        this.setShipment(shipment);
        return this;
    }

    public ShipmentItem getShipmentItem() {
        return this.shipmentItem;
    }

    public void setShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
    }

    public InventoryItemDelegate shipmentItem(ShipmentItem shipmentItem) {
        this.setShipmentItem(shipmentItem);
        return this;
    }

    public ItemIssuance getItemIssuance() {
        return this.itemIssuance;
    }

    public void setItemIssuance(ItemIssuance itemIssuance) {
        this.itemIssuance = itemIssuance;
    }

    public InventoryItemDelegate itemIssuance(ItemIssuance itemIssuance) {
        this.setItemIssuance(itemIssuance);
        return this;
    }

    public ShipmentReceipt getReceipt() {
        return this.receipt;
    }

    public void setReceipt(ShipmentReceipt shipmentReceipt) {
        this.receipt = shipmentReceipt;
    }

    public InventoryItemDelegate receipt(ShipmentReceipt shipmentReceipt) {
        this.setReceipt(shipmentReceipt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryItemDelegate)) {
            return false;
        }
        return getId() != null && getId().equals(((InventoryItemDelegate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItemDelegate{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", quantityOnHandDiff=" + getQuantityOnHandDiff() +
            ", availableToPromiseDiff=" + getAvailableToPromiseDiff() +
            ", accountingQuantityDiff=" + getAccountingQuantityDiff() +
            ", unitCost=" + getUnitCost() +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
