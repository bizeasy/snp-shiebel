package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FacilityLocation.
 */
@Entity
@Table(name = "facility_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "location_seq_id", length = 20, nullable = false)
    private String locationSeqId;

    @Size(max = 20)
    @Column(name = "area_id", length = 20)
    private String areaId;

    @Size(max = 20)
    @Column(name = "aisle_id", length = 20)
    private String aisleId;

    @Size(max = 20)
    @Column(name = "section_id", length = 20)
    private String sectionId;

    @Size(max = 20)
    @Column(name = "level_id", length = 20)
    private String levelId;

    @Size(max = 20)
    @Column(name = "position_id", length = 20)
    private String positionId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "facilityType",
            "parentFacility",
            "ownerParty",
            "facilityGroup",
            "sizeUom",
            "geoPoint",
            "inventoryItemType",
            "productStore",
        },
        allowSetters = true
    )
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    private LocationType locationType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FacilityLocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationSeqId() {
        return this.locationSeqId;
    }

    public FacilityLocation locationSeqId(String locationSeqId) {
        this.setLocationSeqId(locationSeqId);
        return this;
    }

    public void setLocationSeqId(String locationSeqId) {
        this.locationSeqId = locationSeqId;
    }

    public String getAreaId() {
        return this.areaId;
    }

    public FacilityLocation areaId(String areaId) {
        this.setAreaId(areaId);
        return this;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAisleId() {
        return this.aisleId;
    }

    public FacilityLocation aisleId(String aisleId) {
        this.setAisleId(aisleId);
        return this;
    }

    public void setAisleId(String aisleId) {
        this.aisleId = aisleId;
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public FacilityLocation sectionId(String sectionId) {
        this.setSectionId(sectionId);
        return this;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getLevelId() {
        return this.levelId;
    }

    public FacilityLocation levelId(String levelId) {
        this.setLevelId(levelId);
        return this;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getPositionId() {
        return this.positionId;
    }

    public FacilityLocation positionId(String positionId) {
        this.setPositionId(positionId);
        return this;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public FacilityLocation facility(Facility facility) {
        this.setFacility(facility);
        return this;
    }

    public LocationType getLocationType() {
        return this.locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public FacilityLocation locationType(LocationType locationType) {
        this.setLocationType(locationType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacilityLocation)) {
            return false;
        }
        return getId() != null && getId().equals(((FacilityLocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityLocation{" +
            "id=" + getId() +
            ", locationSeqId='" + getLocationSeqId() + "'" +
            ", areaId='" + getAreaId() + "'" +
            ", aisleId='" + getAisleId() + "'" +
            ", sectionId='" + getSectionId() + "'" +
            ", levelId='" + getLevelId() + "'" +
            ", positionId='" + getPositionId() + "'" +
            "}";
    }
}
