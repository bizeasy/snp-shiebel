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
 * A ShipmentReceipt.
 */
@Entity
@Table(name = "shipment_receipt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentReceipt implements Serializable {

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
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "quantity_accepted", precision = 21, scale = 2)
    private BigDecimal quantityAccepted;

    @Column(name = "quantity_rejected", precision = 21, scale = 2)
    private BigDecimal quantityRejected;

    @Column(name = "received_date")
    private Instant receivedDate;

    @Size(max = 255)
    @Column(name = "message", length = 255)
    private String message;

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
            "productType",
            "primaryProductCategory",
            "virtualProduct",
            "inventoryItemType",
            "taxSlab",
            "uom",
            "quantityUom",
            "weightUom",
            "sizeUom",
        },
        allowSetters = true
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party receivedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShipmentReceipt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ShipmentReceipt sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public ShipmentReceipt quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantityAccepted() {
        return this.quantityAccepted;
    }

    public ShipmentReceipt quantityAccepted(BigDecimal quantityAccepted) {
        this.setQuantityAccepted(quantityAccepted);
        return this;
    }

    public void setQuantityAccepted(BigDecimal quantityAccepted) {
        this.quantityAccepted = quantityAccepted;
    }

    public BigDecimal getQuantityRejected() {
        return this.quantityRejected;
    }

    public ShipmentReceipt quantityRejected(BigDecimal quantityRejected) {
        this.setQuantityRejected(quantityRejected);
        return this;
    }

    public void setQuantityRejected(BigDecimal quantityRejected) {
        this.quantityRejected = quantityRejected;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public ShipmentReceipt receivedDate(Instant receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getMessage() {
        return this.message;
    }

    public ShipmentReceipt message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Shipment getShipment() {
        return this.shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public ShipmentReceipt shipment(Shipment shipment) {
        this.setShipment(shipment);
        return this;
    }

    public ShipmentItem getShipmentItem() {
        return this.shipmentItem;
    }

    public void setShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
    }

    public ShipmentReceipt shipmentItem(ShipmentItem shipmentItem) {
        this.setShipmentItem(shipmentItem);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ShipmentReceipt order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public ShipmentReceipt orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public InventoryItem getInventoryItem() {
        return this.inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public ShipmentReceipt inventoryItem(InventoryItem inventoryItem) {
        this.setInventoryItem(inventoryItem);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShipmentReceipt product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Party getReceivedBy() {
        return this.receivedBy;
    }

    public void setReceivedBy(Party party) {
        this.receivedBy = party;
    }

    public ShipmentReceipt receivedBy(Party party) {
        this.setReceivedBy(party);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentReceipt)) {
            return false;
        }
        return getId() != null && getId().equals(((ShipmentReceipt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentReceipt{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", quantity=" + getQuantity() +
            ", quantityAccepted=" + getQuantityAccepted() +
            ", quantityRejected=" + getQuantityRejected() +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
