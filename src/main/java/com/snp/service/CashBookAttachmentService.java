package com.snp.service;

import com.snp.domain.CashBookAttachment;
import com.snp.repository.CashBookAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CashBookAttachment}.
 */
@Service
@Transactional
public class CashBookAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(CashBookAttachmentService.class);

    private final CashBookAttachmentRepository cashBookAttachmentRepository;

    public CashBookAttachmentService(CashBookAttachmentRepository cashBookAttachmentRepository) {
        this.cashBookAttachmentRepository = cashBookAttachmentRepository;
    }

    /**
     * Save a cashBookAttachment.
     *
     * @param cashBookAttachment the entity to save.
     * @return the persisted entity.
     */
    public CashBookAttachment save(CashBookAttachment cashBookAttachment) {
        LOG.debug("Request to save CashBookAttachment : {}", cashBookAttachment);
        return cashBookAttachmentRepository.save(cashBookAttachment);
    }

    /**
     * Update a cashBookAttachment.
     *
     * @param cashBookAttachment the entity to save.
     * @return the persisted entity.
     */
    public CashBookAttachment update(CashBookAttachment cashBookAttachment) {
        LOG.debug("Request to update CashBookAttachment : {}", cashBookAttachment);
        return cashBookAttachmentRepository.save(cashBookAttachment);
    }

    /**
     * Partially update a cashBookAttachment.
     *
     * @param cashBookAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CashBookAttachment> partialUpdate(CashBookAttachment cashBookAttachment) {
        LOG.debug("Request to partially update CashBookAttachment : {}", cashBookAttachment);

        return cashBookAttachmentRepository
            .findById(cashBookAttachment.getId())
            .map(existingCashBookAttachment -> {
                updateIfPresent(existingCashBookAttachment::setName, cashBookAttachment.getName());

                return existingCashBookAttachment;
            })
            .map(cashBookAttachmentRepository::save);
    }

    /**
     * Get all the cashBookAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CashBookAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return cashBookAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one cashBookAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashBookAttachment> findOne(Long id) {
        LOG.debug("Request to get CashBookAttachment : {}", id);
        return cashBookAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the cashBookAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CashBookAttachment : {}", id);
        cashBookAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
