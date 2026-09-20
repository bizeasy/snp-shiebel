package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductKeyword.
 */
@Entity
@Table(name = "product_keyword")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductKeyword implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "keyword", length = 60, nullable = false)
    private String keyword;

    @Column(name = "relevancy_weight")
    private Integer relevancyWeight;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private KeywordType keywordType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductKeyword id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public ProductKeyword keyword(String keyword) {
        this.setKeyword(keyword);
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getRelevancyWeight() {
        return this.relevancyWeight;
    }

    public ProductKeyword relevancyWeight(Integer relevancyWeight) {
        this.setRelevancyWeight(relevancyWeight);
        return this;
    }

    public void setRelevancyWeight(Integer relevancyWeight) {
        this.relevancyWeight = relevancyWeight;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductKeyword product(Product product) {
        this.setProduct(product);
        return this;
    }

    public KeywordType getKeywordType() {
        return this.keywordType;
    }

    public void setKeywordType(KeywordType keywordType) {
        this.keywordType = keywordType;
    }

    public ProductKeyword keywordType(KeywordType keywordType) {
        this.setKeywordType(keywordType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductKeyword)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductKeyword) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductKeyword{" +
            "id=" + getId() +
            ", keyword='" + getKeyword() + "'" +
            ", relevancyWeight=" + getRelevancyWeight() +
            "}";
    }
}
