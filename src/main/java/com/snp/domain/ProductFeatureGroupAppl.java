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
 * A ProductFeatureGroupAppl.
 */
@Entity
@Table(name = "product_feature_group_appl")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFeatureGroupAppl implements Serializable {

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

    @ManyToOne(optional = false)
    @NotNull
    private ProductFeatureGroup productFeatureGroup;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productFeatureType", "productFeatureCategory" }, allowSetters = true)
    private ProductFeature productFeature;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductFeatureGroupAppl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public ProductFeatureGroupAppl fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return this.thruDate;
    }

    public ProductFeatureGroupAppl thruDate(Instant thruDate) {
        this.setThruDate(thruDate);
        return this;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ProductFeatureGroupAppl sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public ProductFeatureGroup getProductFeatureGroup() {
        return this.productFeatureGroup;
    }

    public void setProductFeatureGroup(ProductFeatureGroup productFeatureGroup) {
        this.productFeatureGroup = productFeatureGroup;
    }

    public ProductFeatureGroupAppl productFeatureGroup(ProductFeatureGroup productFeatureGroup) {
        this.setProductFeatureGroup(productFeatureGroup);
        return this;
    }

    public ProductFeature getProductFeature() {
        return this.productFeature;
    }

    public void setProductFeature(ProductFeature productFeature) {
        this.productFeature = productFeature;
    }

    public ProductFeatureGroupAppl productFeature(ProductFeature productFeature) {
        this.setProductFeature(productFeature);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductFeatureGroupAppl)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductFeatureGroupAppl) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFeatureGroupAppl{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", thruDate='" + getThruDate() + "'" +
            ", sequenceNo=" + getSequenceNo() +
            "}";
    }
}
