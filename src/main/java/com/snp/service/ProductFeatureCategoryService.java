package com.snp.service;

import com.snp.domain.ProductFeatureCategory;
import com.snp.repository.ProductFeatureCategoryRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeatureCategory}.
 */
@Service
@Transactional
public class ProductFeatureCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureCategoryService.class);

    private final ProductFeatureCategoryRepository productFeatureCategoryRepository;

    public ProductFeatureCategoryService(ProductFeatureCategoryRepository productFeatureCategoryRepository) {
        this.productFeatureCategoryRepository = productFeatureCategoryRepository;
    }

    /**
     * Save a productFeatureCategory.
     *
     * @param productFeatureCategory the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureCategory save(ProductFeatureCategory productFeatureCategory) {
        LOG.debug("Request to save ProductFeatureCategory : {}", productFeatureCategory);
        return productFeatureCategoryRepository.save(productFeatureCategory);
    }

    /**
     * Update a productFeatureCategory.
     *
     * @param productFeatureCategory the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureCategory update(ProductFeatureCategory productFeatureCategory) {
        LOG.debug("Request to update ProductFeatureCategory : {}", productFeatureCategory);
        return productFeatureCategoryRepository.save(productFeatureCategory);
    }

    /**
     * Partially update a productFeatureCategory.
     *
     * @param productFeatureCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeatureCategory> partialUpdate(ProductFeatureCategory productFeatureCategory) {
        LOG.debug("Request to partially update ProductFeatureCategory : {}", productFeatureCategory);

        return productFeatureCategoryRepository
            .findById(productFeatureCategory.getId())
            .map(existingProductFeatureCategory -> {
                updateIfPresent(existingProductFeatureCategory::setName, productFeatureCategory.getName());
                updateIfPresent(existingProductFeatureCategory::setDescription, productFeatureCategory.getDescription());

                return existingProductFeatureCategory;
            })
            .map(productFeatureCategoryRepository::save);
    }

    /**
     * Get one productFeatureCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeatureCategory> findOne(Long id) {
        LOG.debug("Request to get ProductFeatureCategory : {}", id);
        return productFeatureCategoryRepository.findById(id);
    }

    /**
     * Delete the productFeatureCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeatureCategory : {}", id);
        productFeatureCategoryRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
