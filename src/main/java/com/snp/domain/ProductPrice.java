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
 * A ProductPrice.
 */
@Entity
@Table(name = "product_price")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductPrice implements Serializable {

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

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "cgst", precision = 21, scale = 2)
    private BigDecimal cgst;

    @Column(name = "igst", precision = 21, scale = 2)
    private BigDecimal igst;

    @Column(name = "sgst", precision = 21, scale = 2)
    private BigDecimal sgst;

    @Column(name = "total_price", precision = 21, scale = 2)
    private BigDecimal totalPrice;

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
    private ProductPriceType productPriceType;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductPricePurpose productPricePurpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductPrice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public ProductPrice fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return this.thruDate;
    }

    public ProductPrice thruDate(Instant thruDate) {
        this.setThruDate(thruDate);
        return this;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ProductPrice price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCgst() {
        return this.cgst;
    }

    public ProductPrice cgst(BigDecimal cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst;
    }

    public BigDecimal getIgst() {
        return this.igst;
    }

    public ProductPrice igst(BigDecimal igst) {
        this.setIgst(igst);
        return this;
    }

    public void setIgst(BigDecimal igst) {
        this.igst = igst;
    }

    public BigDecimal getSgst() {
        return this.sgst;
    }

    public ProductPrice sgst(BigDecimal sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public ProductPrice totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductPrice product(Product product) {
        this.setProduct(product);
        return this;
    }

    public ProductPriceType getProductPriceType() {
        return this.productPriceType;
    }

    public void setProductPriceType(ProductPriceType productPriceType) {
        this.productPriceType = productPriceType;
    }

    public ProductPrice productPriceType(ProductPriceType productPriceType) {
        this.setProductPriceType(productPriceType);
        return this;
    }

    public ProductPricePurpose getProductPricePurpose() {
        return this.productPricePurpose;
    }

    public void setProductPricePurpose(ProductPricePurpose productPricePurpose) {
        this.productPricePurpose = productPricePurpose;
    }

    public ProductPrice productPricePurpose(ProductPricePurpose productPricePurpose) {
        this.setProductPricePurpose(productPricePurpose);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public ProductPrice currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductPrice)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductPrice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductPrice{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", thruDate='" + getThruDate() + "'" +
            ", price=" + getPrice() +
            ", cgst=" + getCgst() +
            ", igst=" + getIgst() +
            ", sgst=" + getSgst() +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
