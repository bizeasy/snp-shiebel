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
 * A OrderShipment.
 */
@Entity
@Table(name = "order_shipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderShipment implements Serializable {

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

    @ManyToOne(optional = false)
    @NotNull
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderShipment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public OrderShipment quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderShipment order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public OrderShipment orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Shipment getShipment() {
        return this.shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public OrderShipment shipment(Shipment shipment) {
        this.setShipment(shipment);
        return this;
    }

    public ShipmentItem getShipmentItem() {
        return this.shipmentItem;
    }

    public void setShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
    }

    public OrderShipment shipmentItem(ShipmentItem shipmentItem) {
        this.setShipmentItem(shipmentItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderShipment)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderShipment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderShipment{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
