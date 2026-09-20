package com.snp.service;

import com.snp.domain.Attachment;
import com.snp.repository.AttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Attachment}.
 */
@Service
@Transactional
public class AttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AttachmentService.class);

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Save a attachment.
     *
     * @param attachment the entity to save.
     * @return the persisted entity.
     */
    public Attachment save(Attachment attachment) {
        LOG.debug("Request to save Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    /**
     * Update a attachment.
     *
     * @param attachment the entity to save.
     * @return the persisted entity.
     */
    public Attachment update(Attachment attachment) {
        LOG.debug("Request to update Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    /**
     * Partially update a attachment.
     *
     * @param attachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Attachment> partialUpdate(Attachment attachment) {
        LOG.debug("Request to partially update Attachment : {}", attachment);

        return attachmentRepository
            .findById(attachment.getId())
            .map(existingAttachment -> {
                updateIfPresent(existingAttachment::setName, attachment.getName());
                updateIfPresent(existingAttachment::setFileAttachment, attachment.getFileAttachment());
                updateIfPresent(existingAttachment::setFileAttachmentContentType, attachment.getFileAttachmentContentType());
                updateIfPresent(existingAttachment::setAttachmentUrl, attachment.getAttachmentUrl());
                updateIfPresent(existingAttachment::setMimeType, attachment.getMimeType());
                updateIfPresent(existingAttachment::setFileSize, attachment.getFileSize());
                updateIfPresent(existingAttachment::setUploadedDate, attachment.getUploadedDate());
                updateIfPresent(existingAttachment::setIsTrashed, attachment.getIsTrashed());
                updateIfPresent(existingAttachment::setChecksum, attachment.getChecksum());

                return existingAttachment;
            })
            .map(attachmentRepository::save);
    }

    /**
     * Get all the attachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Attachment> findAllWithEagerRelationships(Pageable pageable) {
        return attachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one attachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Attachment> findOne(Long id) {
        LOG.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the attachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
