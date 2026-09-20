package com.snp.service;

import com.snp.domain.OrderShipment;
import com.snp.repository.OrderShipmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderShipment}.
 */
@Service
@Transactional
public class OrderShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderShipmentService.class);

    private final OrderShipmentRepository orderShipmentRepository;

    public OrderShipmentService(OrderShipmentRepository orderShipmentRepository) {
        this.orderShipmentRepository = orderShipmentRepository;
    }

    /**
     * Save a orderShipment.
     *
     * @param orderShipment the entity to save.
     * @return the persisted entity.
     */
    public OrderShipment save(OrderShipment orderShipment) {
        LOG.debug("Request to save OrderShipment : {}", orderShipment);
        return orderShipmentRepository.save(orderShipment);
    }

    /**
     * Update a orderShipment.
     *
     * @param orderShipment the entity to save.
     * @return the persisted entity.
     */
    public OrderShipment update(OrderShipment orderShipment) {
        LOG.debug("Request to update OrderShipment : {}", orderShipment);
        return orderShipmentRepository.save(orderShipment);
    }

    /**
     * Partially update a orderShipment.
     *
     * @param orderShipment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderShipment> partialUpdate(OrderShipment orderShipment) {
        LOG.debug("Request to partially update OrderShipment : {}", orderShipment);

        return orderShipmentRepository
            .findById(orderShipment.getId())
            .map(existingOrderShipment -> {
                updateIfPresent(existingOrderShipment::setQuantity, orderShipment.getQuantity());

                return existingOrderShipment;
            })
            .map(orderShipmentRepository::save);
    }

    /**
     * Get one orderShipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderShipment> findOne(Long id) {
        LOG.debug("Request to get OrderShipment : {}", id);
        return orderShipmentRepository.findById(id);
    }

    /**
     * Delete the orderShipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderShipment : {}", id);
        orderShipmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
