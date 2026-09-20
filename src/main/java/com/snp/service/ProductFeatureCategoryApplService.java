package com.snp.service;

import com.snp.domain.ProductFeatureCategoryAppl;
import com.snp.repository.ProductFeatureCategoryApplRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeatureCategoryAppl}.
 */
@Service
@Transactional
public class ProductFeatureCategoryApplService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureCategoryApplService.class);

    private final ProductFeatureCategoryApplRepository productFeatureCategoryApplRepository;

    public ProductFeatureCategoryApplService(ProductFeatureCategoryApplRepository productFeatureCategoryApplRepository) {
        this.productFeatureCategoryApplRepository = productFeatureCategoryApplRepository;
    }

    /**
     * Save a productFeatureCategoryAppl.
     *
     * @param productFeatureCategoryAppl the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureCategoryAppl save(ProductFeatureCategoryAppl productFeatureCategoryAppl) {
        LOG.debug("Request to save ProductFeatureCategoryAppl : {}", productFeatureCategoryAppl);
        return productFeatureCategoryApplRepository.save(productFeatureCategoryAppl);
    }

    /**
     * Update a productFeatureCategoryAppl.
     *
     * @param productFeatureCategoryAppl the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureCategoryAppl update(ProductFeatureCategoryAppl productFeatureCategoryAppl) {
        LOG.debug("Request to update ProductFeatureCategoryAppl : {}", productFeatureCategoryAppl);
        return productFeatureCategoryApplRepository.save(productFeatureCategoryAppl);
    }

    /**
     * Partially update a productFeatureCategoryAppl.
     *
     * @param productFeatureCategoryAppl the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeatureCategoryAppl> partialUpdate(ProductFeatureCategoryAppl productFeatureCategoryAppl) {
        LOG.debug("Request to partially update ProductFeatureCategoryAppl : {}", productFeatureCategoryAppl);

        return productFeatureCategoryApplRepository
            .findById(productFeatureCategoryAppl.getId())
            .map(existingProductFeatureCategoryAppl -> {
                updateIfPresent(existingProductFeatureCategoryAppl::setFromDate, productFeatureCategoryAppl.getFromDate());
                updateIfPresent(existingProductFeatureCategoryAppl::setThruDate, productFeatureCategoryAppl.getThruDate());

                return existingProductFeatureCategoryAppl;
            })
            .map(productFeatureCategoryApplRepository::save);
    }

    /**
     * Get all the productFeatureCategoryAppls with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductFeatureCategoryAppl> findAllWithEagerRelationships(Pageable pageable) {
        return productFeatureCategoryApplRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productFeatureCategoryAppl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeatureCategoryAppl> findOne(Long id) {
        LOG.debug("Request to get ProductFeatureCategoryAppl : {}", id);
        return productFeatureCategoryApplRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productFeatureCategoryAppl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeatureCategoryAppl : {}", id);
        productFeatureCategoryApplRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
