package com.snp.service;

import com.snp.domain.OrderStatus;
import com.snp.repository.OrderStatusRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderStatus}.
 */
@Service
@Transactional
public class OrderStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderStatusService.class);

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Save a orderStatus.
     *
     * @param orderStatus the entity to save.
     * @return the persisted entity.
     */
    public OrderStatus save(OrderStatus orderStatus) {
        LOG.debug("Request to save OrderStatus : {}", orderStatus);
        return orderStatusRepository.save(orderStatus);
    }

    /**
     * Update a orderStatus.
     *
     * @param orderStatus the entity to save.
     * @return the persisted entity.
     */
    public OrderStatus update(OrderStatus orderStatus) {
        LOG.debug("Request to update OrderStatus : {}", orderStatus);
        return orderStatusRepository.save(orderStatus);
    }

    /**
     * Partially update a orderStatus.
     *
     * @param orderStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderStatus> partialUpdate(OrderStatus orderStatus) {
        LOG.debug("Request to partially update OrderStatus : {}", orderStatus);

        return orderStatusRepository
            .findById(orderStatus.getId())
            .map(existingOrderStatus -> {
                updateIfPresent(existingOrderStatus::setStatusDateTime, orderStatus.getStatusDateTime());
                updateIfPresent(existingOrderStatus::setSequenceNo, orderStatus.getSequenceNo());

                return existingOrderStatus;
            })
            .map(orderStatusRepository::save);
    }

    /**
     * Get all the orderStatuses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderStatus> findAllWithEagerRelationships(Pageable pageable) {
        return orderStatusRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderStatus> findOne(Long id) {
        LOG.debug("Request to get OrderStatus : {}", id);
        return orderStatusRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderStatus : {}", id);
        orderStatusRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
