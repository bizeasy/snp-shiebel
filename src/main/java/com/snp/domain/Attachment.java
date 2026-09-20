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
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attachment implements Serializable {

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

    @Lob
    @Column(name = "file_attachment")
    private byte[] fileAttachment;

    @Column(name = "file_attachment_content_type")
    private String fileAttachmentContentType;

    @Size(max = 2000)
    @Column(name = "attachment_url", length = 2000)
    private String attachmentUrl;

    @Size(max = 100)
    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "uploaded_date")
    private Instant uploadedDate;

    @Column(name = "is_trashed")
    private Boolean isTrashed;

    @Size(max = 128)
    @Column(name = "checksum", length = 128)
    private String checksum;

    @ManyToOne(fetch = FetchType.LAZY)
    private ContentType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "owner" }, allowSetters = true)
    private FileFolder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Attachment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFileAttachment() {
        return this.fileAttachment;
    }

    public Attachment fileAttachment(byte[] fileAttachment) {
        this.setFileAttachment(fileAttachment);
        return this;
    }

    public void setFileAttachment(byte[] fileAttachment) {
        this.fileAttachment = fileAttachment;
    }

    public String getFileAttachmentContentType() {
        return this.fileAttachmentContentType;
    }

    public Attachment fileAttachmentContentType(String fileAttachmentContentType) {
        this.fileAttachmentContentType = fileAttachmentContentType;
        return this;
    }

    public void setFileAttachmentContentType(String fileAttachmentContentType) {
        this.fileAttachmentContentType = fileAttachmentContentType;
    }

    public String getAttachmentUrl() {
        return this.attachmentUrl;
    }

    public Attachment attachmentUrl(String attachmentUrl) {
        this.setAttachmentUrl(attachmentUrl);
        return this;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Attachment mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public Attachment fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getUploadedDate() {
        return this.uploadedDate;
    }

    public Attachment uploadedDate(Instant uploadedDate) {
        this.setUploadedDate(uploadedDate);
        return this;
    }

    public void setUploadedDate(Instant uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Boolean getIsTrashed() {
        return this.isTrashed;
    }

    public Attachment isTrashed(Boolean isTrashed) {
        this.setIsTrashed(isTrashed);
        return this;
    }

    public void setIsTrashed(Boolean isTrashed) {
        this.isTrashed = isTrashed;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public Attachment checksum(String checksum) {
        this.setChecksum(checksum);
        return this;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Attachment contentType(ContentType contentType) {
        this.setContentType(contentType);
        return this;
    }

    public FileFolder getFolder() {
        return this.folder;
    }

    public void setFolder(FileFolder fileFolder) {
        this.folder = fileFolder;
    }

    public Attachment folder(FileFolder fileFolder) {
        this.setFolder(fileFolder);
        return this;
    }

    public Party getOwner() {
        return this.owner;
    }

    public void setOwner(Party party) {
        this.owner = party;
    }

    public Attachment owner(Party party) {
        this.setOwner(party);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return getId() != null && getId().equals(((Attachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fileAttachment='" + getFileAttachment() + "'" +
            ", fileAttachmentContentType='" + getFileAttachmentContentType() + "'" +
            ", attachmentUrl='" + getAttachmentUrl() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", fileSize=" + getFileSize() +
            ", uploadedDate='" + getUploadedDate() + "'" +
            ", isTrashed='" + getIsTrashed() + "'" +
            ", checksum='" + getChecksum() + "'" +
            "}";
    }
}
