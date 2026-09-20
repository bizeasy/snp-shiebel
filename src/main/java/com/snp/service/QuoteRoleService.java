package com.snp.service;

import com.snp.domain.QuoteRole;
import com.snp.repository.QuoteRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.QuoteRole}.
 */
@Service
@Transactional
public class QuoteRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteRoleService.class);

    private final QuoteRoleRepository quoteRoleRepository;

    public QuoteRoleService(QuoteRoleRepository quoteRoleRepository) {
        this.quoteRoleRepository = quoteRoleRepository;
    }

    /**
     * Save a quoteRole.
     *
     * @param quoteRole the entity to save.
     * @return the persisted entity.
     */
    public QuoteRole save(QuoteRole quoteRole) {
        LOG.debug("Request to save QuoteRole : {}", quoteRole);
        return quoteRoleRepository.save(quoteRole);
    }

    /**
     * Update a quoteRole.
     *
     * @param quoteRole the entity to save.
     * @return the persisted entity.
     */
    public QuoteRole update(QuoteRole quoteRole) {
        LOG.debug("Request to update QuoteRole : {}", quoteRole);
        return quoteRoleRepository.save(quoteRole);
    }

    /**
     * Partially update a quoteRole.
     *
     * @param quoteRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuoteRole> partialUpdate(QuoteRole quoteRole) {
        LOG.debug("Request to partially update QuoteRole : {}", quoteRole);

        return quoteRoleRepository.findById(quoteRole.getId()).map(quoteRoleRepository::save);
    }

    /**
     * Get all the quoteRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<QuoteRole> findAllWithEagerRelationships(Pageable pageable) {
        return quoteRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one quoteRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuoteRole> findOne(Long id) {
        LOG.debug("Request to get QuoteRole : {}", id);
        return quoteRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the quoteRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QuoteRole : {}", id);
        quoteRoleRepository.deleteById(id);
    }
}
