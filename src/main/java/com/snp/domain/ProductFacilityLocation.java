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
 * A ProductFacilityLocation.
 */
@Entity
@Table(name = "product_facility_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFacilityLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "minimum_stock", precision = 21, scale = 2)
    private BigDecimal minimumStock;

    @Column(name = "move_quantity", precision = 21, scale = 2)
    private BigDecimal moveQuantity;

    @ManyToOne(optional = false)
    @NotNull
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

    @ManyToOne(optional = false)
    @NotNull
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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "facility", "locationType" }, allowSetters = true)
    private FacilityLocation facilityLocation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductFacilityLocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMinimumStock() {
        return this.minimumStock;
    }

    public ProductFacilityLocation minimumStock(BigDecimal minimumStock) {
        this.setMinimumStock(minimumStock);
        return this;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public BigDecimal getMoveQuantity() {
        return this.moveQuantity;
    }

    public ProductFacilityLocation moveQuantity(BigDecimal moveQuantity) {
        this.setMoveQuantity(moveQuantity);
        return this;
    }

    public void setMoveQuantity(BigDecimal moveQuantity) {
        this.moveQuantity = moveQuantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductFacilityLocation product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public ProductFacilityLocation facility(Facility facility) {
        this.setFacility(facility);
        return this;
    }

    public FacilityLocation getFacilityLocation() {
        return this.facilityLocation;
    }

    public void setFacilityLocation(FacilityLocation facilityLocation) {
        this.facilityLocation = facilityLocation;
    }

    public ProductFacilityLocation facilityLocation(FacilityLocation facilityLocation) {
        this.setFacilityLocation(facilityLocation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductFacilityLocation)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductFacilityLocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFacilityLocation{" +
            "id=" + getId() +
            ", minimumStock=" + getMinimumStock() +
            ", moveQuantity=" + getMoveQuantity() +
            "}";
    }
}
