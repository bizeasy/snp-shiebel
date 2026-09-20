package com.snp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductFeature.
 */
@Entity
@Table(name = "product_feature")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFeature implements Serializable {

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

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Size(max = 20)
    @Column(name = "abbrev", length = 20)
    private String abbrev;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductFeatureType productFeatureType;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductFeatureCategory productFeatureCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductFeature id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProductFeature name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductFeature description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ProductFeature amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ProductFeature sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getAbbrev() {
        return this.abbrev;
    }

    public ProductFeature abbrev(String abbrev) {
        this.setAbbrev(abbrev);
        return this;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public ProductFeatureType getProductFeatureType() {
        return this.productFeatureType;
    }

    public void setProductFeatureType(ProductFeatureType productFeatureType) {
        this.productFeatureType = productFeatureType;
    }

    public ProductFeature productFeatureType(ProductFeatureType productFeatureType) {
        this.setProductFeatureType(productFeatureType);
        return this;
    }

    public ProductFeatureCategory getProductFeatureCategory() {
        return this.productFeatureCategory;
    }

    public void setProductFeatureCategory(ProductFeatureCategory productFeatureCategory) {
        this.productFeatureCategory = productFeatureCategory;
    }

    public ProductFeature productFeatureCategory(ProductFeatureCategory productFeatureCategory) {
        this.setProductFeatureCategory(productFeatureCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductFeature)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductFeature) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFeature{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", sequenceNo=" + getSequenceNo() +
            ", abbrev='" + getAbbrev() + "'" +
            "}";
    }
}
