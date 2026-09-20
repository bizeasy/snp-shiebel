package com.snp.service;

import com.snp.domain.QuoteAttachment;
import com.snp.repository.QuoteAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.QuoteAttachment}.
 */
@Service
@Transactional
public class QuoteAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteAttachmentService.class);

    private final QuoteAttachmentRepository quoteAttachmentRepository;

    public QuoteAttachmentService(QuoteAttachmentRepository quoteAttachmentRepository) {
        this.quoteAttachmentRepository = quoteAttachmentRepository;
    }

    /**
     * Save a quoteAttachment.
     *
     * @param quoteAttachment the entity to save.
     * @return the persisted entity.
     */
    public QuoteAttachment save(QuoteAttachment quoteAttachment) {
        LOG.debug("Request to save QuoteAttachment : {}", quoteAttachment);
        return quoteAttachmentRepository.save(quoteAttachment);
    }

    /**
     * Update a quoteAttachment.
     *
     * @param quoteAttachment the entity to save.
     * @return the persisted entity.
     */
    public QuoteAttachment update(QuoteAttachment quoteAttachment) {
        LOG.debug("Request to update QuoteAttachment : {}", quoteAttachment);
        return quoteAttachmentRepository.save(quoteAttachment);
    }

    /**
     * Partially update a quoteAttachment.
     *
     * @param quoteAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuoteAttachment> partialUpdate(QuoteAttachment quoteAttachment) {
        LOG.debug("Request to partially update QuoteAttachment : {}", quoteAttachment);

        return quoteAttachmentRepository
            .findById(quoteAttachment.getId())
            .map(existingQuoteAttachment -> {
                updateIfPresent(existingQuoteAttachment::setName, quoteAttachment.getName());

                return existingQuoteAttachment;
            })
            .map(quoteAttachmentRepository::save);
    }

    /**
     * Get all the quoteAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<QuoteAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return quoteAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one quoteAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuoteAttachment> findOne(Long id) {
        LOG.debug("Request to get QuoteAttachment : {}", id);
        return quoteAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the quoteAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QuoteAttachment : {}", id);
        quoteAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
