package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GeoAssoc.
 */
@Entity
@Table(name = "geo_assoc")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GeoAssoc implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "geoType" }, allowSetters = true)
    private Geo geo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "geoType" }, allowSetters = true)
    private Geo geoTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private GeoAssocType geoAssocType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GeoAssoc id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Geo getGeo() {
        return this.geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public GeoAssoc geo(Geo geo) {
        this.setGeo(geo);
        return this;
    }

    public Geo getGeoTo() {
        return this.geoTo;
    }

    public void setGeoTo(Geo geo) {
        this.geoTo = geo;
    }

    public GeoAssoc geoTo(Geo geo) {
        this.setGeoTo(geo);
        return this;
    }

    public GeoAssocType getGeoAssocType() {
        return this.geoAssocType;
    }

    public void setGeoAssocType(GeoAssocType geoAssocType) {
        this.geoAssocType = geoAssocType;
    }

    public GeoAssoc geoAssocType(GeoAssocType geoAssocType) {
        this.setGeoAssocType(geoAssocType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoAssoc)) {
            return false;
        }
        return getId() != null && getId().equals(((GeoAssoc) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeoAssoc{" +
            "id=" + getId() +
            "}";
    }
}
