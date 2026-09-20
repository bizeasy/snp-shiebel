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
 * A ShipmentStatus.
 */
@Entity
@Table(name = "shipment_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status_date_time", nullable = false)
    private Instant statusDateTime;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShipmentStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStatusDateTime() {
        return this.statusDateTime;
    }

    public ShipmentStatus statusDateTime(Instant statusDateTime) {
        this.setStatusDateTime(statusDateTime);
        return this;
    }

    public void setStatusDateTime(Instant statusDateTime) {
        this.statusDateTime = statusDateTime;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ShipmentStatus sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Shipment getShipment() {
        return this.shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public ShipmentStatus shipment(Shipment shipment) {
        this.setShipment(shipment);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ShipmentStatus status(Status status) {
        this.setStatus(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((ShipmentStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentStatus{" +
            "id=" + getId() +
            ", statusDateTime='" + getStatusDateTime() + "'" +
            ", sequenceNo=" + getSequenceNo() +
            "}";
    }
}
