package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductCategory.
 */
@Entity
@Table(name = "product_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCategory implements Serializable {

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

    @Size(max = 2000)
    @Column(name = "long_description", length = 2000)
    private String longDescription;

    @Size(max = 25)
    @Column(name = "attribute", length = 25)
    private String attribute;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Size(max = 2000)
    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    @Size(max = 2000)
    @Column(name = "alt_image_url", length = 2000)
    private String altImageUrl;

    @Size(max = 2000)
    @Column(name = "banner_url", length = 2000)
    private String bannerUrl;

    @Size(max = 2000)
    @Column(name = "large_image_url", length = 2000)
    private String largeImageUrl;

    @Size(max = 2000)
    @Column(name = "alt_large_image_url", length = 2000)
    private String altLargeImageUrl;

    @Lob
    @Column(name = "info")
    private String info;

    @Column(name = "is_banner")
    private Boolean isBanner;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategoryType productCategoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productCategoryType", "parent" }, allowSetters = true)
    private ProductCategory parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProductCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public ProductCategory longDescription(String longDescription) {
        this.setLongDescription(longDescription);
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public ProductCategory attribute(String attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public ProductCategory sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public ProductCategory imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAltImageUrl() {
        return this.altImageUrl;
    }

    public ProductCategory altImageUrl(String altImageUrl) {
        this.setAltImageUrl(altImageUrl);
        return this;
    }

    public void setAltImageUrl(String altImageUrl) {
        this.altImageUrl = altImageUrl;
    }

    public String getBannerUrl() {
        return this.bannerUrl;
    }

    public ProductCategory bannerUrl(String bannerUrl) {
        this.setBannerUrl(bannerUrl);
        return this;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getLargeImageUrl() {
        return this.largeImageUrl;
    }

    public ProductCategory largeImageUrl(String largeImageUrl) {
        this.setLargeImageUrl(largeImageUrl);
        return this;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getAltLargeImageUrl() {
        return this.altLargeImageUrl;
    }

    public ProductCategory altLargeImageUrl(String altLargeImageUrl) {
        this.setAltLargeImageUrl(altLargeImageUrl);
        return this;
    }

    public void setAltLargeImageUrl(String altLargeImageUrl) {
        this.altLargeImageUrl = altLargeImageUrl;
    }

    public String getInfo() {
        return this.info;
    }

    public ProductCategory info(String info) {
        this.setInfo(info);
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getIsBanner() {
        return this.isBanner;
    }

    public ProductCategory isBanner(Boolean isBanner) {
        this.setIsBanner(isBanner);
        return this;
    }

    public void setIsBanner(Boolean isBanner) {
        this.isBanner = isBanner;
    }

    public ProductCategoryType getProductCategoryType() {
        return this.productCategoryType;
    }

    public void setProductCategoryType(ProductCategoryType productCategoryType) {
        this.productCategoryType = productCategoryType;
    }

    public ProductCategory productCategoryType(ProductCategoryType productCategoryType) {
        this.setProductCategoryType(productCategoryType);
        return this;
    }

    public ProductCategory getParent() {
        return this.parent;
    }

    public void setParent(ProductCategory productCategory) {
        this.parent = productCategory;
    }

    public ProductCategory parent(ProductCategory productCategory) {
        this.setParent(productCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", attribute='" + getAttribute() + "'" +
            ", sequenceNo=" + getSequenceNo() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", altImageUrl='" + getAltImageUrl() + "'" +
            ", bannerUrl='" + getBannerUrl() + "'" +
            ", largeImageUrl='" + getLargeImageUrl() + "'" +
            ", altLargeImageUrl='" + getAltLargeImageUrl() + "'" +
            ", info='" + getInfo() + "'" +
            ", isBanner='" + getIsBanner() + "'" +
            "}";
    }
}
