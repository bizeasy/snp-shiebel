package com.snp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Catalogue.
 */
@Entity
@Table(name = "catalogue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Catalogue implements Serializable {

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

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Size(max = 2000)
    @Column(name = "image_path", length = 2000)
    private String imagePath;

    @Size(max = 2000)
    @Column(name = "mobile_image_path", length = 2000)
    private String mobileImagePath;

    @Size(max = 2000)
    @Column(name = "alt_image_1", length = 2000)
    private String altImage1;

    @Size(max = 2000)
    @Column(name = "alt_image_2", length = 2000)
    private String altImage2;

    @Size(max = 2000)
    @Column(name = "alt_image_3", length = 2000)
    private String altImage3;

    @Size(max = 2000)
    @Column(name = "banner_url", length = 2000)
    private String bannerUrl;

    @Column(name = "is_banner")
    private Boolean isBanner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Catalogue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Catalogue name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Catalogue description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public Catalogue sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public Catalogue imagePath(String imagePath) {
        this.setImagePath(imagePath);
        return this;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMobileImagePath() {
        return this.mobileImagePath;
    }

    public Catalogue mobileImagePath(String mobileImagePath) {
        this.setMobileImagePath(mobileImagePath);
        return this;
    }

    public void setMobileImagePath(String mobileImagePath) {
        this.mobileImagePath = mobileImagePath;
    }

    public String getAltImage1() {
        return this.altImage1;
    }

    public Catalogue altImage1(String altImage1) {
        this.setAltImage1(altImage1);
        return this;
    }

    public void setAltImage1(String altImage1) {
        this.altImage1 = altImage1;
    }

    public String getAltImage2() {
        return this.altImage2;
    }

    public Catalogue altImage2(String altImage2) {
        this.setAltImage2(altImage2);
        return this;
    }

    public void setAltImage2(String altImage2) {
        this.altImage2 = altImage2;
    }

    public String getAltImage3() {
        return this.altImage3;
    }

    public Catalogue altImage3(String altImage3) {
        this.setAltImage3(altImage3);
        return this;
    }

    public void setAltImage3(String altImage3) {
        this.altImage3 = altImage3;
    }

    public String getBannerUrl() {
        return this.bannerUrl;
    }

    public Catalogue bannerUrl(String bannerUrl) {
        this.setBannerUrl(bannerUrl);
        return this;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Boolean getIsBanner() {
        return this.isBanner;
    }

    public Catalogue isBanner(Boolean isBanner) {
        this.setIsBanner(isBanner);
        return this;
    }

    public void setIsBanner(Boolean isBanner) {
        this.isBanner = isBanner;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogue)) {
            return false;
        }
        return getId() != null && getId().equals(((Catalogue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalogue{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", sequenceNo=" + getSequenceNo() +
            ", imagePath='" + getImagePath() + "'" +
            ", mobileImagePath='" + getMobileImagePath() + "'" +
            ", altImage1='" + getAltImage1() + "'" +
            ", altImage2='" + getAltImage2() + "'" +
            ", altImage3='" + getAltImage3() + "'" +
            ", bannerUrl='" + getBannerUrl() + "'" +
            ", isBanner='" + getIsBanner() + "'" +
            "}";
    }
}
