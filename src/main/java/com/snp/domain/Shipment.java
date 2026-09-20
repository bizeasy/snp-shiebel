package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Shipment.
 */
@Entity
@Table(name = "shipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Shipment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "estimated_ship_date")
    private Instant estimatedShipDate;

    @Column(name = "estimated_ready_date")
    private Instant estimatedReadyDate;

    @Column(name = "estimated_arrival_date")
    private Instant estimatedArrivalDate;

    @Column(name = "estimated_ship_cost", precision = 21, scale = 2)
    private BigDecimal estimatedShipCost;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShipmentType shipmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShipmentMethodType shipmentMethodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

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
            "facilityType",
            "parentFacility",
            "ownerParty",
            "facilityGroup",
            "sizeUom",
            "geoPoint",
            "inventoryItemType",
            "productStore",
        },
        allowSetters = true
    )
    private Facility originFacility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "facilityType",
            "parentFacility",
            "ownerParty",
            "facilityGroup",
            "sizeUom",
            "geoPoint",
            "inventoryItemType",
            "productStore",
        },
        allowSetters = true
    )
    private Facility destinationFacility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party fromParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party toParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party carrierParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contactMechType" }, allowSetters = true)
    private ContactMech contactMech;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shipment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEstimatedShipDate() {
        return this.estimatedShipDate;
    }

    public Shipment estimatedShipDate(Instant estimatedShipDate) {
        this.setEstimatedShipDate(estimatedShipDate);
        return this;
    }

    public void setEstimatedShipDate(Instant estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public Instant getEstimatedReadyDate() {
        return this.estimatedReadyDate;
    }

    public Shipment estimatedReadyDate(Instant estimatedReadyDate) {
        this.setEstimatedReadyDate(estimatedReadyDate);
        return this;
    }

    public void setEstimatedReadyDate(Instant estimatedReadyDate) {
        this.estimatedReadyDate = estimatedReadyDate;
    }

    public Instant getEstimatedArrivalDate() {
        return this.estimatedArrivalDate;
    }

    public Shipment estimatedArrivalDate(Instant estimatedArrivalDate) {
        this.setEstimatedArrivalDate(estimatedArrivalDate);
        return this;
    }

    public void setEstimatedArrivalDate(Instant estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    public BigDecimal getEstimatedShipCost() {
        return this.estimatedShipCost;
    }

    public Shipment estimatedShipCost(BigDecimal estimatedShipCost) {
        this.setEstimatedShipCost(estimatedShipCost);
        return this;
    }

    public void setEstimatedShipCost(BigDecimal estimatedShipCost) {
        this.estimatedShipCost = estimatedShipCost;
    }

    public ShipmentType getShipmentType() {
        return this.shipmentType;
    }

    public void setShipmentType(ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }

    public Shipment shipmentType(ShipmentType shipmentType) {
        this.setShipmentType(shipmentType);
        return this;
    }

    public ShipmentMethodType getShipmentMethodType() {
        return this.shipmentMethodType;
    }

    public void setShipmentMethodType(ShipmentMethodType shipmentMethodType) {
        this.shipmentMethodType = shipmentMethodType;
    }

    public Shipment shipmentMethodType(ShipmentMethodType shipmentMethodType) {
        this.setShipmentMethodType(shipmentMethodType);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Shipment status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Shipment order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Shipment orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Facility getOriginFacility() {
        return this.originFacility;
    }

    public void setOriginFacility(Facility facility) {
        this.originFacility = facility;
    }

    public Shipment originFacility(Facility facility) {
        this.setOriginFacility(facility);
        return this;
    }

    public Facility getDestinationFacility() {
        return this.destinationFacility;
    }

    public void setDestinationFacility(Facility facility) {
        this.destinationFacility = facility;
    }

    public Shipment destinationFacility(Facility facility) {
        this.setDestinationFacility(facility);
        return this;
    }

    public Party getFromParty() {
        return this.fromParty;
    }

    public void setFromParty(Party party) {
        this.fromParty = party;
    }

    public Shipment fromParty(Party party) {
        this.setFromParty(party);
        return this;
    }

    public Party getToParty() {
        return this.toParty;
    }

    public void setToParty(Party party) {
        this.toParty = party;
    }

    public Shipment toParty(Party party) {
        this.setToParty(party);
        return this;
    }

    public Party getCarrierParty() {
        return this.carrierParty;
    }

    public void setCarrierParty(Party party) {
        this.carrierParty = party;
    }

    public Shipment carrierParty(Party party) {
        this.setCarrierParty(party);
        return this;
    }

    public ContactMech getContactMech() {
        return this.contactMech;
    }

    public void setContactMech(ContactMech contactMech) {
        this.contactMech = contactMech;
    }

    public Shipment contactMech(ContactMech contactMech) {
        this.setContactMech(contactMech);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shipment)) {
            return false;
        }
        return getId() != null && getId().equals(((Shipment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shipment{" +
            "id=" + getId() +
            ", estimatedShipDate='" + getEstimatedShipDate() + "'" +
            ", estimatedReadyDate='" + getEstimatedReadyDate() + "'" +
            ", estimatedArrivalDate='" + getEstimatedArrivalDate() + "'" +
            ", estimatedShipCost=" + getEstimatedShipCost() +
            "}";
    }
}
