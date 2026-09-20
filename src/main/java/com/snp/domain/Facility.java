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
 * A Facility.
 */
@Entity
@Table(name = "facility")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Facility implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "name", length = 60, nullable = false, unique = true)
    private String name;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "facility_size", precision = 21, scale = 2)
    private BigDecimal facilitySize;

    @ManyToOne(fetch = FetchType.LAZY)
    private FacilityType facilityType;

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
    private Facility parentFacility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party ownerParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facilityGroupType", "parentFacilityGroup" }, allowSetters = true)
    private FacilityGroup facilityGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom sizeUom;

    @ManyToOne(fetch = FetchType.LAZY)
    private GeoPoint geoPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    private InventoryItemType inventoryItemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "owner", "admin" }, allowSetters = true)
    private ProductStore productStore;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facility id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Facility name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Facility description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFacilitySize() {
        return this.facilitySize;
    }

    public Facility facilitySize(BigDecimal facilitySize) {
        this.setFacilitySize(facilitySize);
        return this;
    }

    public void setFacilitySize(BigDecimal facilitySize) {
        this.facilitySize = facilitySize;
    }

    public FacilityType getFacilityType() {
        return this.facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public Facility facilityType(FacilityType facilityType) {
        this.setFacilityType(facilityType);
        return this;
    }

    public Facility getParentFacility() {
        return this.parentFacility;
    }

    public void setParentFacility(Facility facility) {
        this.parentFacility = facility;
    }

    public Facility parentFacility(Facility facility) {
        this.setParentFacility(facility);
        return this;
    }

    public Party getOwnerParty() {
        return this.ownerParty;
    }

    public void setOwnerParty(Party party) {
        this.ownerParty = party;
    }

    public Facility ownerParty(Party party) {
        this.setOwnerParty(party);
        return this;
    }

    public FacilityGroup getFacilityGroup() {
        return this.facilityGroup;
    }

    public void setFacilityGroup(FacilityGroup facilityGroup) {
        this.facilityGroup = facilityGroup;
    }

    public Facility facilityGroup(FacilityGroup facilityGroup) {
        this.setFacilityGroup(facilityGroup);
        return this;
    }

    public Uom getSizeUom() {
        return this.sizeUom;
    }

    public void setSizeUom(Uom uom) {
        this.sizeUom = uom;
    }

    public Facility sizeUom(Uom uom) {
        this.setSizeUom(uom);
        return this;
    }

    public GeoPoint getGeoPoint() {
        return this.geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Facility geoPoint(GeoPoint geoPoint) {
        this.setGeoPoint(geoPoint);
        return this;
    }

    public InventoryItemType getInventoryItemType() {
        return this.inventoryItemType;
    }

    public void setInventoryItemType(InventoryItemType inventoryItemType) {
        this.inventoryItemType = inventoryItemType;
    }

    public Facility inventoryItemType(InventoryItemType inventoryItemType) {
        this.setInventoryItemType(inventoryItemType);
        return this;
    }

    public ProductStore getProductStore() {
        return this.productStore;
    }

    public void setProductStore(ProductStore productStore) {
        this.productStore = productStore;
    }

    public Facility productStore(ProductStore productStore) {
        this.setProductStore(productStore);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facility)) {
            return false;
        }
        return getId() != null && getId().equals(((Facility) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facility{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", facilitySize=" + getFacilitySize() +
            "}";
    }
}
