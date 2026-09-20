package com.snp.service;

import com.snp.domain.OrderRole;
import com.snp.repository.OrderRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderRole}.
 */
@Service
@Transactional
public class OrderRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderRoleService.class);

    private final OrderRoleRepository orderRoleRepository;

    public OrderRoleService(OrderRoleRepository orderRoleRepository) {
        this.orderRoleRepository = orderRoleRepository;
    }

    /**
     * Save a orderRole.
     *
     * @param orderRole the entity to save.
     * @return the persisted entity.
     */
    public OrderRole save(OrderRole orderRole) {
        LOG.debug("Request to save OrderRole : {}", orderRole);
        return orderRoleRepository.save(orderRole);
    }

    /**
     * Update a orderRole.
     *
     * @param orderRole the entity to save.
     * @return the persisted entity.
     */
    public OrderRole update(OrderRole orderRole) {
        LOG.debug("Request to update OrderRole : {}", orderRole);
        return orderRoleRepository.save(orderRole);
    }

    /**
     * Partially update a orderRole.
     *
     * @param orderRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderRole> partialUpdate(OrderRole orderRole) {
        LOG.debug("Request to partially update OrderRole : {}", orderRole);

        return orderRoleRepository.findById(orderRole.getId()).map(orderRoleRepository::save);
    }

    /**
     * Get all the orderRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderRole> findAllWithEagerRelationships(Pageable pageable) {
        return orderRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderRole> findOne(Long id) {
        LOG.debug("Request to get OrderRole : {}", id);
        return orderRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderRole : {}", id);
        orderRoleRepository.deleteById(id);
    }
}
