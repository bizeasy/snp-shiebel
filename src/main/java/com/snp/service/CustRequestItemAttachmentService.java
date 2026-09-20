package com.snp.service;

import com.snp.domain.CustRequestItemAttachment;
import com.snp.repository.CustRequestItemAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustRequestItemAttachment}.
 */
@Service
@Transactional
public class CustRequestItemAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestItemAttachmentService.class);

    private final CustRequestItemAttachmentRepository custRequestItemAttachmentRepository;

    public CustRequestItemAttachmentService(CustRequestItemAttachmentRepository custRequestItemAttachmentRepository) {
        this.custRequestItemAttachmentRepository = custRequestItemAttachmentRepository;
    }

    /**
     * Save a custRequestItemAttachment.
     *
     * @param custRequestItemAttachment the entity to save.
     * @return the persisted entity.
     */
    public CustRequestItemAttachment save(CustRequestItemAttachment custRequestItemAttachment) {
        LOG.debug("Request to save CustRequestItemAttachment : {}", custRequestItemAttachment);
        return custRequestItemAttachmentRepository.save(custRequestItemAttachment);
    }

    /**
     * Update a custRequestItemAttachment.
     *
     * @param custRequestItemAttachment the entity to save.
     * @return the persisted entity.
     */
    public CustRequestItemAttachment update(CustRequestItemAttachment custRequestItemAttachment) {
        LOG.debug("Request to update CustRequestItemAttachment : {}", custRequestItemAttachment);
        return custRequestItemAttachmentRepository.save(custRequestItemAttachment);
    }

    /**
     * Partially update a custRequestItemAttachment.
     *
     * @param custRequestItemAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustRequestItemAttachment> partialUpdate(CustRequestItemAttachment custRequestItemAttachment) {
        LOG.debug("Request to partially update CustRequestItemAttachment : {}", custRequestItemAttachment);

        return custRequestItemAttachmentRepository
            .findById(custRequestItemAttachment.getId())
            .map(existingCustRequestItemAttachment -> {
                updateIfPresent(existingCustRequestItemAttachment::setName, custRequestItemAttachment.getName());

                return existingCustRequestItemAttachment;
            })
            .map(custRequestItemAttachmentRepository::save);
    }

    /**
     * Get all the custRequestItemAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustRequestItemAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return custRequestItemAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one custRequestItemAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustRequestItemAttachment> findOne(Long id) {
        LOG.debug("Request to get CustRequestItemAttachment : {}", id);
        return custRequestItemAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the custRequestItemAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustRequestItemAttachment : {}", id);
        custRequestItemAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
