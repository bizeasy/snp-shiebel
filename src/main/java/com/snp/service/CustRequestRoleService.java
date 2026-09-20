package com.snp.service;

import com.snp.domain.CustRequestRole;
import com.snp.repository.CustRequestRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustRequestRole}.
 */
@Service
@Transactional
public class CustRequestRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestRoleService.class);

    private final CustRequestRoleRepository custRequestRoleRepository;

    public CustRequestRoleService(CustRequestRoleRepository custRequestRoleRepository) {
        this.custRequestRoleRepository = custRequestRoleRepository;
    }

    /**
     * Save a custRequestRole.
     *
     * @param custRequestRole the entity to save.
     * @return the persisted entity.
     */
    public CustRequestRole save(CustRequestRole custRequestRole) {
        LOG.debug("Request to save CustRequestRole : {}", custRequestRole);
        return custRequestRoleRepository.save(custRequestRole);
    }

    /**
     * Update a custRequestRole.
     *
     * @param custRequestRole the entity to save.
     * @return the persisted entity.
     */
    public CustRequestRole update(CustRequestRole custRequestRole) {
        LOG.debug("Request to update CustRequestRole : {}", custRequestRole);
        return custRequestRoleRepository.save(custRequestRole);
    }

    /**
     * Partially update a custRequestRole.
     *
     * @param custRequestRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustRequestRole> partialUpdate(CustRequestRole custRequestRole) {
        LOG.debug("Request to partially update CustRequestRole : {}", custRequestRole);

        return custRequestRoleRepository.findById(custRequestRole.getId()).map(custRequestRoleRepository::save);
    }

    /**
     * Get all the custRequestRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustRequestRole> findAllWithEagerRelationships(Pageable pageable) {
        return custRequestRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one custRequestRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustRequestRole> findOne(Long id) {
        LOG.debug("Request to get CustRequestRole : {}", id);
        return custRequestRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the custRequestRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustRequestRole : {}", id);
        custRequestRoleRepository.deleteById(id);
    }
}
