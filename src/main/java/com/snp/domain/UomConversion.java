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
 * A UomConversion.
 */
@Entity
@Table(name = "uom_conversion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UomConversion implements Serializable {

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

    @NotNull
    @Column(name = "conversion_factor", precision = 21, scale = 2, nullable = false)
    private BigDecimal conversionFactor;

    @Column(name = "from_date")
    private Instant fromDate;

    @Column(name = "to_date")
    private Instant toDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom fromUom;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom toUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UomConversion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UomConversion name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getConversionFactor() {
        return this.conversionFactor;
    }

    public UomConversion conversionFactor(BigDecimal conversionFactor) {
        this.setConversionFactor(conversionFactor);
        return this;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public UomConversion fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return this.toDate;
    }

    public UomConversion toDate(Instant toDate) {
        this.setToDate(toDate);
        return this;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public Uom getFromUom() {
        return this.fromUom;
    }

    public void setFromUom(Uom uom) {
        this.fromUom = uom;
    }

    public UomConversion fromUom(Uom uom) {
        this.setFromUom(uom);
        return this;
    }

    public Uom getToUom() {
        return this.toUom;
    }

    public void setToUom(Uom uom) {
        this.toUom = uom;
    }

    public UomConversion toUom(Uom uom) {
        this.setToUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UomConversion)) {
            return false;
        }
        return getId() != null && getId().equals(((UomConversion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UomConversion{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", conversionFactor=" + getConversionFactor() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            "}";
    }
}
