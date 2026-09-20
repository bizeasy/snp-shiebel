package com.snp.service;

import com.snp.domain.OrderAdjustment;
import com.snp.repository.OrderAdjustmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderAdjustment}.
 */
@Service
@Transactional
public class OrderAdjustmentService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAdjustmentService.class);

    private final OrderAdjustmentRepository orderAdjustmentRepository;

    public OrderAdjustmentService(OrderAdjustmentRepository orderAdjustmentRepository) {
        this.orderAdjustmentRepository = orderAdjustmentRepository;
    }

    /**
     * Save a orderAdjustment.
     *
     * @param orderAdjustment the entity to save.
     * @return the persisted entity.
     */
    public OrderAdjustment save(OrderAdjustment orderAdjustment) {
        LOG.debug("Request to save OrderAdjustment : {}", orderAdjustment);
        return orderAdjustmentRepository.save(orderAdjustment);
    }

    /**
     * Update a orderAdjustment.
     *
     * @param orderAdjustment the entity to save.
     * @return the persisted entity.
     */
    public OrderAdjustment update(OrderAdjustment orderAdjustment) {
        LOG.debug("Request to update OrderAdjustment : {}", orderAdjustment);
        return orderAdjustmentRepository.save(orderAdjustment);
    }

    /**
     * Partially update a orderAdjustment.
     *
     * @param orderAdjustment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderAdjustment> partialUpdate(OrderAdjustment orderAdjustment) {
        LOG.debug("Request to partially update OrderAdjustment : {}", orderAdjustment);

        return orderAdjustmentRepository
            .findById(orderAdjustment.getId())
            .map(existingOrderAdjustment -> {
                updateIfPresent(existingOrderAdjustment::setDescription, orderAdjustment.getDescription());
                updateIfPresent(existingOrderAdjustment::setComments, orderAdjustment.getComments());
                updateIfPresent(existingOrderAdjustment::setAmount, orderAdjustment.getAmount());
                updateIfPresent(existingOrderAdjustment::setSourcePercentage, orderAdjustment.getSourcePercentage());
                updateIfPresent(existingOrderAdjustment::setSourceRefId, orderAdjustment.getSourceRefId());
                updateIfPresent(existingOrderAdjustment::setExemptAmount, orderAdjustment.getExemptAmount());
                updateIfPresent(existingOrderAdjustment::setIncludeInTax, orderAdjustment.getIncludeInTax());
                updateIfPresent(existingOrderAdjustment::setIncludeInShipping, orderAdjustment.getIncludeInShipping());
                updateIfPresent(existingOrderAdjustment::setIncludedInPrice, orderAdjustment.getIncludedInPrice());
                updateIfPresent(existingOrderAdjustment::setEntryDate, orderAdjustment.getEntryDate());

                return existingOrderAdjustment;
            })
            .map(orderAdjustmentRepository::save);
    }

    /**
     * Get all the orderAdjustments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderAdjustment> findAllWithEagerRelationships(Pageable pageable) {
        return orderAdjustmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderAdjustment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderAdjustment> findOne(Long id) {
        LOG.debug("Request to get OrderAdjustment : {}", id);
        return orderAdjustmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderAdjustment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderAdjustment : {}", id);
        orderAdjustmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
