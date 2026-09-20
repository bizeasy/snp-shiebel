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
 * A ProductAssoc.
 */
@Entity
@Table(name = "product_assoc")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductAssoc implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private Instant fromDate;

    @Column(name = "thru_date")
    private Instant thruDate;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

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
    private Product productTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductAssocType productAssocType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductAssoc id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public ProductAssoc fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return this.thruDate;
    }

    public ProductAssoc thruDate(Instant thruDate) {
        this.setThruDate(thruDate);
        return this;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ProductAssoc sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public ProductAssoc quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductAssoc product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Product getProductTo() {
        return this.productTo;
    }

    public void setProductTo(Product product) {
        this.productTo = product;
    }

    public ProductAssoc productTo(Product product) {
        this.setProductTo(product);
        return this;
    }

    public ProductAssocType getProductAssocType() {
        return this.productAssocType;
    }

    public void setProductAssocType(ProductAssocType productAssocType) {
        this.productAssocType = productAssocType;
    }

    public ProductAssoc productAssocType(ProductAssocType productAssocType) {
        this.setProductAssocType(productAssocType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductAssoc)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductAssoc) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductAssoc{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", thruDate='" + getThruDate() + "'" +
            ", sequenceNo=" + getSequenceNo() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
