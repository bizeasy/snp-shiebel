package com.snp.service;

import com.snp.domain.OrderItemAttachment;
import com.snp.repository.OrderItemAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderItemAttachment}.
 */
@Service
@Transactional
public class OrderItemAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemAttachmentService.class);

    private final OrderItemAttachmentRepository orderItemAttachmentRepository;

    public OrderItemAttachmentService(OrderItemAttachmentRepository orderItemAttachmentRepository) {
        this.orderItemAttachmentRepository = orderItemAttachmentRepository;
    }

    /**
     * Save a orderItemAttachment.
     *
     * @param orderItemAttachment the entity to save.
     * @return the persisted entity.
     */
    public OrderItemAttachment save(OrderItemAttachment orderItemAttachment) {
        LOG.debug("Request to save OrderItemAttachment : {}", orderItemAttachment);
        return orderItemAttachmentRepository.save(orderItemAttachment);
    }

    /**
     * Update a orderItemAttachment.
     *
     * @param orderItemAttachment the entity to save.
     * @return the persisted entity.
     */
    public OrderItemAttachment update(OrderItemAttachment orderItemAttachment) {
        LOG.debug("Request to update OrderItemAttachment : {}", orderItemAttachment);
        return orderItemAttachmentRepository.save(orderItemAttachment);
    }

    /**
     * Partially update a orderItemAttachment.
     *
     * @param orderItemAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItemAttachment> partialUpdate(OrderItemAttachment orderItemAttachment) {
        LOG.debug("Request to partially update OrderItemAttachment : {}", orderItemAttachment);

        return orderItemAttachmentRepository
            .findById(orderItemAttachment.getId())
            .map(existingOrderItemAttachment -> {
                updateIfPresent(existingOrderItemAttachment::setName, orderItemAttachment.getName());

                return existingOrderItemAttachment;
            })
            .map(orderItemAttachmentRepository::save);
    }

    /**
     * Get all the orderItemAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderItemAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return orderItemAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderItemAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItemAttachment> findOne(Long id) {
        LOG.debug("Request to get OrderItemAttachment : {}", id);
        return orderItemAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderItemAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderItemAttachment : {}", id);
        orderItemAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
