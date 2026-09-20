package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.SupplierProduct} entity. This class is used
 * in {@link com.snp.web.rest.SupplierProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplier-products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierProductCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private StringFilter supplierProductId;

    private BigDecimalFilter minOrderQty;

    private BigDecimalFilter lastPrice;

    private BigDecimalFilter shippingPrice;

    private BigDecimalFilter cgst;

    private BigDecimalFilter igst;

    private BigDecimalFilter sgst;

    private BigDecimalFilter totalPrice;

    private StringFilter comments;

    private IntegerFilter leadTimeDays;

    private BigDecimalFilter unitsIncluded;

    private BooleanFilter canDropShip;

    private LongFilter productId;

    private LongFilter partyId;

    private LongFilter quantityUomId;

    private LongFilter currencyUomId;

    private Boolean distinct;

    public SupplierProductCriteria() {}

    public SupplierProductCriteria(SupplierProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.supplierProductId = other.optionalSupplierProductId().map(StringFilter::copy).orElse(null);
        this.minOrderQty = other.optionalMinOrderQty().map(BigDecimalFilter::copy).orElse(null);
        this.lastPrice = other.optionalLastPrice().map(BigDecimalFilter::copy).orElse(null);
        this.shippingPrice = other.optionalShippingPrice().map(BigDecimalFilter::copy).orElse(null);
        this.cgst = other.optionalCgst().map(BigDecimalFilter::copy).orElse(null);
        this.igst = other.optionalIgst().map(BigDecimalFilter::copy).orElse(null);
        this.sgst = other.optionalSgst().map(BigDecimalFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.leadTimeDays = other.optionalLeadTimeDays().map(IntegerFilter::copy).orElse(null);
        this.unitsIncluded = other.optionalUnitsIncluded().map(BigDecimalFilter::copy).orElse(null);
        this.canDropShip = other.optionalCanDropShip().map(BooleanFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.quantityUomId = other.optionalQuantityUomId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SupplierProductCriteria copy() {
        return new SupplierProductCriteria(this);
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

    public StringFilter getSupplierProductId() {
        return supplierProductId;
    }

    public Optional<StringFilter> optionalSupplierProductId() {
        return Optional.ofNullable(supplierProductId);
    }

    public StringFilter supplierProductId() {
        if (supplierProductId == null) {
            setSupplierProductId(new StringFilter());
        }
        return supplierProductId;
    }

    public void setSupplierProductId(StringFilter supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public BigDecimalFilter getMinOrderQty() {
        return minOrderQty;
    }

    public Optional<BigDecimalFilter> optionalMinOrderQty() {
        return Optional.ofNullable(minOrderQty);
    }

    public BigDecimalFilter minOrderQty() {
        if (minOrderQty == null) {
            setMinOrderQty(new BigDecimalFilter());
        }
        return minOrderQty;
    }

    public void setMinOrderQty(BigDecimalFilter minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public BigDecimalFilter getLastPrice() {
        return lastPrice;
    }

    public Optional<BigDecimalFilter> optionalLastPrice() {
        return Optional.ofNullable(lastPrice);
    }

    public BigDecimalFilter lastPrice() {
        if (lastPrice == null) {
            setLastPrice(new BigDecimalFilter());
        }
        return lastPrice;
    }

    public void setLastPrice(BigDecimalFilter lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimalFilter getShippingPrice() {
        return shippingPrice;
    }

    public Optional<BigDecimalFilter> optionalShippingPrice() {
        return Optional.ofNullable(shippingPrice);
    }

    public BigDecimalFilter shippingPrice() {
        if (shippingPrice == null) {
            setShippingPrice(new BigDecimalFilter());
        }
        return shippingPrice;
    }

    public void setShippingPrice(BigDecimalFilter shippingPrice) {
        this.shippingPrice = shippingPrice;
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

    public StringFilter getComments() {
        return comments;
    }

    public Optional<StringFilter> optionalComments() {
        return Optional.ofNullable(comments);
    }

    public StringFilter comments() {
        if (comments == null) {
            setComments(new StringFilter());
        }
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public IntegerFilter getLeadTimeDays() {
        return leadTimeDays;
    }

    public Optional<IntegerFilter> optionalLeadTimeDays() {
        return Optional.ofNullable(leadTimeDays);
    }

    public IntegerFilter leadTimeDays() {
        if (leadTimeDays == null) {
            setLeadTimeDays(new IntegerFilter());
        }
        return leadTimeDays;
    }

    public void setLeadTimeDays(IntegerFilter leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }

    public BigDecimalFilter getUnitsIncluded() {
        return unitsIncluded;
    }

    public Optional<BigDecimalFilter> optionalUnitsIncluded() {
        return Optional.ofNullable(unitsIncluded);
    }

    public BigDecimalFilter unitsIncluded() {
        if (unitsIncluded == null) {
            setUnitsIncluded(new BigDecimalFilter());
        }
        return unitsIncluded;
    }

    public void setUnitsIncluded(BigDecimalFilter unitsIncluded) {
        this.unitsIncluded = unitsIncluded;
    }

    public BooleanFilter getCanDropShip() {
        return canDropShip;
    }

    public Optional<BooleanFilter> optionalCanDropShip() {
        return Optional.ofNullable(canDropShip);
    }

    public BooleanFilter canDropShip() {
        if (canDropShip == null) {
            setCanDropShip(new BooleanFilter());
        }
        return canDropShip;
    }

    public void setCanDropShip(BooleanFilter canDropShip) {
        this.canDropShip = canDropShip;
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

    public LongFilter getPartyId() {
        return partyId;
    }

    public Optional<LongFilter> optionalPartyId() {
        return Optional.ofNullable(partyId);
    }

    public LongFilter partyId() {
        if (partyId == null) {
            setPartyId(new LongFilter());
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
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
        final SupplierProductCriteria that = (SupplierProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(supplierProductId, that.supplierProductId) &&
            Objects.equals(minOrderQty, that.minOrderQty) &&
            Objects.equals(lastPrice, that.lastPrice) &&
            Objects.equals(shippingPrice, that.shippingPrice) &&
            Objects.equals(cgst, that.cgst) &&
            Objects.equals(igst, that.igst) &&
            Objects.equals(sgst, that.sgst) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(leadTimeDays, that.leadTimeDays) &&
            Objects.equals(unitsIncluded, that.unitsIncluded) &&
            Objects.equals(canDropShip, that.canDropShip) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(quantityUomId, that.quantityUomId) &&
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
            supplierProductId,
            minOrderQty,
            lastPrice,
            shippingPrice,
            cgst,
            igst,
            sgst,
            totalPrice,
            comments,
            leadTimeDays,
            unitsIncluded,
            canDropShip,
            productId,
            partyId,
            quantityUomId,
            currencyUomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalSupplierProductId().map(f -> "supplierProductId=" + f + ", ").orElse("") +
            optionalMinOrderQty().map(f -> "minOrderQty=" + f + ", ").orElse("") +
            optionalLastPrice().map(f -> "lastPrice=" + f + ", ").orElse("") +
            optionalShippingPrice().map(f -> "shippingPrice=" + f + ", ").orElse("") +
            optionalCgst().map(f -> "cgst=" + f + ", ").orElse("") +
            optionalIgst().map(f -> "igst=" + f + ", ").orElse("") +
            optionalSgst().map(f -> "sgst=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalLeadTimeDays().map(f -> "leadTimeDays=" + f + ", ").orElse("") +
            optionalUnitsIncluded().map(f -> "unitsIncluded=" + f + ", ").orElse("") +
            optionalCanDropShip().map(f -> "canDropShip=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalQuantityUomId().map(f -> "quantityUomId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
