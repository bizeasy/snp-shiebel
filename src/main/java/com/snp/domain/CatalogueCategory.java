package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CatalogueCategory.
 */
@Entity
@Table(name = "catalogue_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CatalogueCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @ManyToOne(optional = false)
    @NotNull
    private Catalogue catalogue;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productCategoryType", "parent" }, allowSetters = true)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private CatalogueCategoryType catalogueCategoryType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CatalogueCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public CatalogueCategory sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Catalogue getCatalogue() {
        return this.catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public CatalogueCategory catalogue(Catalogue catalogue) {
        this.setCatalogue(catalogue);
        return this;
    }

    public ProductCategory getProductCategory() {
        return this.productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public CatalogueCategory productCategory(ProductCategory productCategory) {
        this.setProductCategory(productCategory);
        return this;
    }

    public CatalogueCategoryType getCatalogueCategoryType() {
        return this.catalogueCategoryType;
    }

    public void setCatalogueCategoryType(CatalogueCategoryType catalogueCategoryType) {
        this.catalogueCategoryType = catalogueCategoryType;
    }

    public CatalogueCategory catalogueCategoryType(CatalogueCategoryType catalogueCategoryType) {
        this.setCatalogueCategoryType(catalogueCategoryType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogueCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((CatalogueCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogueCategory{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            "}";
    }
}
