package com.snp.service;

import com.snp.domain.TaxSlab;
import com.snp.repository.TaxSlabRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.TaxSlab}.
 */
@Service
@Transactional
public class TaxSlabService {

    private static final Logger LOG = LoggerFactory.getLogger(TaxSlabService.class);

    private final TaxSlabRepository taxSlabRepository;

    public TaxSlabService(TaxSlabRepository taxSlabRepository) {
        this.taxSlabRepository = taxSlabRepository;
    }

    /**
     * Save a taxSlab.
     *
     * @param taxSlab the entity to save.
     * @return the persisted entity.
     */
    public TaxSlab save(TaxSlab taxSlab) {
        LOG.debug("Request to save TaxSlab : {}", taxSlab);
        return taxSlabRepository.save(taxSlab);
    }

    /**
     * Update a taxSlab.
     *
     * @param taxSlab the entity to save.
     * @return the persisted entity.
     */
    public TaxSlab update(TaxSlab taxSlab) {
        LOG.debug("Request to update TaxSlab : {}", taxSlab);
        return taxSlabRepository.save(taxSlab);
    }

    /**
     * Partially update a taxSlab.
     *
     * @param taxSlab the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaxSlab> partialUpdate(TaxSlab taxSlab) {
        LOG.debug("Request to partially update TaxSlab : {}", taxSlab);

        return taxSlabRepository
            .findById(taxSlab.getId())
            .map(existingTaxSlab -> {
                updateIfPresent(existingTaxSlab::setName, taxSlab.getName());
                updateIfPresent(existingTaxSlab::setRate, taxSlab.getRate());

                return existingTaxSlab;
            })
            .map(taxSlabRepository::save);
    }

    /**
     * Get one taxSlab by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaxSlab> findOne(Long id) {
        LOG.debug("Request to get TaxSlab : {}", id);
        return taxSlabRepository.findById(id);
    }

    /**
     * Delete the taxSlab by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TaxSlab : {}", id);
        taxSlabRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
