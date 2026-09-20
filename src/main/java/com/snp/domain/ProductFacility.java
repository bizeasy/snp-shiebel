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
 * A ProductFacility.
 */
@Entity
@Table(name = "product_facility")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFacility implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "minimum_stock", precision = 21, scale = 2)
    private BigDecimal minimumStock;

    @Column(name = "reorder_qty", precision = 21, scale = 2)
    private BigDecimal reorderQty;

    @Column(name = "days_to_ship")
    private Integer daysToShip;

    @Column(name = "last_inventory_count", precision = 21, scale = 2)
    private BigDecimal lastInventoryCount;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductFacility id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMinimumStock() {
        return this.minimumStock;
    }

    public ProductFacility minimumStock(BigDecimal minimumStock) {
        this.setMinimumStock(minimumStock);
        return this;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public BigDecimal getReorderQty() {
        return this.reorderQty;
    }

    public ProductFacility reorderQty(BigDecimal reorderQty) {
        this.setReorderQty(reorderQty);
        return this;
    }

    public void setReorderQty(BigDecimal reorderQty) {
        this.reorderQty = reorderQty;
    }

    public Integer getDaysToShip() {
        return this.daysToShip;
    }

    public ProductFacility daysToShip(Integer daysToShip) {
        this.setDaysToShip(daysToShip);
        return this;
    }

    public void setDaysToShip(Integer daysToShip) {
        this.daysToShip = daysToShip;
    }

    public BigDecimal getLastInventoryCount() {
        return this.lastInventoryCount;
    }

    public ProductFacility lastInventoryCount(BigDecimal lastInventoryCount) {
        this.setLastInventoryCount(lastInventoryCount);
        return this;
    }

    public void setLastInventoryCount(BigDecimal lastInventoryCount) {
        this.lastInventoryCount = lastInventoryCount;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductFacility product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public ProductFacility facility(Facility facility) {
        this.setFacility(facility);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductFacility)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductFacility) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFacility{" +
            "id=" + getId() +
            ", minimumStock=" + getMinimumStock() +
            ", reorderQty=" + getReorderQty() +
            ", daysToShip=" + getDaysToShip() +
            ", lastInventoryCount=" + getLastInventoryCount() +
            "}";
    }
}
