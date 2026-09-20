package com.snp.service;

import com.snp.domain.OrderItemType;
import com.snp.repository.OrderItemTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderItemType}.
 */
@Service
@Transactional
public class OrderItemTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemTypeService.class);

    private final OrderItemTypeRepository orderItemTypeRepository;

    public OrderItemTypeService(OrderItemTypeRepository orderItemTypeRepository) {
        this.orderItemTypeRepository = orderItemTypeRepository;
    }

    /**
     * Save a orderItemType.
     *
     * @param orderItemType the entity to save.
     * @return the persisted entity.
     */
    public OrderItemType save(OrderItemType orderItemType) {
        LOG.debug("Request to save OrderItemType : {}", orderItemType);
        return orderItemTypeRepository.save(orderItemType);
    }

    /**
     * Update a orderItemType.
     *
     * @param orderItemType the entity to save.
     * @return the persisted entity.
     */
    public OrderItemType update(OrderItemType orderItemType) {
        LOG.debug("Request to update OrderItemType : {}", orderItemType);
        return orderItemTypeRepository.save(orderItemType);
    }

    /**
     * Partially update a orderItemType.
     *
     * @param orderItemType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItemType> partialUpdate(OrderItemType orderItemType) {
        LOG.debug("Request to partially update OrderItemType : {}", orderItemType);

        return orderItemTypeRepository
            .findById(orderItemType.getId())
            .map(existingOrderItemType -> {
                updateIfPresent(existingOrderItemType::setName, orderItemType.getName());
                updateIfPresent(existingOrderItemType::setDescription, orderItemType.getDescription());

                return existingOrderItemType;
            })
            .map(orderItemTypeRepository::save);
    }

    /**
     * Get one orderItemType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItemType> findOne(Long id) {
        LOG.debug("Request to get OrderItemType : {}", id);
        return orderItemTypeRepository.findById(id);
    }

    /**
     * Delete the orderItemType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderItemType : {}", id);
        orderItemTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
