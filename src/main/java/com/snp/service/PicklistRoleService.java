package com.snp.service;

import com.snp.domain.PicklistRole;
import com.snp.repository.PicklistRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PicklistRole}.
 */
@Service
@Transactional
public class PicklistRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(PicklistRoleService.class);

    private final PicklistRoleRepository picklistRoleRepository;

    public PicklistRoleService(PicklistRoleRepository picklistRoleRepository) {
        this.picklistRoleRepository = picklistRoleRepository;
    }

    /**
     * Save a picklistRole.
     *
     * @param picklistRole the entity to save.
     * @return the persisted entity.
     */
    public PicklistRole save(PicklistRole picklistRole) {
        LOG.debug("Request to save PicklistRole : {}", picklistRole);
        return picklistRoleRepository.save(picklistRole);
    }

    /**
     * Update a picklistRole.
     *
     * @param picklistRole the entity to save.
     * @return the persisted entity.
     */
    public PicklistRole update(PicklistRole picklistRole) {
        LOG.debug("Request to update PicklistRole : {}", picklistRole);
        return picklistRoleRepository.save(picklistRole);
    }

    /**
     * Partially update a picklistRole.
     *
     * @param picklistRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PicklistRole> partialUpdate(PicklistRole picklistRole) {
        LOG.debug("Request to partially update PicklistRole : {}", picklistRole);

        return picklistRoleRepository.findById(picklistRole.getId()).map(picklistRoleRepository::save);
    }

    /**
     * Get all the picklistRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PicklistRole> findAllWithEagerRelationships(Pageable pageable) {
        return picklistRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one picklistRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PicklistRole> findOne(Long id) {
        LOG.debug("Request to get PicklistRole : {}", id);
        return picklistRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the picklistRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PicklistRole : {}", id);
        picklistRoleRepository.deleteById(id);
    }
}
