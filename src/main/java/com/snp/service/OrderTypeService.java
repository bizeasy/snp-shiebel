package com.snp.service;

import com.snp.domain.OrderType;
import com.snp.repository.OrderTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderType}.
 */
@Service
@Transactional
public class OrderTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderTypeService.class);

    private final OrderTypeRepository orderTypeRepository;

    public OrderTypeService(OrderTypeRepository orderTypeRepository) {
        this.orderTypeRepository = orderTypeRepository;
    }

    /**
     * Save a orderType.
     *
     * @param orderType the entity to save.
     * @return the persisted entity.
     */
    public OrderType save(OrderType orderType) {
        LOG.debug("Request to save OrderType : {}", orderType);
        return orderTypeRepository.save(orderType);
    }

    /**
     * Update a orderType.
     *
     * @param orderType the entity to save.
     * @return the persisted entity.
     */
    public OrderType update(OrderType orderType) {
        LOG.debug("Request to update OrderType : {}", orderType);
        return orderTypeRepository.save(orderType);
    }

    /**
     * Partially update a orderType.
     *
     * @param orderType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderType> partialUpdate(OrderType orderType) {
        LOG.debug("Request to partially update OrderType : {}", orderType);

        return orderTypeRepository
            .findById(orderType.getId())
            .map(existingOrderType -> {
                updateIfPresent(existingOrderType::setName, orderType.getName());
                updateIfPresent(existingOrderType::setDescription, orderType.getDescription());

                return existingOrderType;
            })
            .map(orderTypeRepository::save);
    }

    /**
     * Get one orderType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderType> findOne(Long id) {
        LOG.debug("Request to get OrderType : {}", id);
        return orderTypeRepository.findById(id);
    }

    /**
     * Delete the orderType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderType : {}", id);
        orderTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
