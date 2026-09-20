package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Product} entity. This class is used
 * in {@link com.snp.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter internalName;

    private StringFilter brandName;

    private StringFilter variant;

    private StringFilter sku;

    private InstantFilter introductionDate;

    private InstantFilter releaseDate;

    private BigDecimalFilter quantityIncluded;

    private IntegerFilter piecesIncluded;

    private BigDecimalFilter weight;

    private BigDecimalFilter height;

    private BigDecimalFilter width;

    private BigDecimalFilter depth;

    private StringFilter smallImageUrl;

    private StringFilter mediumImageUrl;

    private StringFilter largeImageUrl;

    private StringFilter detailImageUrl;

    private StringFilter originalImageUrl;

    private StringFilter altImageUrl1;

    private StringFilter altImageUrl2;

    private StringFilter altImageUrl3;

    private StringFilter altImageUrl4;

    private StringFilter altImageUrl5;

    private StringFilter altImageUrl6;

    private StringFilter videoUrl;

    private BigDecimalFilter quantity;

    private BigDecimalFilter price;

    private BigDecimalFilter cgst;

    private BigDecimalFilter igst;

    private BigDecimalFilter sgst;

    private BigDecimalFilter cgstPercentage;

    private BigDecimalFilter igstPercentage;

    private BigDecimalFilter sgstPercentage;

    private BigDecimalFilter totalPrice;

    private StringFilter description;

    private StringFilter longDescription;

    private BooleanFilter isVirtual;

    private BooleanFilter isVariant;

    private BooleanFilter requireInventory;

    private BooleanFilter returnable;

    private BooleanFilter isPopular;

    private BooleanFilter isBanner;

    private StringFilter bannerUrl;

    private LongFilter productTypeId;

    private LongFilter primaryProductCategoryId;

    private LongFilter virtualProductId;

    private LongFilter inventoryItemTypeId;

    private LongFilter taxSlabId;

    private LongFilter uomId;

    private LongFilter quantityUomId;

    private LongFilter weightUomId;

    private LongFilter sizeUomId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.internalName = other.optionalInternalName().map(StringFilter::copy).orElse(null);
        this.brandName = other.optionalBrandName().map(StringFilter::copy).orElse(null);
        this.variant = other.optionalVariant().map(StringFilter::copy).orElse(null);
        this.sku = other.optionalSku().map(StringFilter::copy).orElse(null);
        this.introductionDate = other.optionalIntroductionDate().map(InstantFilter::copy).orElse(null);
        this.releaseDate = other.optionalReleaseDate().map(InstantFilter::copy).orElse(null);
        this.quantityIncluded = other.optionalQuantityIncluded().map(BigDecimalFilter::copy).orElse(null);
        this.piecesIncluded = other.optionalPiecesIncluded().map(IntegerFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(BigDecimalFilter::copy).orElse(null);
        this.height = other.optionalHeight().map(BigDecimalFilter::copy).orElse(null);
        this.width = other.optionalWidth().map(BigDecimalFilter::copy).orElse(null);
        this.depth = other.optionalDepth().map(BigDecimalFilter::copy).orElse(null);
        this.smallImageUrl = other.optionalSmallImageUrl().map(StringFilter::copy).orElse(null);
        this.mediumImageUrl = other.optionalMediumImageUrl().map(StringFilter::copy).orElse(null);
        this.largeImageUrl = other.optionalLargeImageUrl().map(StringFilter::copy).orElse(null);
        this.detailImageUrl = other.optionalDetailImageUrl().map(StringFilter::copy).orElse(null);
        this.originalImageUrl = other.optionalOriginalImageUrl().map(StringFilter::copy).orElse(null);
        this.altImageUrl1 = other.optionalAltImageUrl1().map(StringFilter::copy).orElse(null);
        this.altImageUrl2 = other.optionalAltImageUrl2().map(StringFilter::copy).orElse(null);
        this.altImageUrl3 = other.optionalAltImageUrl3().map(StringFilter::copy).orElse(null);
        this.altImageUrl4 = other.optionalAltImageUrl4().map(StringFilter::copy).orElse(null);
        this.altImageUrl5 = other.optionalAltImageUrl5().map(StringFilter::copy).orElse(null);
        this.altImageUrl6 = other.optionalAltImageUrl6().map(StringFilter::copy).orElse(null);
        this.videoUrl = other.optionalVideoUrl().map(StringFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.cgst = other.optionalCgst().map(BigDecimalFilter::copy).orElse(null);
        this.igst = other.optionalIgst().map(BigDecimalFilter::copy).orElse(null);
        this.sgst = other.optionalSgst().map(BigDecimalFilter::copy).orElse(null);
        this.cgstPercentage = other.optionalCgstPercentage().map(BigDecimalFilter::copy).orElse(null);
        this.igstPercentage = other.optionalIgstPercentage().map(BigDecimalFilter::copy).orElse(null);
        this.sgstPercentage = other.optionalSgstPercentage().map(BigDecimalFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.longDescription = other.optionalLongDescription().map(StringFilter::copy).orElse(null);
        this.isVirtual = other.optionalIsVirtual().map(BooleanFilter::copy).orElse(null);
        this.isVariant = other.optionalIsVariant().map(BooleanFilter::copy).orElse(null);
        this.requireInventory = other.optionalRequireInventory().map(BooleanFilter::copy).orElse(null);
        this.returnable = other.optionalReturnable().map(BooleanFilter::copy).orElse(null);
        this.isPopular = other.optionalIsPopular().map(BooleanFilter::copy).orElse(null);
        this.isBanner = other.optionalIsBanner().map(BooleanFilter::copy).orElse(null);
        this.bannerUrl = other.optionalBannerUrl().map(StringFilter::copy).orElse(null);
        this.productTypeId = other.optionalProductTypeId().map(LongFilter::copy).orElse(null);
        this.primaryProductCategoryId = other.optionalPrimaryProductCategoryId().map(LongFilter::copy).orElse(null);
        this.virtualProductId = other.optionalVirtualProductId().map(LongFilter::copy).orElse(null);
        this.inventoryItemTypeId = other.optionalInventoryItemTypeId().map(LongFilter::copy).orElse(null);
        this.taxSlabId = other.optionalTaxSlabId().map(LongFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(LongFilter::copy).orElse(null);
        this.quantityUomId = other.optionalQuantityUomId().map(LongFilter::copy).orElse(null);
        this.weightUomId = other.optionalWeightUomId().map(LongFilter::copy).orElse(null);
        this.sizeUomId = other.optionalSizeUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getInternalName() {
        return internalName;
    }

    public Optional<StringFilter> optionalInternalName() {
        return Optional.ofNullable(internalName);
    }

    public StringFilter internalName() {
        if (internalName == null) {
            setInternalName(new StringFilter());
        }
        return internalName;
    }

    public void setInternalName(StringFilter internalName) {
        this.internalName = internalName;
    }

    public StringFilter getBrandName() {
        return brandName;
    }

    public Optional<StringFilter> optionalBrandName() {
        return Optional.ofNullable(brandName);
    }

    public StringFilter brandName() {
        if (brandName == null) {
            setBrandName(new StringFilter());
        }
        return brandName;
    }

    public void setBrandName(StringFilter brandName) {
        this.brandName = brandName;
    }

    public StringFilter getVariant() {
        return variant;
    }

    public Optional<StringFilter> optionalVariant() {
        return Optional.ofNullable(variant);
    }

    public StringFilter variant() {
        if (variant == null) {
            setVariant(new StringFilter());
        }
        return variant;
    }

    public void setVariant(StringFilter variant) {
        this.variant = variant;
    }

    public StringFilter getSku() {
        return sku;
    }

    public Optional<StringFilter> optionalSku() {
        return Optional.ofNullable(sku);
    }

    public StringFilter sku() {
        if (sku == null) {
            setSku(new StringFilter());
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public InstantFilter getIntroductionDate() {
        return introductionDate;
    }

    public Optional<InstantFilter> optionalIntroductionDate() {
        return Optional.ofNullable(introductionDate);
    }

    public InstantFilter introductionDate() {
        if (introductionDate == null) {
            setIntroductionDate(new InstantFilter());
        }
        return introductionDate;
    }

    public void setIntroductionDate(InstantFilter introductionDate) {
        this.introductionDate = introductionDate;
    }

    public InstantFilter getReleaseDate() {
        return releaseDate;
    }

    public Optional<InstantFilter> optionalReleaseDate() {
        return Optional.ofNullable(releaseDate);
    }

    public InstantFilter releaseDate() {
        if (releaseDate == null) {
            setReleaseDate(new InstantFilter());
        }
        return releaseDate;
    }

    public void setReleaseDate(InstantFilter releaseDate) {
        this.releaseDate = releaseDate;
    }

    public BigDecimalFilter getQuantityIncluded() {
        return quantityIncluded;
    }

    public Optional<BigDecimalFilter> optionalQuantityIncluded() {
        return Optional.ofNullable(quantityIncluded);
    }

    public BigDecimalFilter quantityIncluded() {
        if (quantityIncluded == null) {
            setQuantityIncluded(new BigDecimalFilter());
        }
        return quantityIncluded;
    }

    public void setQuantityIncluded(BigDecimalFilter quantityIncluded) {
        this.quantityIncluded = quantityIncluded;
    }

    public IntegerFilter getPiecesIncluded() {
        return piecesIncluded;
    }

    public Optional<IntegerFilter> optionalPiecesIncluded() {
        return Optional.ofNullable(piecesIncluded);
    }

    public IntegerFilter piecesIncluded() {
        if (piecesIncluded == null) {
            setPiecesIncluded(new IntegerFilter());
        }
        return piecesIncluded;
    }

    public void setPiecesIncluded(IntegerFilter piecesIncluded) {
        this.piecesIncluded = piecesIncluded;
    }

    public BigDecimalFilter getWeight() {
        return weight;
    }

    public Optional<BigDecimalFilter> optionalWeight() {
        return Optional.ofNullable(weight);
    }

    public BigDecimalFilter weight() {
        if (weight == null) {
            setWeight(new BigDecimalFilter());
        }
        return weight;
    }

    public void setWeight(BigDecimalFilter weight) {
        this.weight = weight;
    }

    public BigDecimalFilter getHeight() {
        return height;
    }

    public Optional<BigDecimalFilter> optionalHeight() {
        return Optional.ofNullable(height);
    }

    public BigDecimalFilter height() {
        if (height == null) {
            setHeight(new BigDecimalFilter());
        }
        return height;
    }

    public void setHeight(BigDecimalFilter height) {
        this.height = height;
    }

    public BigDecimalFilter getWidth() {
        return width;
    }

    public Optional<BigDecimalFilter> optionalWidth() {
        return Optional.ofNullable(width);
    }

    public BigDecimalFilter width() {
        if (width == null) {
            setWidth(new BigDecimalFilter());
        }
        return width;
    }

    public void setWidth(BigDecimalFilter width) {
        this.width = width;
    }

    public BigDecimalFilter getDepth() {
        return depth;
    }

    public Optional<BigDecimalFilter> optionalDepth() {
        return Optional.ofNullable(depth);
    }

    public BigDecimalFilter depth() {
        if (depth == null) {
            setDepth(new BigDecimalFilter());
        }
        return depth;
    }

    public void setDepth(BigDecimalFilter depth) {
        this.depth = depth;
    }

    public StringFilter getSmallImageUrl() {
        return smallImageUrl;
    }

    public Optional<StringFilter> optionalSmallImageUrl() {
        return Optional.ofNullable(smallImageUrl);
    }

    public StringFilter smallImageUrl() {
        if (smallImageUrl == null) {
            setSmallImageUrl(new StringFilter());
        }
        return smallImageUrl;
    }

    public void setSmallImageUrl(StringFilter smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public StringFilter getMediumImageUrl() {
        return mediumImageUrl;
    }

    public Optional<StringFilter> optionalMediumImageUrl() {
        return Optional.ofNullable(mediumImageUrl);
    }

    public StringFilter mediumImageUrl() {
        if (mediumImageUrl == null) {
            setMediumImageUrl(new StringFilter());
        }
        return mediumImageUrl;
    }

    public void setMediumImageUrl(StringFilter mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }

    public StringFilter getLargeImageUrl() {
        return largeImageUrl;
    }

    public Optional<StringFilter> optionalLargeImageUrl() {
        return Optional.ofNullable(largeImageUrl);
    }

    public StringFilter largeImageUrl() {
        if (largeImageUrl == null) {
            setLargeImageUrl(new StringFilter());
        }
        return largeImageUrl;
    }

    public void setLargeImageUrl(StringFilter largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public StringFilter getDetailImageUrl() {
        return detailImageUrl;
    }

    public Optional<StringFilter> optionalDetailImageUrl() {
        return Optional.ofNullable(detailImageUrl);
    }

    public StringFilter detailImageUrl() {
        if (detailImageUrl == null) {
            setDetailImageUrl(new StringFilter());
        }
        return detailImageUrl;
    }

    public void setDetailImageUrl(StringFilter detailImageUrl) {
        this.detailImageUrl = detailImageUrl;
    }

    public StringFilter getOriginalImageUrl() {
        return originalImageUrl;
    }

    public Optional<StringFilter> optionalOriginalImageUrl() {
        return Optional.ofNullable(originalImageUrl);
    }

    public StringFilter originalImageUrl() {
        if (originalImageUrl == null) {
            setOriginalImageUrl(new StringFilter());
        }
        return originalImageUrl;
    }

    public void setOriginalImageUrl(StringFilter originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public StringFilter getAltImageUrl1() {
        return altImageUrl1;
    }

    public Optional<StringFilter> optionalAltImageUrl1() {
        return Optional.ofNullable(altImageUrl1);
    }

    public StringFilter altImageUrl1() {
        if (altImageUrl1 == null) {
            setAltImageUrl1(new StringFilter());
        }
        return altImageUrl1;
    }

    public void setAltImageUrl1(StringFilter altImageUrl1) {
        this.altImageUrl1 = altImageUrl1;
    }

    public StringFilter getAltImageUrl2() {
        return altImageUrl2;
    }

    public Optional<StringFilter> optionalAltImageUrl2() {
        return Optional.ofNullable(altImageUrl2);
    }

    public StringFilter altImageUrl2() {
        if (altImageUrl2 == null) {
            setAltImageUrl2(new StringFilter());
        }
        return altImageUrl2;
    }

    public void setAltImageUrl2(StringFilter altImageUrl2) {
        this.altImageUrl2 = altImageUrl2;
    }

    public StringFilter getAltImageUrl3() {
        return altImageUrl3;
    }

    public Optional<StringFilter> optionalAltImageUrl3() {
        return Optional.ofNullable(altImageUrl3);
    }

    public StringFilter altImageUrl3() {
        if (altImageUrl3 == null) {
            setAltImageUrl3(new StringFilter());
        }
        return altImageUrl3;
    }

    public void setAltImageUrl3(StringFilter altImageUrl3) {
        this.altImageUrl3 = altImageUrl3;
    }

    public StringFilter getAltImageUrl4() {
        return altImageUrl4;
    }

    public Optional<StringFilter> optionalAltImageUrl4() {
        return Optional.ofNullable(altImageUrl4);
    }

    public StringFilter altImageUrl4() {
        if (altImageUrl4 == null) {
            setAltImageUrl4(new StringFilter());
        }
        return altImageUrl4;
    }

    public void setAltImageUrl4(StringFilter altImageUrl4) {
        this.altImageUrl4 = altImageUrl4;
    }

    public StringFilter getAltImageUrl5() {
        return altImageUrl5;
    }

    public Optional<StringFilter> optionalAltImageUrl5() {
        return Optional.ofNullable(altImageUrl5);
    }

    public StringFilter altImageUrl5() {
        if (altImageUrl5 == null) {
            setAltImageUrl5(new StringFilter());
        }
        return altImageUrl5;
    }

    public void setAltImageUrl5(StringFilter altImageUrl5) {
        this.altImageUrl5 = altImageUrl5;
    }

    public StringFilter getAltImageUrl6() {
        return altImageUrl6;
    }

    public Optional<StringFilter> optionalAltImageUrl6() {
        return Optional.ofNullable(altImageUrl6);
    }

    public StringFilter altImageUrl6() {
        if (altImageUrl6 == null) {
            setAltImageUrl6(new StringFilter());
        }
        return altImageUrl6;
    }

    public void setAltImageUrl6(StringFilter altImageUrl6) {
        this.altImageUrl6 = altImageUrl6;
    }

    public StringFilter getVideoUrl() {
        return videoUrl;
    }

    public Optional<StringFilter> optionalVideoUrl() {
        return Optional.ofNullable(videoUrl);
    }

    public StringFilter videoUrl() {
        if (videoUrl == null) {
            setVideoUrl(new StringFilter());
        }
        return videoUrl;
    }

    public void setVideoUrl(StringFilter videoUrl) {
        this.videoUrl = videoUrl;
    }

    public BigDecimalFilter getQuantity() {
        return quantity;
    }

    public Optional<BigDecimalFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public BigDecimalFilter quantity() {
        if (quantity == null) {
            setQuantity(new BigDecimalFilter());
        }
        return quantity;
    }

    public void setQuantity(BigDecimalFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BigDecimalFilter getCgst() {
        return cgst;
    }

    public Optional<BigDecimalFilter> optionalCgst() {
        return Optional.ofNullable(cgst);
    }

    public BigDecimalFilter cgst() {
        if (cgst == null) {
            setCgst(new BigDecimalFilter());
        }
        return cgst;
    }

    public void setCgst(BigDecimalFilter cgst) {
        this.cgst = cgst;
    }

    public BigDecimalFilter getIgst() {
        return igst;
    }

    public Optional<BigDecimalFilter> optionalIgst() {
        return Optional.ofNullable(igst);
    }

    public BigDecimalFilter igst() {
        if (igst == null) {
            setIgst(new BigDecimalFilter());
        }
        return igst;
    }

    public void setIgst(BigDecimalFilter igst) {
        this.igst = igst;
    }

    public BigDecimalFilter getSgst() {
        return sgst;
    }

    public Optional<BigDecimalFilter> optionalSgst() {
        return Optional.ofNullable(sgst);
    }

    public BigDecimalFilter sgst() {
        if (sgst == null) {
            setSgst(new BigDecimalFilter());
        }
        return sgst;
    }

    public void setSgst(BigDecimalFilter sgst) {
        this.sgst = sgst;
    }

    public BigDecimalFilter getCgstPercentage() {
        return cgstPercentage;
    }

    public Optional<BigDecimalFilter> optionalCgstPercentage() {
        return Optional.ofNullable(cgstPercentage);
    }

    public BigDecimalFilter cgstPercentage() {
        if (cgstPercentage == null) {
            setCgstPercentage(new BigDecimalFilter());
        }
        return cgstPercentage;
    }

    public void setCgstPercentage(BigDecimalFilter cgstPercentage) {
        this.cgstPercentage = cgstPercentage;
    }

    public BigDecimalFilter getIgstPercentage() {
        return igstPercentage;
    }

    public Optional<BigDecimalFilter> optionalIgstPercentage() {
        return Optional.ofNullable(igstPercentage);
    }

    public BigDecimalFilter igstPercentage() {
        if (igstPercentage == null) {
            setIgstPercentage(new BigDecimalFilter());
        }
        return igstPercentage;
    }

    public void setIgstPercentage(BigDecimalFilter igstPercentage) {
        this.igstPercentage = igstPercentage;
    }

    public BigDecimalFilter getSgstPercentage() {
        return sgstPercentage;
    }

    public Optional<BigDecimalFilter> optionalSgstPercentage() {
        return Optional.ofNullable(sgstPercentage);
    }

    public BigDecimalFilter sgstPercentage() {
        if (sgstPercentage == null) {
            setSgstPercentage(new BigDecimalFilter());
        }
        return sgstPercentage;
    }

    public void setSgstPercentage(BigDecimalFilter sgstPercentage) {
        this.sgstPercentage = sgstPercentage;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public Optional<BigDecimalFilter> optionalTotalPrice() {
        return Optional.ofNullable(totalPrice);
    }

    public BigDecimalFilter totalPrice() {
        if (totalPrice == null) {
            setTotalPrice(new BigDecimalFilter());
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLongDescription() {
        return longDescription;
    }

    public Optional<StringFilter> optionalLongDescription() {
        return Optional.ofNullable(longDescription);
    }

    public StringFilter longDescription() {
        if (longDescription == null) {
            setLongDescription(new StringFilter());
        }
        return longDescription;
    }

    public void setLongDescription(StringFilter longDescription) {
        this.longDescription = longDescription;
    }

    public BooleanFilter getIsVirtual() {
        return isVirtual;
    }

    public Optional<BooleanFilter> optionalIsVirtual() {
        return Optional.ofNullable(isVirtual);
    }

    public BooleanFilter isVirtual() {
        if (isVirtual == null) {
            setIsVirtual(new BooleanFilter());
        }
        return isVirtual;
    }

    public void setIsVirtual(BooleanFilter isVirtual) {
        this.isVirtual = isVirtual;
    }

    public BooleanFilter getIsVariant() {
        return isVariant;
    }

    public Optional<BooleanFilter> optionalIsVariant() {
        return Optional.ofNullable(isVariant);
    }

    public BooleanFilter isVariant() {
        if (isVariant == null) {
            setIsVariant(new BooleanFilter());
        }
        return isVariant;
    }

    public void setIsVariant(BooleanFilter isVariant) {
        this.isVariant = isVariant;
    }

    public BooleanFilter getRequireInventory() {
        return requireInventory;
    }

    public Optional<BooleanFilter> optionalRequireInventory() {
        return Optional.ofNullable(requireInventory);
    }

    public BooleanFilter requireInventory() {
        if (requireInventory == null) {
            setRequireInventory(new BooleanFilter());
        }
        return requireInventory;
    }

    public void setRequireInventory(BooleanFilter requireInventory) {
        this.requireInventory = requireInventory;
    }

    public BooleanFilter getReturnable() {
        return returnable;
    }

    public Optional<BooleanFilter> optionalReturnable() {
        return Optional.ofNullable(returnable);
    }

    public BooleanFilter returnable() {
        if (returnable == null) {
            setReturnable(new BooleanFilter());
        }
        return returnable;
    }

    public void setReturnable(BooleanFilter returnable) {
        this.returnable = returnable;
    }

    public BooleanFilter getIsPopular() {
        return isPopular;
    }

    public Optional<BooleanFilter> optionalIsPopular() {
        return Optional.ofNullable(isPopular);
    }

    public BooleanFilter isPopular() {
        if (isPopular == null) {
            setIsPopular(new BooleanFilter());
        }
        return isPopular;
    }

    public void setIsPopular(BooleanFilter isPopular) {
        this.isPopular = isPopular;
    }

    public BooleanFilter getIsBanner() {
        return isBanner;
    }

    public Optional<BooleanFilter> optionalIsBanner() {
        return Optional.ofNullable(isBanner);
    }

    public BooleanFilter isBanner() {
        if (isBanner == null) {
            setIsBanner(new BooleanFilter());
        }
        return isBanner;
    }

    public void setIsBanner(BooleanFilter isBanner) {
        this.isBanner = isBanner;
    }

    public StringFilter getBannerUrl() {
        return bannerUrl;
    }

    public Optional<StringFilter> optionalBannerUrl() {
        return Optional.ofNullable(bannerUrl);
    }

    public StringFilter bannerUrl() {
        if (bannerUrl == null) {
            setBannerUrl(new StringFilter());
        }
        return bannerUrl;
    }

    public void setBannerUrl(StringFilter bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public LongFilter getProductTypeId() {
        return productTypeId;
    }

    public Optional<LongFilter> optionalProductTypeId() {
        return Optional.ofNullable(productTypeId);
    }

    public LongFilter productTypeId() {
        if (productTypeId == null) {
            setProductTypeId(new LongFilter());
        }
        return productTypeId;
    }

    public void setProductTypeId(LongFilter productTypeId) {
        this.productTypeId = productTypeId;
    }

    public LongFilter getPrimaryProductCategoryId() {
        return primaryProductCategoryId;
    }

    public Optional<LongFilter> optionalPrimaryProductCategoryId() {
        return Optional.ofNullable(primaryProductCategoryId);
    }

    public LongFilter primaryProductCategoryId() {
        if (primaryProductCategoryId == null) {
            setPrimaryProductCategoryId(new LongFilter());
        }
        return primaryProductCategoryId;
    }

    public void setPrimaryProductCategoryId(LongFilter primaryProductCategoryId) {
        this.primaryProductCategoryId = primaryProductCategoryId;
    }

    public LongFilter getVirtualProductId() {
        return virtualProductId;
    }

    public Optional<LongFilter> optionalVirtualProductId() {
        return Optional.ofNullable(virtualProductId);
    }

    public LongFilter virtualProductId() {
        if (virtualProductId == null) {
            setVirtualProductId(new LongFilter());
        }
        return virtualProductId;
    }

    public void setVirtualProductId(LongFilter virtualProductId) {
        this.virtualProductId = virtualProductId;
    }

    public LongFilter getInventoryItemTypeId() {
        return inventoryItemTypeId;
    }

    public Optional<LongFilter> optionalInventoryItemTypeId() {
        return Optional.ofNullable(inventoryItemTypeId);
    }

    public LongFilter inventoryItemTypeId() {
        if (inventoryItemTypeId == null) {
            setInventoryItemTypeId(new LongFilter());
        }
        return inventoryItemTypeId;
    }

    public void setInventoryItemTypeId(LongFilter inventoryItemTypeId) {
        this.inventoryItemTypeId = inventoryItemTypeId;
    }

    public LongFilter getTaxSlabId() {
        return taxSlabId;
    }

    public Optional<LongFilter> optionalTaxSlabId() {
        return Optional.ofNullable(taxSlabId);
    }

    public LongFilter taxSlabId() {
        if (taxSlabId == null) {
            setTaxSlabId(new LongFilter());
        }
        return taxSlabId;
    }

    public void setTaxSlabId(LongFilter taxSlabId) {
        this.taxSlabId = taxSlabId;
    }

    public LongFilter getUomId() {
        return uomId;
    }

    public Optional<LongFilter> optionalUomId() {
        return Optional.ofNullable(uomId);
    }

    public LongFilter uomId() {
        if (uomId == null) {
            setUomId(new LongFilter());
        }
        return uomId;
    }

    public void setUomId(LongFilter uomId) {
        this.uomId = uomId;
    }

    public LongFilter getQuantityUomId() {
        return quantityUomId;
    }

    public Optional<LongFilter> optionalQuantityUomId() {
        return Optional.ofNullable(quantityUomId);
    }

    public LongFilter quantityUomId() {
        if (quantityUomId == null) {
            setQuantityUomId(new LongFilter());
        }
        return quantityUomId;
    }

    public void setQuantityUomId(LongFilter quantityUomId) {
        this.quantityUomId = quantityUomId;
    }

    public LongFilter getWeightUomId() {
        return weightUomId;
    }

    public Optional<LongFilter> optionalWeightUomId() {
        return Optional.ofNullable(weightUomId);
    }

    public LongFilter weightUomId() {
        if (weightUomId == null) {
            setWeightUomId(new LongFilter());
        }
        return weightUomId;
    }

    public void setWeightUomId(LongFilter weightUomId) {
        this.weightUomId = weightUomId;
    }

    public LongFilter getSizeUomId() {
        return sizeUomId;
    }

    public Optional<LongFilter> optionalSizeUomId() {
        return Optional.ofNullable(sizeUomId);
    }

    public LongFilter sizeUomId() {
        if (sizeUomId == null) {
            setSizeUomId(new LongFilter());
        }
        return sizeUomId;
    }

    public void setSizeUomId(LongFilter sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(internalName, that.internalName) &&
            Objects.equals(brandName, that.brandName) &&
            Objects.equals(variant, that.variant) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(introductionDate, that.introductionDate) &&
            Objects.equals(releaseDate, that.releaseDate) &&
            Objects.equals(quantityIncluded, that.quantityIncluded) &&
            Objects.equals(piecesIncluded, that.piecesIncluded) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(height, that.height) &&
            Objects.equals(width, that.width) &&
            Objects.equals(depth, that.depth) &&
            Objects.equals(smallImageUrl, that.smallImageUrl) &&
            Objects.equals(mediumImageUrl, that.mediumImageUrl) &&
            Objects.equals(largeImageUrl, that.largeImageUrl) &&
            Objects.equals(detailImageUrl, that.detailImageUrl) &&
            Objects.equals(originalImageUrl, that.originalImageUrl) &&
            Objects.equals(altImageUrl1, that.altImageUrl1) &&
            Objects.equals(altImageUrl2, that.altImageUrl2) &&
            Objects.equals(altImageUrl3, that.altImageUrl3) &&
            Objects.equals(altImageUrl4, that.altImageUrl4) &&
            Objects.equals(altImageUrl5, that.altImageUrl5) &&
            Objects.equals(altImageUrl6, that.altImageUrl6) &&
            Objects.equals(videoUrl, that.videoUrl) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(price, that.price) &&
            Objects.equals(cgst, that.cgst) &&
            Objects.equals(igst, that.igst) &&
            Objects.equals(sgst, that.sgst) &&
            Objects.equals(cgstPercentage, that.cgstPercentage) &&
            Objects.equals(igstPercentage, that.igstPercentage) &&
            Objects.equals(sgstPercentage, that.sgstPercentage) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(description, that.description) &&
            Objects.equals(longDescription, that.longDescription) &&
            Objects.equals(isVirtual, that.isVirtual) &&
            Objects.equals(isVariant, that.isVariant) &&
            Objects.equals(requireInventory, that.requireInventory) &&
            Objects.equals(returnable, that.returnable) &&
            Objects.equals(isPopular, that.isPopular) &&
            Objects.equals(isBanner, that.isBanner) &&
            Objects.equals(bannerUrl, that.bannerUrl) &&
            Objects.equals(productTypeId, that.productTypeId) &&
            Objects.equals(primaryProductCategoryId, that.primaryProductCategoryId) &&
            Objects.equals(virtualProductId, that.virtualProductId) &&
            Objects.equals(inventoryItemTypeId, that.inventoryItemTypeId) &&
            Objects.equals(taxSlabId, that.taxSlabId) &&
            Objects.equals(uomId, that.uomId) &&
            Objects.equals(quantityUomId, that.quantityUomId) &&
            Objects.equals(weightUomId, that.weightUomId) &&
            Objects.equals(sizeUomId, that.sizeUomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            internalName,
            brandName,
            variant,
            sku,
            introductionDate,
            releaseDate,
            quantityIncluded,
            piecesIncluded,
            weight,
            height,
            width,
            depth,
            smallImageUrl,
            mediumImageUrl,
            largeImageUrl,
            detailImageUrl,
            originalImageUrl,
            altImageUrl1,
            altImageUrl2,
            altImageUrl3,
            altImageUrl4,
            altImageUrl5,
            altImageUrl6,
            videoUrl,
            quantity,
            price,
            cgst,
            igst,
            sgst,
            cgstPercentage,
            igstPercentage,
            sgstPercentage,
            totalPrice,
            description,
            longDescription,
            isVirtual,
            isVariant,
            requireInventory,
            returnable,
            isPopular,
            isBanner,
            bannerUrl,
            productTypeId,
            primaryProductCategoryId,
            virtualProductId,
            inventoryItemTypeId,
            taxSlabId,
            uomId,
            quantityUomId,
            weightUomId,
            sizeUomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalInternalName().map(f -> "internalName=" + f + ", ").orElse("") +
            optionalBrandName().map(f -> "brandName=" + f + ", ").orElse("") +
            optionalVariant().map(f -> "variant=" + f + ", ").orElse("") +
            optionalSku().map(f -> "sku=" + f + ", ").orElse("") +
            optionalIntroductionDate().map(f -> "introductionDate=" + f + ", ").orElse("") +
            optionalReleaseDate().map(f -> "releaseDate=" + f + ", ").orElse("") +
            optionalQuantityIncluded().map(f -> "quantityIncluded=" + f + ", ").orElse("") +
            optionalPiecesIncluded().map(f -> "piecesIncluded=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalHeight().map(f -> "height=" + f + ", ").orElse("") +
            optionalWidth().map(f -> "width=" + f + ", ").orElse("") +
            optionalDepth().map(f -> "depth=" + f + ", ").orElse("") +
            optionalSmallImageUrl().map(f -> "smallImageUrl=" + f + ", ").orElse("") +
            optionalMediumImageUrl().map(f -> "mediumImageUrl=" + f + ", ").orElse("") +
            optionalLargeImageUrl().map(f -> "largeImageUrl=" + f + ", ").orElse("") +
            optionalDetailImageUrl().map(f -> "detailImageUrl=" + f + ", ").orElse("") +
            optionalOriginalImageUrl().map(f -> "originalImageUrl=" + f + ", ").orElse("") +
            optionalAltImageUrl1().map(f -> "altImageUrl1=" + f + ", ").orElse("") +
            optionalAltImageUrl2().map(f -> "altImageUrl2=" + f + ", ").orElse("") +
            optionalAltImageUrl3().map(f -> "altImageUrl3=" + f + ", ").orElse("") +
            optionalAltImageUrl4().map(f -> "altImageUrl4=" + f + ", ").orElse("") +
            optionalAltImageUrl5().map(f -> "altImageUrl5=" + f + ", ").orElse("") +
            optionalAltImageUrl6().map(f -> "altImageUrl6=" + f + ", ").orElse("") +
            optionalVideoUrl().map(f -> "videoUrl=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalCgst().map(f -> "cgst=" + f + ", ").orElse("") +
            optionalIgst().map(f -> "igst=" + f + ", ").orElse("") +
            optionalSgst().map(f -> "sgst=" + f + ", ").orElse("") +
            optionalCgstPercentage().map(f -> "cgstPercentage=" + f + ", ").orElse("") +
            optionalIgstPercentage().map(f -> "igstPercentage=" + f + ", ").orElse("") +
            optionalSgstPercentage().map(f -> "sgstPercentage=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalLongDescription().map(f -> "longDescription=" + f + ", ").orElse("") +
            optionalIsVirtual().map(f -> "isVirtual=" + f + ", ").orElse("") +
            optionalIsVariant().map(f -> "isVariant=" + f + ", ").orElse("") +
            optionalRequireInventory().map(f -> "requireInventory=" + f + ", ").orElse("") +
            optionalReturnable().map(f -> "returnable=" + f + ", ").orElse("") +
            optionalIsPopular().map(f -> "isPopular=" + f + ", ").orElse("") +
            optionalIsBanner().map(f -> "isBanner=" + f + ", ").orElse("") +
            optionalBannerUrl().map(f -> "bannerUrl=" + f + ", ").orElse("") +
            optionalProductTypeId().map(f -> "productTypeId=" + f + ", ").orElse("") +
            optionalPrimaryProductCategoryId().map(f -> "primaryProductCategoryId=" + f + ", ").orElse("") +
            optionalVirtualProductId().map(f -> "virtualProductId=" + f + ", ").orElse("") +
            optionalInventoryItemTypeId().map(f -> "inventoryItemTypeId=" + f + ", ").orElse("") +
            optionalTaxSlabId().map(f -> "taxSlabId=" + f + ", ").orElse("") +
            optionalUomId().map(f -> "uomId=" + f + ", ").orElse("") +
            optionalQuantityUomId().map(f -> "quantityUomId=" + f + ", ").orElse("") +
            optionalWeightUomId().map(f -> "weightUomId=" + f + ", ").orElse("") +
            optionalSizeUomId().map(f -> "sizeUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
