package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductPrice} entity. This class is used
 * in {@link com.snp.web.rest.ProductPriceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-prices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductPriceCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private BigDecimalFilter price;

    private BigDecimalFilter cgst;

    private BigDecimalFilter igst;

    private BigDecimalFilter sgst;

    private BigDecimalFilter totalPrice;

    private LongFilter productId;

    private LongFilter productPriceTypeId;

    private LongFilter productPricePurposeId;

    private LongFilter currencyUomId;

    private Boolean distinct;

    public ProductPriceCriteria() {}

    public ProductPriceCriteria(ProductPriceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.cgst = other.optionalCgst().map(BigDecimalFilter::copy).orElse(null);
        this.igst = other.optionalIgst().map(BigDecimalFilter::copy).orElse(null);
        this.sgst = other.optionalSgst().map(BigDecimalFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.productPriceTypeId = other.optionalProductPriceTypeId().map(LongFilter::copy).orElse(null);
        this.productPricePurposeId = other.optionalProductPricePurposeId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductPriceCriteria copy() {
        return new ProductPriceCriteria(this);
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

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public Optional<InstantFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new InstantFilter());
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getThruDate() {
        return thruDate;
    }

    public Optional<InstantFilter> optionalThruDate() {
        return Optional.ofNullable(thruDate);
    }

    public InstantFilter thruDate() {
        if (thruDate == null) {
            setThruDate(new InstantFilter());
        }
        return thruDate;
    }

    public void setThruDate(InstantFilter thruDate) {
        this.thruDate = thruDate;
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

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getProductPriceTypeId() {
        return productPriceTypeId;
    }

    public Optional<LongFilter> optionalProductPriceTypeId() {
        return Optional.ofNullable(productPriceTypeId);
    }

    public LongFilter productPriceTypeId() {
        if (productPriceTypeId == null) {
            setProductPriceTypeId(new LongFilter());
        }
        return productPriceTypeId;
    }

    public void setProductPriceTypeId(LongFilter productPriceTypeId) {
        this.productPriceTypeId = productPriceTypeId;
    }

    public LongFilter getProductPricePurposeId() {
        return productPricePurposeId;
    }

    public Optional<LongFilter> optionalProductPricePurposeId() {
        return Optional.ofNullable(productPricePurposeId);
    }

    public LongFilter productPricePurposeId() {
        if (productPricePurposeId == null) {
            setProductPricePurposeId(new LongFilter());
        }
        return productPricePurposeId;
    }

    public void setProductPricePurposeId(LongFilter productPricePurposeId) {
        this.productPricePurposeId = productPricePurposeId;
    }

    public LongFilter getCurrencyUomId() {
        return currencyUomId;
    }

    public Optional<LongFilter> optionalCurrencyUomId() {
        return Optional.ofNullable(currencyUomId);
    }

    public LongFilter currencyUomId() {
        if (currencyUomId == null) {
            setCurrencyUomId(new LongFilter());
        }
        return currencyUomId;
    }

    public void setCurrencyUomId(LongFilter currencyUomId) {
        this.currencyUomId = currencyUomId;
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
        final ProductPriceCriteria that = (ProductPriceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(cgst, that.cgst) &&
            Objects.equals(igst, that.igst) &&
            Objects.equals(sgst, that.sgst) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(productPriceTypeId, that.productPriceTypeId) &&
            Objects.equals(productPricePurposeId, that.productPricePurposeId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fromDate,
            thruDate,
            price,
            cgst,
            igst,
            sgst,
            totalPrice,
            productId,
            productPriceTypeId,
            productPricePurposeId,
            currencyUomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductPriceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalCgst().map(f -> "cgst=" + f + ", ").orElse("") +
            optionalIgst().map(f -> "igst=" + f + ", ").orElse("") +
            optionalSgst().map(f -> "sgst=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalProductPriceTypeId().map(f -> "productPriceTypeId=" + f + ", ").orElse("") +
            optionalProductPricePurposeId().map(f -> "productPricePurposeId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
