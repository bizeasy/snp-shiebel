package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FileFolder.
 */
@Entity
@Table(name = "file_folder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileFolder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 2000)
    @Column(name = "path", length = 2000)
    private String path;

    @Column(name = "is_system_folder")
    private Boolean isSystemFolder;

    @Column(name = "is_trashed")
    private Boolean isTrashed;

    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "owner" }, allowSetters = true)
    private FileFolder parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileFolder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FileFolder name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public FileFolder path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIsSystemFolder() {
        return this.isSystemFolder;
    }

    public FileFolder isSystemFolder(Boolean isSystemFolder) {
        this.setIsSystemFolder(isSystemFolder);
        return this;
    }

    public void setIsSystemFolder(Boolean isSystemFolder) {
        this.isSystemFolder = isSystemFolder;
    }

    public Boolean getIsTrashed() {
        return this.isTrashed;
    }

    public FileFolder isTrashed(Boolean isTrashed) {
        this.setIsTrashed(isTrashed);
        return this;
    }

    public void setIsTrashed(Boolean isTrashed) {
        this.isTrashed = isTrashed;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public FileFolder createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public FileFolder getParent() {
        return this.parent;
    }

    public void setParent(FileFolder fileFolder) {
        this.parent = fileFolder;
    }

    public FileFolder parent(FileFolder fileFolder) {
        this.setParent(fileFolder);
        return this;
    }

    public Party getOwner() {
        return this.owner;
    }

    public void setOwner(Party party) {
        this.owner = party;
    }

    public FileFolder owner(Party party) {
        this.setOwner(party);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileFolder)) {
            return false;
        }
        return getId() != null && getId().equals(((FileFolder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileFolder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", isSystemFolder='" + getIsSystemFolder() + "'" +
            ", isTrashed='" + getIsTrashed() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
