package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snp.domain.enumeration.SharePermission;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FileShare.
 */
@Entity
@Table(name = "file_share")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileShare implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    private SharePermission permission;

    @Size(min = 5, max = 100)
    @Column(name = "shared_with_email", length = 100)
    private String sharedWithEmail;

    @NotNull
    @Column(name = "shared_date", nullable = false)
    private Instant sharedDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Size(max = 100)
    @Column(name = "access_token", length = 100)
    private String accessToken;

    @Column(name = "is_revoked")
    private Boolean isRevoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "owner" }, allowSetters = true)
    private FileFolder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contentType", "folder", "owner" }, allowSetters = true)
    private Attachment attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party sharedWith;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party sharedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileShare id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SharePermission getPermission() {
        return this.permission;
    }

    public FileShare permission(SharePermission permission) {
        this.setPermission(permission);
        return this;
    }

    public void setPermission(SharePermission permission) {
        this.permission = permission;
    }

    public String getSharedWithEmail() {
        return this.sharedWithEmail;
    }

    public FileShare sharedWithEmail(String sharedWithEmail) {
        this.setSharedWithEmail(sharedWithEmail);
        return this;
    }

    public void setSharedWithEmail(String sharedWithEmail) {
        this.sharedWithEmail = sharedWithEmail;
    }

    public Instant getSharedDate() {
        return this.sharedDate;
    }

    public FileShare sharedDate(Instant sharedDate) {
        this.setSharedDate(sharedDate);
        return this;
    }

    public void setSharedDate(Instant sharedDate) {
        this.sharedDate = sharedDate;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public FileShare expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public FileShare accessToken(String accessToken) {
        this.setAccessToken(accessToken);
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Boolean getIsRevoked() {
        return this.isRevoked;
    }

    public FileShare isRevoked(Boolean isRevoked) {
        this.setIsRevoked(isRevoked);
        return this;
    }

    public void setIsRevoked(Boolean isRevoked) {
        this.isRevoked = isRevoked;
    }

    public FileFolder getFolder() {
        return this.folder;
    }

    public void setFolder(FileFolder fileFolder) {
        this.folder = fileFolder;
    }

    public FileShare folder(FileFolder fileFolder) {
        this.setFolder(fileFolder);
        return this;
    }

    public Attachment getAttachment() {
        return this.attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public FileShare attachment(Attachment attachment) {
        this.setAttachment(attachment);
        return this;
    }

    public Party getSharedWith() {
        return this.sharedWith;
    }

    public void setSharedWith(Party party) {
        this.sharedWith = party;
    }

    public FileShare sharedWith(Party party) {
        this.setSharedWith(party);
        return this;
    }

    public Party getSharedBy() {
        return this.sharedBy;
    }

    public void setSharedBy(Party party) {
        this.sharedBy = party;
    }

    public FileShare sharedBy(Party party) {
        this.setSharedBy(party);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileShare)) {
            return false;
        }
        return getId() != null && getId().equals(((FileShare) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileShare{" +
            "id=" + getId() +
            ", permission='" + getPermission() + "'" +
            ", sharedWithEmail='" + getSharedWithEmail() + "'" +
            ", sharedDate='" + getSharedDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", accessToken='" + getAccessToken() + "'" +
            ", isRevoked='" + getIsRevoked() + "'" +
            "}";
    }
}
