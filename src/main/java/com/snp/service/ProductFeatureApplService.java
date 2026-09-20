package com.snp.service;

import com.snp.domain.ProductFeatureAppl;
import com.snp.repository.ProductFeatureApplRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeatureAppl}.
 */
@Service
@Transactional
public class ProductFeatureApplService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureApplService.class);

    private final ProductFeatureApplRepository productFeatureApplRepository;

    public ProductFeatureApplService(ProductFeatureApplRepository productFeatureApplRepository) {
        this.productFeatureApplRepository = productFeatureApplRepository;
    }

    /**
     * Save a productFeatureAppl.
     *
     * @param productFeatureAppl the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureAppl save(ProductFeatureAppl productFeatureAppl) {
        LOG.debug("Request to save ProductFeatureAppl : {}", productFeatureAppl);
        return productFeatureApplRepository.save(productFeatureAppl);
    }

    /**
     * Update a productFeatureAppl.
     *
     * @param productFeatureAppl the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureAppl update(ProductFeatureAppl productFeatureAppl) {
        LOG.debug("Request to update ProductFeatureAppl : {}", productFeatureAppl);
        return productFeatureApplRepository.save(productFeatureAppl);
    }

    /**
     * Partially update a productFeatureAppl.
     *
     * @param productFeatureAppl the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeatureAppl> partialUpdate(ProductFeatureAppl productFeatureAppl) {
        LOG.debug("Request to partially update ProductFeatureAppl : {}", productFeatureAppl);

        return productFeatureApplRepository
            .findById(productFeatureAppl.getId())
            .map(existingProductFeatureAppl -> {
                updateIfPresent(existingProductFeatureAppl::setFromDate, productFeatureAppl.getFromDate());
                updateIfPresent(existingProductFeatureAppl::setThruDate, productFeatureAppl.getThruDate());
                updateIfPresent(existingProductFeatureAppl::setSequenceNo, productFeatureAppl.getSequenceNo());
                updateIfPresent(existingProductFeatureAppl::setAmount, productFeatureAppl.getAmount());

                return existingProductFeatureAppl;
            })
            .map(productFeatureApplRepository::save);
    }

    /**
     * Get all the productFeatureAppls with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductFeatureAppl> findAllWithEagerRelationships(Pageable pageable) {
        return productFeatureApplRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productFeatureAppl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeatureAppl> findOne(Long id) {
        LOG.debug("Request to get ProductFeatureAppl : {}", id);
        return productFeatureApplRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productFeatureAppl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeatureAppl : {}", id);
        productFeatureApplRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
