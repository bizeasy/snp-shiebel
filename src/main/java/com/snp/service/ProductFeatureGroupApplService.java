package com.snp.service;

import com.snp.domain.ProductFeatureGroupAppl;
import com.snp.repository.ProductFeatureGroupApplRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeatureGroupAppl}.
 */
@Service
@Transactional
public class ProductFeatureGroupApplService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureGroupApplService.class);

    private final ProductFeatureGroupApplRepository productFeatureGroupApplRepository;

    public ProductFeatureGroupApplService(ProductFeatureGroupApplRepository productFeatureGroupApplRepository) {
        this.productFeatureGroupApplRepository = productFeatureGroupApplRepository;
    }

    /**
     * Save a productFeatureGroupAppl.
     *
     * @param productFeatureGroupAppl the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureGroupAppl save(ProductFeatureGroupAppl productFeatureGroupAppl) {
        LOG.debug("Request to save ProductFeatureGroupAppl : {}", productFeatureGroupAppl);
        return productFeatureGroupApplRepository.save(productFeatureGroupAppl);
    }

    /**
     * Update a productFeatureGroupAppl.
     *
     * @param productFeatureGroupAppl the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureGroupAppl update(ProductFeatureGroupAppl productFeatureGroupAppl) {
        LOG.debug("Request to update ProductFeatureGroupAppl : {}", productFeatureGroupAppl);
        return productFeatureGroupApplRepository.save(productFeatureGroupAppl);
    }

    /**
     * Partially update a productFeatureGroupAppl.
     *
     * @param productFeatureGroupAppl the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeatureGroupAppl> partialUpdate(ProductFeatureGroupAppl productFeatureGroupAppl) {
        LOG.debug("Request to partially update ProductFeatureGroupAppl : {}", productFeatureGroupAppl);

        return productFeatureGroupApplRepository
            .findById(productFeatureGroupAppl.getId())
            .map(existingProductFeatureGroupAppl -> {
                updateIfPresent(existingProductFeatureGroupAppl::setFromDate, productFeatureGroupAppl.getFromDate());
                updateIfPresent(existingProductFeatureGroupAppl::setThruDate, productFeatureGroupAppl.getThruDate());
                updateIfPresent(existingProductFeatureGroupAppl::setSequenceNo, productFeatureGroupAppl.getSequenceNo());

                return existingProductFeatureGroupAppl;
            })
            .map(productFeatureGroupApplRepository::save);
    }

    /**
     * Get all the productFeatureGroupAppls with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductFeatureGroupAppl> findAllWithEagerRelationships(Pageable pageable) {
        return productFeatureGroupApplRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productFeatureGroupAppl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeatureGroupAppl> findOne(Long id) {
        LOG.debug("Request to get ProductFeatureGroupAppl : {}", id);
        return productFeatureGroupApplRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productFeatureGroupAppl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeatureGroupAppl : {}", id);
        productFeatureGroupApplRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
