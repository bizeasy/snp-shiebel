package com.snp.service;

import com.snp.domain.OrderAttachment;
import com.snp.repository.OrderAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderAttachment}.
 */
@Service
@Transactional
public class OrderAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAttachmentService.class);

    private final OrderAttachmentRepository orderAttachmentRepository;

    public OrderAttachmentService(OrderAttachmentRepository orderAttachmentRepository) {
        this.orderAttachmentRepository = orderAttachmentRepository;
    }

    /**
     * Save a orderAttachment.
     *
     * @param orderAttachment the entity to save.
     * @return the persisted entity.
     */
    public OrderAttachment save(OrderAttachment orderAttachment) {
        LOG.debug("Request to save OrderAttachment : {}", orderAttachment);
        return orderAttachmentRepository.save(orderAttachment);
    }

    /**
     * Update a orderAttachment.
     *
     * @param orderAttachment the entity to save.
     * @return the persisted entity.
     */
    public OrderAttachment update(OrderAttachment orderAttachment) {
        LOG.debug("Request to update OrderAttachment : {}", orderAttachment);
        return orderAttachmentRepository.save(orderAttachment);
    }

    /**
     * Partially update a orderAttachment.
     *
     * @param orderAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderAttachment> partialUpdate(OrderAttachment orderAttachment) {
        LOG.debug("Request to partially update OrderAttachment : {}", orderAttachment);

        return orderAttachmentRepository
            .findById(orderAttachment.getId())
            .map(existingOrderAttachment -> {
                updateIfPresent(existingOrderAttachment::setName, orderAttachment.getName());

                return existingOrderAttachment;
            })
            .map(orderAttachmentRepository::save);
    }

    /**
     * Get all the orderAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return orderAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderAttachment> findOne(Long id) {
        LOG.debug("Request to get OrderAttachment : {}", id);
        return orderAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderAttachment : {}", id);
        orderAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
