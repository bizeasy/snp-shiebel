package com.snp.service;

import com.snp.domain.OrderAdjustmentType;
import com.snp.repository.OrderAdjustmentTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderAdjustmentType}.
 */
@Service
@Transactional
public class OrderAdjustmentTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAdjustmentTypeService.class);

    private final OrderAdjustmentTypeRepository orderAdjustmentTypeRepository;

    public OrderAdjustmentTypeService(OrderAdjustmentTypeRepository orderAdjustmentTypeRepository) {
        this.orderAdjustmentTypeRepository = orderAdjustmentTypeRepository;
    }

    /**
     * Save a orderAdjustmentType.
     *
     * @param orderAdjustmentType the entity to save.
     * @return the persisted entity.
     */
    public OrderAdjustmentType save(OrderAdjustmentType orderAdjustmentType) {
        LOG.debug("Request to save OrderAdjustmentType : {}", orderAdjustmentType);
        return orderAdjustmentTypeRepository.save(orderAdjustmentType);
    }

    /**
     * Update a orderAdjustmentType.
     *
     * @param orderAdjustmentType the entity to save.
     * @return the persisted entity.
     */
    public OrderAdjustmentType update(OrderAdjustmentType orderAdjustmentType) {
        LOG.debug("Request to update OrderAdjustmentType : {}", orderAdjustmentType);
        return orderAdjustmentTypeRepository.save(orderAdjustmentType);
    }

    /**
     * Partially update a orderAdjustmentType.
     *
     * @param orderAdjustmentType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderAdjustmentType> partialUpdate(OrderAdjustmentType orderAdjustmentType) {
        LOG.debug("Request to partially update OrderAdjustmentType : {}", orderAdjustmentType);

        return orderAdjustmentTypeRepository
            .findById(orderAdjustmentType.getId())
            .map(existingOrderAdjustmentType -> {
                updateIfPresent(existingOrderAdjustmentType::setName, orderAdjustmentType.getName());
                updateIfPresent(existingOrderAdjustmentType::setDescription, orderAdjustmentType.getDescription());

                return existingOrderAdjustmentType;
            })
            .map(orderAdjustmentTypeRepository::save);
    }

    /**
     * Get all the orderAdjustmentTypes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<OrderAdjustmentType> findAllWithEagerRelationships() {
        return orderAdjustmentTypeRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one orderAdjustmentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderAdjustmentType> findOne(Long id) {
        LOG.debug("Request to get OrderAdjustmentType : {}", id);
        return orderAdjustmentTypeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderAdjustmentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderAdjustmentType : {}", id);
        orderAdjustmentTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
