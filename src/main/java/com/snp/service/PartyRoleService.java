package com.snp.service;

import com.snp.domain.PartyRole;
import com.snp.repository.PartyRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyRole}.
 */
@Service
@Transactional
public class PartyRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRoleService.class);

    private final PartyRoleRepository partyRoleRepository;

    public PartyRoleService(PartyRoleRepository partyRoleRepository) {
        this.partyRoleRepository = partyRoleRepository;
    }

    /**
     * Save a partyRole.
     *
     * @param partyRole the entity to save.
     * @return the persisted entity.
     */
    public PartyRole save(PartyRole partyRole) {
        LOG.debug("Request to save PartyRole : {}", partyRole);
        return partyRoleRepository.save(partyRole);
    }

    /**
     * Update a partyRole.
     *
     * @param partyRole the entity to save.
     * @return the persisted entity.
     */
    public PartyRole update(PartyRole partyRole) {
        LOG.debug("Request to update PartyRole : {}", partyRole);
        return partyRoleRepository.save(partyRole);
    }

    /**
     * Partially update a partyRole.
     *
     * @param partyRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyRole> partialUpdate(PartyRole partyRole) {
        LOG.debug("Request to partially update PartyRole : {}", partyRole);

        return partyRoleRepository.findById(partyRole.getId()).map(partyRoleRepository::save);
    }

    /**
     * Get all the partyRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PartyRole> findAllWithEagerRelationships(Pageable pageable) {
        return partyRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one partyRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyRole> findOne(Long id) {
        LOG.debug("Request to get PartyRole : {}", id);
        return partyRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partyRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyRole : {}", id);
        partyRoleRepository.deleteById(id);
    }
}
