package com.snp.service;

import com.snp.domain.OrderContactMech;
import com.snp.repository.OrderContactMechRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderContactMech}.
 */
@Service
@Transactional
public class OrderContactMechService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderContactMechService.class);

    private final OrderContactMechRepository orderContactMechRepository;

    public OrderContactMechService(OrderContactMechRepository orderContactMechRepository) {
        this.orderContactMechRepository = orderContactMechRepository;
    }

    /**
     * Save a orderContactMech.
     *
     * @param orderContactMech the entity to save.
     * @return the persisted entity.
     */
    public OrderContactMech save(OrderContactMech orderContactMech) {
        LOG.debug("Request to save OrderContactMech : {}", orderContactMech);
        return orderContactMechRepository.save(orderContactMech);
    }

    /**
     * Update a orderContactMech.
     *
     * @param orderContactMech the entity to save.
     * @return the persisted entity.
     */
    public OrderContactMech update(OrderContactMech orderContactMech) {
        LOG.debug("Request to update OrderContactMech : {}", orderContactMech);
        return orderContactMechRepository.save(orderContactMech);
    }

    /**
     * Partially update a orderContactMech.
     *
     * @param orderContactMech the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderContactMech> partialUpdate(OrderContactMech orderContactMech) {
        LOG.debug("Request to partially update OrderContactMech : {}", orderContactMech);

        return orderContactMechRepository
            .findById(orderContactMech.getId())
            .map(existingOrderContactMech -> {
                updateIfPresent(existingOrderContactMech::setFromDate, orderContactMech.getFromDate());
                updateIfPresent(existingOrderContactMech::setThruDate, orderContactMech.getThruDate());

                return existingOrderContactMech;
            })
            .map(orderContactMechRepository::save);
    }

    /**
     * Get all the orderContactMeches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return orderContactMechRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderContactMech by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderContactMech> findOne(Long id) {
        LOG.debug("Request to get OrderContactMech : {}", id);
        return orderContactMechRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderContactMech by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderContactMech : {}", id);
        orderContactMechRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
