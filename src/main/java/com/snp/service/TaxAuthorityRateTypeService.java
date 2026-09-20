package com.snp.service;

import com.snp.domain.TaxAuthorityRateType;
import com.snp.repository.TaxAuthorityRateTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.TaxAuthorityRateType}.
 */
@Service
@Transactional
public class TaxAuthorityRateTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(TaxAuthorityRateTypeService.class);

    private final TaxAuthorityRateTypeRepository taxAuthorityRateTypeRepository;

    public TaxAuthorityRateTypeService(TaxAuthorityRateTypeRepository taxAuthorityRateTypeRepository) {
        this.taxAuthorityRateTypeRepository = taxAuthorityRateTypeRepository;
    }

    /**
     * Save a taxAuthorityRateType.
     *
     * @param taxAuthorityRateType the entity to save.
     * @return the persisted entity.
     */
    public TaxAuthorityRateType save(TaxAuthorityRateType taxAuthorityRateType) {
        LOG.debug("Request to save TaxAuthorityRateType : {}", taxAuthorityRateType);
        return taxAuthorityRateTypeRepository.save(taxAuthorityRateType);
    }

    /**
     * Update a taxAuthorityRateType.
     *
     * @param taxAuthorityRateType the entity to save.
     * @return the persisted entity.
     */
    public TaxAuthorityRateType update(TaxAuthorityRateType taxAuthorityRateType) {
        LOG.debug("Request to update TaxAuthorityRateType : {}", taxAuthorityRateType);
        return taxAuthorityRateTypeRepository.save(taxAuthorityRateType);
    }

    /**
     * Partially update a taxAuthorityRateType.
     *
     * @param taxAuthorityRateType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaxAuthorityRateType> partialUpdate(TaxAuthorityRateType taxAuthorityRateType) {
        LOG.debug("Request to partially update TaxAuthorityRateType : {}", taxAuthorityRateType);

        return taxAuthorityRateTypeRepository
            .findById(taxAuthorityRateType.getId())
            .map(existingTaxAuthorityRateType -> {
                updateIfPresent(existingTaxAuthorityRateType::setName, taxAuthorityRateType.getName());
                updateIfPresent(existingTaxAuthorityRateType::setDescription, taxAuthorityRateType.getDescription());

                return existingTaxAuthorityRateType;
            })
            .map(taxAuthorityRateTypeRepository::save);
    }

    /**
     * Get all the taxAuthorityRateTypes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<TaxAuthorityRateType> findAllWithEagerRelationships() {
        return taxAuthorityRateTypeRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one taxAuthorityRateType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaxAuthorityRateType> findOne(Long id) {
        LOG.debug("Request to get TaxAuthorityRateType : {}", id);
        return taxAuthorityRateTypeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the taxAuthorityRateType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TaxAuthorityRateType : {}", id);
        taxAuthorityRateTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
