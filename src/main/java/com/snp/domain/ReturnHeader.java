package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReturnHeader.
 */
@Entity
@Table(name = "return_header")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReturnHeader implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "needs_inventory_receive")
    private Boolean needsInventoryReceive;

    @Size(max = 20)
    @Column(name = "supplier_rma_id", length = 20)
    private String supplierRmaId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReturnHeaderType returnType;

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
    @JsonIgnoreProperties(value = { "paymentMethodType", "party" }, allowSetters = true)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contactMechType" }, allowSetters = true)
    private ContactMech originContactMech;

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
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReturnHeader id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getNeedsInventoryReceive() {
        return this.needsInventoryReceive;
    }

    public ReturnHeader needsInventoryReceive(Boolean needsInventoryReceive) {
        this.setNeedsInventoryReceive(needsInventoryReceive);
        return this;
    }

    public void setNeedsInventoryReceive(Boolean needsInventoryReceive) {
        this.needsInventoryReceive = needsInventoryReceive;
    }

    public String getSupplierRmaId() {
        return this.supplierRmaId;
    }

    public ReturnHeader supplierRmaId(String supplierRmaId) {
        this.setSupplierRmaId(supplierRmaId);
        return this;
    }

    public void setSupplierRmaId(String supplierRmaId) {
        this.supplierRmaId = supplierRmaId;
    }

    public ReturnHeaderType getReturnType() {
        return this.returnType;
    }

    public void setReturnType(ReturnHeaderType returnHeaderType) {
        this.returnType = returnHeaderType;
    }

    public ReturnHeader returnType(ReturnHeaderType returnHeaderType) {
        this.setReturnType(returnHeaderType);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ReturnHeader status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Party getFromParty() {
        return this.fromParty;
    }

    public void setFromParty(Party party) {
        this.fromParty = party;
    }

    public ReturnHeader fromParty(Party party) {
        this.setFromParty(party);
        return this;
    }

    public Party getToParty() {
        return this.toParty;
    }

    public void setToParty(Party party) {
        this.toParty = party;
    }

    public ReturnHeader toParty(Party party) {
        this.setToParty(party);
        return this;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ReturnHeader paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public ContactMech getOriginContactMech() {
        return this.originContactMech;
    }

    public void setOriginContactMech(ContactMech contactMech) {
        this.originContactMech = contactMech;
    }

    public ReturnHeader originContactMech(ContactMech contactMech) {
        this.setOriginContactMech(contactMech);
        return this;
    }

    public Facility getDestinationFacility() {
        return this.destinationFacility;
    }

    public void setDestinationFacility(Facility facility) {
        this.destinationFacility = facility;
    }

    public ReturnHeader destinationFacility(Facility facility) {
        this.setDestinationFacility(facility);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public ReturnHeader currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReturnHeader)) {
            return false;
        }
        return getId() != null && getId().equals(((ReturnHeader) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReturnHeader{" +
            "id=" + getId() +
            ", needsInventoryReceive='" + getNeedsInventoryReceive() + "'" +
            ", supplierRmaId='" + getSupplierRmaId() + "'" +
            "}";
    }
}
