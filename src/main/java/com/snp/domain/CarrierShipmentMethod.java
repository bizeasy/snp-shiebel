package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CarrierShipmentMethod.
 */
@Entity
@Table(name = "carrier_shipment_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarrierShipmentMethod implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Size(max = 60)
    @Column(name = "carrier_service_code", length = 60)
    private String carrierServiceCode;

    @ManyToOne(optional = false)
    @NotNull
    private ShipmentMethodType shipmentMethodType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarrierShipmentMethod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public CarrierShipmentMethod sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getCarrierServiceCode() {
        return this.carrierServiceCode;
    }

    public CarrierShipmentMethod carrierServiceCode(String carrierServiceCode) {
        this.setCarrierServiceCode(carrierServiceCode);
        return this;
    }

    public void setCarrierServiceCode(String carrierServiceCode) {
        this.carrierServiceCode = carrierServiceCode;
    }

    public ShipmentMethodType getShipmentMethodType() {
        return this.shipmentMethodType;
    }

    public void setShipmentMethodType(ShipmentMethodType shipmentMethodType) {
        this.shipmentMethodType = shipmentMethodType;
    }

    public CarrierShipmentMethod shipmentMethodType(ShipmentMethodType shipmentMethodType) {
        this.setShipmentMethodType(shipmentMethodType);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public CarrierShipmentMethod party(Party party) {
        this.setParty(party);
        return this;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public CarrierShipmentMethod roleType(RoleType roleType) {
        this.setRoleType(roleType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarrierShipmentMethod)) {
            return false;
        }
        return getId() != null && getId().equals(((CarrierShipmentMethod) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarrierShipmentMethod{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", carrierServiceCode='" + getCarrierServiceCode() + "'" +
            "}";
    }
}
