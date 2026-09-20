package com.snp.service;

import com.snp.domain.CustRequestAttachment;
import com.snp.repository.CustRequestAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustRequestAttachment}.
 */
@Service
@Transactional
public class CustRequestAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestAttachmentService.class);

    private final CustRequestAttachmentRepository custRequestAttachmentRepository;

    public CustRequestAttachmentService(CustRequestAttachmentRepository custRequestAttachmentRepository) {
        this.custRequestAttachmentRepository = custRequestAttachmentRepository;
    }

    /**
     * Save a custRequestAttachment.
     *
     * @param custRequestAttachment the entity to save.
     * @return the persisted entity.
     */
    public CustRequestAttachment save(CustRequestAttachment custRequestAttachment) {
        LOG.debug("Request to save CustRequestAttachment : {}", custRequestAttachment);
        return custRequestAttachmentRepository.save(custRequestAttachment);
    }

    /**
     * Update a custRequestAttachment.
     *
     * @param custRequestAttachment the entity to save.
     * @return the persisted entity.
     */
    public CustRequestAttachment update(CustRequestAttachment custRequestAttachment) {
        LOG.debug("Request to update CustRequestAttachment : {}", custRequestAttachment);
        return custRequestAttachmentRepository.save(custRequestAttachment);
    }

    /**
     * Partially update a custRequestAttachment.
     *
     * @param custRequestAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustRequestAttachment> partialUpdate(CustRequestAttachment custRequestAttachment) {
        LOG.debug("Request to partially update CustRequestAttachment : {}", custRequestAttachment);

        return custRequestAttachmentRepository
            .findById(custRequestAttachment.getId())
            .map(existingCustRequestAttachment -> {
                updateIfPresent(existingCustRequestAttachment::setName, custRequestAttachment.getName());

                return existingCustRequestAttachment;
            })
            .map(custRequestAttachmentRepository::save);
    }

    /**
     * Get all the custRequestAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustRequestAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return custRequestAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one custRequestAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustRequestAttachment> findOne(Long id) {
        LOG.debug("Request to get CustRequestAttachment : {}", id);
        return custRequestAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the custRequestAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustRequestAttachment : {}", id);
        custRequestAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
