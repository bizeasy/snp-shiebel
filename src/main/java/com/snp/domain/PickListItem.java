package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PickListItem.
 */
@Entity
@Table(name = "pick_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PickListItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "facility", "status", "party" }, allowSetters = true)
    private PickList pickList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "orderType",
            "salesChannel",
            "party",
            "supplier",
            "status",
            "currencyUom",
            "currencyConversion",
            "quote",
            "facility",
            "productStore",
            "department",
            "customTimePeriod",
        },
        allowSetters = true
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "order",
            "orderItemType",
            "product",
            "uom",
            "supplierProduct",
            "supplier",
            "productFeature",
            "productFacility",
            "fromInventoryItem",
            "shoppingListItem",
            "quoteItem",
            "status",
            "taxAuthRate",
        },
        allowSetters = true
    )
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "inventoryItemType",
            "product",
            "supplier",
            "ownerParty",
            "status",
            "facility",
            "facilityLocation",
            "uom",
            "currencyUom",
            "lot",
        },
        allowSetters = true
    )
    private InventoryItem inventoryItem;

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
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facility", "locationType" }, allowSetters = true)
    private FacilityLocation facilityLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PickListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public PickListItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public PickList getPickList() {
        return this.pickList;
    }

    public void setPickList(PickList pickList) {
        this.pickList = pickList;
    }

    public PickListItem pickList(PickList pickList) {
        this.setPickList(pickList);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PickListItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public PickListItem orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public InventoryItem getInventoryItem() {
        return this.inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public PickListItem inventoryItem(InventoryItem inventoryItem) {
        this.setInventoryItem(inventoryItem);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PickListItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public FacilityLocation getFacilityLocation() {
        return this.facilityLocation;
    }

    public void setFacilityLocation(FacilityLocation facilityLocation) {
        this.facilityLocation = facilityLocation;
    }

    public PickListItem facilityLocation(FacilityLocation facilityLocation) {
        this.setFacilityLocation(facilityLocation);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PickListItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PickListItem)) {
            return false;
        }
        return getId() != null && getId().equals(((PickListItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PickListItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
