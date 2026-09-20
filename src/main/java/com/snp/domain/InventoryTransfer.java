package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InventoryTransfer.
 */
@Entity
@Table(name = "inventory_transfer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryTransfer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sent_date")
    private Instant sentDate;

    @Column(name = "received_date")
    private Instant receivedDate;

    @Size(max = 255)
    @Column(name = "comments", length = 255)
    private String comments;

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
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

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
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facility", "locationType" }, allowSetters = true)
    private FacilityLocation facilityLocation;

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
    private Facility facilityTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facility", "locationType" }, allowSetters = true)
    private FacilityLocation facilityLocationTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "inventoryItem", "order", "orderItem", "shipment", "shipmentItem", "issuedByParty" },
        allowSetters = true
    )
    private ItemIssuance issuance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InventoryTransfer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSentDate() {
        return this.sentDate;
    }

    public InventoryTransfer sentDate(Instant sentDate) {
        this.setSentDate(sentDate);
        return this;
    }

    public void setSentDate(Instant sentDate) {
        this.sentDate = sentDate;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public InventoryTransfer receivedDate(Instant receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getComments() {
        return this.comments;
    }

    public InventoryTransfer comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public InventoryItem getInventoryItem() {
        return this.inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public InventoryTransfer inventoryItem(InventoryItem inventoryItem) {
        this.setInventoryItem(inventoryItem);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public InventoryTransfer status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public InventoryTransfer facility(Facility facility) {
        this.setFacility(facility);
        return this;
    }

    public FacilityLocation getFacilityLocation() {
        return this.facilityLocation;
    }

    public void setFacilityLocation(FacilityLocation facilityLocation) {
        this.facilityLocation = facilityLocation;
    }

    public InventoryTransfer facilityLocation(FacilityLocation facilityLocation) {
        this.setFacilityLocation(facilityLocation);
        return this;
    }

    public Facility getFacilityTo() {
        return this.facilityTo;
    }

    public void setFacilityTo(Facility facility) {
        this.facilityTo = facility;
    }

    public InventoryTransfer facilityTo(Facility facility) {
        this.setFacilityTo(facility);
        return this;
    }

    public FacilityLocation getFacilityLocationTo() {
        return this.facilityLocationTo;
    }

    public void setFacilityLocationTo(FacilityLocation facilityLocation) {
        this.facilityLocationTo = facilityLocation;
    }

    public InventoryTransfer facilityLocationTo(FacilityLocation facilityLocation) {
        this.setFacilityLocationTo(facilityLocation);
        return this;
    }

    public ItemIssuance getIssuance() {
        return this.issuance;
    }

    public void setIssuance(ItemIssuance itemIssuance) {
        this.issuance = itemIssuance;
    }

    public InventoryTransfer issuance(ItemIssuance itemIssuance) {
        this.setIssuance(itemIssuance);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryTransfer)) {
            return false;
        }
        return getId() != null && getId().equals(((InventoryTransfer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryTransfer{" +
            "id=" + getId() +
            ", sentDate='" + getSentDate() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
