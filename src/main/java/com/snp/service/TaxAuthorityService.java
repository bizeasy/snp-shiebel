package com.snp.service;

import com.snp.domain.TaxAuthority;
import com.snp.repository.TaxAuthorityRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.TaxAuthority}.
 */
@Service
@Transactional
public class TaxAuthorityService {

    private static final Logger LOG = LoggerFactory.getLogger(TaxAuthorityService.class);

    private final TaxAuthorityRepository taxAuthorityRepository;

    public TaxAuthorityService(TaxAuthorityRepository taxAuthorityRepository) {
        this.taxAuthorityRepository = taxAuthorityRepository;
    }

    /**
     * Save a taxAuthority.
     *
     * @param taxAuthority the entity to save.
     * @return the persisted entity.
     */
    public TaxAuthority save(TaxAuthority taxAuthority) {
        LOG.debug("Request to save TaxAuthority : {}", taxAuthority);
        return taxAuthorityRepository.save(taxAuthority);
    }

    /**
     * Update a taxAuthority.
     *
     * @param taxAuthority the entity to save.
     * @return the persisted entity.
     */
    public TaxAuthority update(TaxAuthority taxAuthority) {
        LOG.debug("Request to update TaxAuthority : {}", taxAuthority);
        return taxAuthorityRepository.save(taxAuthority);
    }

    /**
     * Partially update a taxAuthority.
     *
     * @param taxAuthority the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaxAuthority> partialUpdate(TaxAuthority taxAuthority) {
        LOG.debug("Request to partially update TaxAuthority : {}", taxAuthority);

        return taxAuthorityRepository
            .findById(taxAuthority.getId())
            .map(existingTaxAuthority -> {
                updateIfPresent(existingTaxAuthority::setName, taxAuthority.getName());

                return existingTaxAuthority;
            })
            .map(taxAuthorityRepository::save);
    }

    /**
     * Get all the taxAuthorities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<TaxAuthority> findAllWithEagerRelationships() {
        return taxAuthorityRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one taxAuthority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaxAuthority> findOne(Long id) {
        LOG.debug("Request to get TaxAuthority : {}", id);
        return taxAuthorityRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the taxAuthority by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TaxAuthority : {}", id);
        taxAuthorityRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
