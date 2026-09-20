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
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 60)
    @Column(name = "internal_name", length = 60)
    private String internalName;

    @Size(max = 60)
    @Column(name = "brand_name", length = 60)
    private String brandName;

    @Size(max = 60)
    @Column(name = "variant", length = 60)
    private String variant;

    @Size(max = 60)
    @Column(name = "sku", length = 60)
    private String sku;

    @Column(name = "introduction_date")
    private Instant introductionDate;

    @Column(name = "release_date")
    private Instant releaseDate;

    @Column(name = "quantity_included", precision = 21, scale = 2)
    private BigDecimal quantityIncluded;

    @Column(name = "pieces_included")
    private Integer piecesIncluded;

    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @Column(name = "height", precision = 21, scale = 2)
    private BigDecimal height;

    @Column(name = "width", precision = 21, scale = 2)
    private BigDecimal width;

    @Column(name = "depth", precision = 21, scale = 2)
    private BigDecimal depth;

    @Size(max = 2000)
    @Column(name = "small_image_url", length = 2000)
    private String smallImageUrl;

    @Size(max = 2000)
    @Column(name = "medium_image_url", length = 2000)
    private String mediumImageUrl;

    @Size(max = 2000)
    @Column(name = "large_image_url", length = 2000)
    private String largeImageUrl;

    @Size(max = 2000)
    @Column(name = "detail_image_url", length = 2000)
    private String detailImageUrl;

    @Size(max = 2000)
    @Column(name = "original_image_url", length = 2000)
    private String originalImageUrl;

    @Size(max = 2000)
    @Column(name = "alt_image_url_1", length = 2000)
    private String altImageUrl1;

    @Size(max = 2000)
    @Column(name = "alt_image_url_2", length = 2000)
    private String altImageUrl2;

    @Size(max = 2000)
    @Column(name = "alt_image_url_3", length = 2000)
    private String altImageUrl3;

    @Size(max = 2000)
    @Column(name = "alt_image_url_4", length = 2000)
    private String altImageUrl4;

    @Size(max = 2000)
    @Column(name = "alt_image_url_5", length = 2000)
    private String altImageUrl5;

    @Size(max = 2000)
    @Column(name = "alt_image_url_6", length = 2000)
    private String altImageUrl6;

    @Size(max = 2000)
    @Column(name = "video_url", length = 2000)
    private String videoUrl;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "cgst", precision = 21, scale = 2)
    private BigDecimal cgst;

    @Column(name = "igst", precision = 21, scale = 2)
    private BigDecimal igst;

    @Column(name = "sgst", precision = 21, scale = 2)
    private BigDecimal sgst;

    @Column(name = "cgst_percentage", precision = 21, scale = 2)
    private BigDecimal cgstPercentage;

    @Column(name = "igst_percentage", precision = 21, scale = 2)
    private BigDecimal igstPercentage;

    @Column(name = "sgst_percentage", precision = 21, scale = 2)
    private BigDecimal sgstPercentage;

    @Column(name = "total_price", precision = 21, scale = 2)
    private BigDecimal totalPrice;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @Size(max = 2000)
    @Column(name = "long_description", length = 2000)
    private String longDescription;

    @Lob
    @Column(name = "info")
    private String info;

    @Column(name = "is_virtual")
    private Boolean isVirtual;

    @Column(name = "is_variant")
    private Boolean isVariant;

    @Column(name = "require_inventory")
    private Boolean requireInventory;

    @Column(name = "returnable")
    private Boolean returnable;

    @Column(name = "is_popular")
    private Boolean isPopular;

    @Column(name = "is_banner")
    private Boolean isBanner;

    @Size(max = 2000)
    @Column(name = "banner_url", length = 2000)
    private String bannerUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productCategoryType", "parent" }, allowSetters = true)
    private ProductCategory primaryProductCategory;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private Product virtualProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    private InventoryItemType inventoryItemType;

    @ManyToOne(fetch = FetchType.LAZY)
    private TaxSlab taxSlab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom uom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom quantityUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom weightUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom sizeUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalName() {
        return this.internalName;
    }

    public Product internalName(String internalName) {
        this.setInternalName(internalName);
        return this;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public Product brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getVariant() {
        return this.variant;
    }

    public Product variant(String variant) {
        this.setVariant(variant);
        return this;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getSku() {
        return this.sku;
    }

    public Product sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Instant getIntroductionDate() {
        return this.introductionDate;
    }

    public Product introductionDate(Instant introductionDate) {
        this.setIntroductionDate(introductionDate);
        return this;
    }

    public void setIntroductionDate(Instant introductionDate) {
        this.introductionDate = introductionDate;
    }

    public Instant getReleaseDate() {
        return this.releaseDate;
    }

    public Product releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public BigDecimal getQuantityIncluded() {
        return this.quantityIncluded;
    }

    public Product quantityIncluded(BigDecimal quantityIncluded) {
        this.setQuantityIncluded(quantityIncluded);
        return this;
    }

    public void setQuantityIncluded(BigDecimal quantityIncluded) {
        this.quantityIncluded = quantityIncluded;
    }

    public Integer getPiecesIncluded() {
        return this.piecesIncluded;
    }

    public Product piecesIncluded(Integer piecesIncluded) {
        this.setPiecesIncluded(piecesIncluded);
        return this;
    }

    public void setPiecesIncluded(Integer piecesIncluded) {
        this.piecesIncluded = piecesIncluded;
    }

    public BigDecimal getWeight() {
        return this.weight;
    }

    public Product weight(BigDecimal weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return this.height;
    }

    public Product height(BigDecimal height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWidth() {
        return this.width;
    }

    public Product width(BigDecimal width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepth() {
        return this.depth;
    }

    public Product depth(BigDecimal depth) {
        this.setDepth(depth);
        return this;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public String getSmallImageUrl() {
        return this.smallImageUrl;
    }

    public Product smallImageUrl(String smallImageUrl) {
        this.setSmallImageUrl(smallImageUrl);
        return this;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getMediumImageUrl() {
        return this.mediumImageUrl;
    }

    public Product mediumImageUrl(String mediumImageUrl) {
        this.setMediumImageUrl(mediumImageUrl);
        return this;
    }

    public void setMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }

    public String getLargeImageUrl() {
        return this.largeImageUrl;
    }

    public Product largeImageUrl(String largeImageUrl) {
        this.setLargeImageUrl(largeImageUrl);
        return this;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getDetailImageUrl() {
        return this.detailImageUrl;
    }

    public Product detailImageUrl(String detailImageUrl) {
        this.setDetailImageUrl(detailImageUrl);
        return this;
    }

    public void setDetailImageUrl(String detailImageUrl) {
        this.detailImageUrl = detailImageUrl;
    }

    public String getOriginalImageUrl() {
        return this.originalImageUrl;
    }

    public Product originalImageUrl(String originalImageUrl) {
        this.setOriginalImageUrl(originalImageUrl);
        return this;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public String getAltImageUrl1() {
        return this.altImageUrl1;
    }

    public Product altImageUrl1(String altImageUrl1) {
        this.setAltImageUrl1(altImageUrl1);
        return this;
    }

    public void setAltImageUrl1(String altImageUrl1) {
        this.altImageUrl1 = altImageUrl1;
    }

    public String getAltImageUrl2() {
        return this.altImageUrl2;
    }

    public Product altImageUrl2(String altImageUrl2) {
        this.setAltImageUrl2(altImageUrl2);
        return this;
    }

    public void setAltImageUrl2(String altImageUrl2) {
        this.altImageUrl2 = altImageUrl2;
    }

    public String getAltImageUrl3() {
        return this.altImageUrl3;
    }

    public Product altImageUrl3(String altImageUrl3) {
        this.setAltImageUrl3(altImageUrl3);
        return this;
    }

    public void setAltImageUrl3(String altImageUrl3) {
        this.altImageUrl3 = altImageUrl3;
    }

    public String getAltImageUrl4() {
        return this.altImageUrl4;
    }

    public Product altImageUrl4(String altImageUrl4) {
        this.setAltImageUrl4(altImageUrl4);
        return this;
    }

    public void setAltImageUrl4(String altImageUrl4) {
        this.altImageUrl4 = altImageUrl4;
    }

    public String getAltImageUrl5() {
        return this.altImageUrl5;
    }

    public Product altImageUrl5(String altImageUrl5) {
        this.setAltImageUrl5(altImageUrl5);
        return this;
    }

    public void setAltImageUrl5(String altImageUrl5) {
        this.altImageUrl5 = altImageUrl5;
    }

    public String getAltImageUrl6() {
        return this.altImageUrl6;
    }

    public Product altImageUrl6(String altImageUrl6) {
        this.setAltImageUrl6(altImageUrl6);
        return this;
    }

    public void setAltImageUrl6(String altImageUrl6) {
        this.altImageUrl6 = altImageUrl6;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Product videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public Product quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCgst() {
        return this.cgst;
    }

    public Product cgst(BigDecimal cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst;
    }

    public BigDecimal getIgst() {
        return this.igst;
    }

    public Product igst(BigDecimal igst) {
        this.setIgst(igst);
        return this;
    }

    public void setIgst(BigDecimal igst) {
        this.igst = igst;
    }

    public BigDecimal getSgst() {
        return this.sgst;
    }

    public Product sgst(BigDecimal sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst;
    }

    public BigDecimal getCgstPercentage() {
        return this.cgstPercentage;
    }

    public Product cgstPercentage(BigDecimal cgstPercentage) {
        this.setCgstPercentage(cgstPercentage);
        return this;
    }

    public void setCgstPercentage(BigDecimal cgstPercentage) {
        this.cgstPercentage = cgstPercentage;
    }

    public BigDecimal getIgstPercentage() {
        return this.igstPercentage;
    }

    public Product igstPercentage(BigDecimal igstPercentage) {
        this.setIgstPercentage(igstPercentage);
        return this;
    }

    public void setIgstPercentage(BigDecimal igstPercentage) {
        this.igstPercentage = igstPercentage;
    }

    public BigDecimal getSgstPercentage() {
        return this.sgstPercentage;
    }

    public Product sgstPercentage(BigDecimal sgstPercentage) {
        this.setSgstPercentage(sgstPercentage);
        return this;
    }

    public void setSgstPercentage(BigDecimal sgstPercentage) {
        this.sgstPercentage = sgstPercentage;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public Product totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public Product longDescription(String longDescription) {
        this.setLongDescription(longDescription);
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getInfo() {
        return this.info;
    }

    public Product info(String info) {
        this.setInfo(info);
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getIsVirtual() {
        return this.isVirtual;
    }

    public Product isVirtual(Boolean isVirtual) {
        this.setIsVirtual(isVirtual);
        return this;
    }

    public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public Boolean getIsVariant() {
        return this.isVariant;
    }

    public Product isVariant(Boolean isVariant) {
        this.setIsVariant(isVariant);
        return this;
    }

    public void setIsVariant(Boolean isVariant) {
        this.isVariant = isVariant;
    }

    public Boolean getRequireInventory() {
        return this.requireInventory;
    }

    public Product requireInventory(Boolean requireInventory) {
        this.setRequireInventory(requireInventory);
        return this;
    }

    public void setRequireInventory(Boolean requireInventory) {
        this.requireInventory = requireInventory;
    }

    public Boolean getReturnable() {
        return this.returnable;
    }

    public Product returnable(Boolean returnable) {
        this.setReturnable(returnable);
        return this;
    }

    public void setReturnable(Boolean returnable) {
        this.returnable = returnable;
    }

    public Boolean getIsPopular() {
        return this.isPopular;
    }

    public Product isPopular(Boolean isPopular) {
        this.setIsPopular(isPopular);
        return this;
    }

    public void setIsPopular(Boolean isPopular) {
        this.isPopular = isPopular;
    }

    public Boolean getIsBanner() {
        return this.isBanner;
    }

    public Product isBanner(Boolean isBanner) {
        this.setIsBanner(isBanner);
        return this;
    }

    public void setIsBanner(Boolean isBanner) {
        this.isBanner = isBanner;
    }

    public String getBannerUrl() {
        return this.bannerUrl;
    }

    public Product bannerUrl(String bannerUrl) {
        this.setBannerUrl(bannerUrl);
        return this;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Product productType(ProductType productType) {
        this.setProductType(productType);
        return this;
    }

    public ProductCategory getPrimaryProductCategory() {
        return this.primaryProductCategory;
    }

    public void setPrimaryProductCategory(ProductCategory productCategory) {
        this.primaryProductCategory = productCategory;
    }

    public Product primaryProductCategory(ProductCategory productCategory) {
        this.setPrimaryProductCategory(productCategory);
        return this;
    }

    public Product getVirtualProduct() {
        return this.virtualProduct;
    }

    public void setVirtualProduct(Product product) {
        this.virtualProduct = product;
    }

    public Product virtualProduct(Product product) {
        this.setVirtualProduct(product);
        return this;
    }

    public InventoryItemType getInventoryItemType() {
        return this.inventoryItemType;
    }

    public void setInventoryItemType(InventoryItemType inventoryItemType) {
        this.inventoryItemType = inventoryItemType;
    }

    public Product inventoryItemType(InventoryItemType inventoryItemType) {
        this.setInventoryItemType(inventoryItemType);
        return this;
    }

    public TaxSlab getTaxSlab() {
        return this.taxSlab;
    }

    public void setTaxSlab(TaxSlab taxSlab) {
        this.taxSlab = taxSlab;
    }

    public Product taxSlab(TaxSlab taxSlab) {
        this.setTaxSlab(taxSlab);
        return this;
    }

    public Uom getUom() {
        return this.uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public Product uom(Uom uom) {
        this.setUom(uom);
        return this;
    }

    public Uom getQuantityUom() {
        return this.quantityUom;
    }

    public void setQuantityUom(Uom uom) {
        this.quantityUom = uom;
    }

    public Product quantityUom(Uom uom) {
        this.setQuantityUom(uom);
        return this;
    }

    public Uom getWeightUom() {
        return this.weightUom;
    }

    public void setWeightUom(Uom uom) {
        this.weightUom = uom;
    }

    public Product weightUom(Uom uom) {
        this.setWeightUom(uom);
        return this;
    }

    public Uom getSizeUom() {
        return this.sizeUom;
    }

    public void setSizeUom(Uom uom) {
        this.sizeUom = uom;
    }

    public Product sizeUom(Uom uom) {
        this.setSizeUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", internalName='" + getInternalName() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", variant='" + getVariant() + "'" +
            ", sku='" + getSku() + "'" +
            ", introductionDate='" + getIntroductionDate() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", quantityIncluded=" + getQuantityIncluded() +
            ", piecesIncluded=" + getPiecesIncluded() +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            ", depth=" + getDepth() +
            ", smallImageUrl='" + getSmallImageUrl() + "'" +
            ", mediumImageUrl='" + getMediumImageUrl() + "'" +
            ", largeImageUrl='" + getLargeImageUrl() + "'" +
            ", detailImageUrl='" + getDetailImageUrl() + "'" +
            ", originalImageUrl='" + getOriginalImageUrl() + "'" +
            ", altImageUrl1='" + getAltImageUrl1() + "'" +
            ", altImageUrl2='" + getAltImageUrl2() + "'" +
            ", altImageUrl3='" + getAltImageUrl3() + "'" +
            ", altImageUrl4='" + getAltImageUrl4() + "'" +
            ", altImageUrl5='" + getAltImageUrl5() + "'" +
            ", altImageUrl6='" + getAltImageUrl6() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", cgst=" + getCgst() +
            ", igst=" + getIgst() +
            ", sgst=" + getSgst() +
            ", cgstPercentage=" + getCgstPercentage() +
            ", igstPercentage=" + getIgstPercentage() +
            ", sgstPercentage=" + getSgstPercentage() +
            ", totalPrice=" + getTotalPrice() +
            ", description='" + getDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", info='" + getInfo() + "'" +
            ", isVirtual='" + getIsVirtual() + "'" +
            ", isVariant='" + getIsVariant() + "'" +
            ", requireInventory='" + getRequireInventory() + "'" +
            ", returnable='" + getReturnable() + "'" +
            ", isPopular='" + getIsPopular() + "'" +
            ", isBanner='" + getIsBanner() + "'" +
            ", bannerUrl='" + getBannerUrl() + "'" +
            "}";
    }
}
