package com.snp.service;

import com.snp.domain.ProductFeatureApplType;
import com.snp.repository.ProductFeatureApplTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeatureApplType}.
 */
@Service
@Transactional
public class ProductFeatureApplTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureApplTypeService.class);

    private final ProductFeatureApplTypeRepository productFeatureApplTypeRepository;

    public ProductFeatureApplTypeService(ProductFeatureApplTypeRepository productFeatureApplTypeRepository) {
        this.productFeatureApplTypeRepository = productFeatureApplTypeRepository;
    }

    /**
     * Save a productFeatureApplType.
     *
     * @param productFeatureApplType the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureApplType save(ProductFeatureApplType productFeatureApplType) {
        LOG.debug("Request to save ProductFeatureApplType : {}", productFeatureApplType);
        return productFeatureApplTypeRepository.save(productFeatureApplType);
    }

    /**
     * Update a productFeatureApplType.
     *
     * @param productFeatureApplType the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureApplType update(ProductFeatureApplType productFeatureApplType) {
        LOG.debug("Request to update ProductFeatureApplType : {}", productFeatureApplType);
        return productFeatureApplTypeRepository.save(productFeatureApplType);
    }

    /**
     * Partially update a productFeatureApplType.
     *
     * @param productFeatureApplType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeatureApplType> partialUpdate(ProductFeatureApplType productFeatureApplType) {
        LOG.debug("Request to partially update ProductFeatureApplType : {}", productFeatureApplType);

        return productFeatureApplTypeRepository
            .findById(productFeatureApplType.getId())
            .map(existingProductFeatureApplType -> {
                updateIfPresent(existingProductFeatureApplType::setName, productFeatureApplType.getName());
                updateIfPresent(existingProductFeatureApplType::setDescription, productFeatureApplType.getDescription());

                return existingProductFeatureApplType;
            })
            .map(productFeatureApplTypeRepository::save);
    }

    /**
     * Get one productFeatureApplType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeatureApplType> findOne(Long id) {
        LOG.debug("Request to get ProductFeatureApplType : {}", id);
        return productFeatureApplTypeRepository.findById(id);
    }

    /**
     * Delete the productFeatureApplType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeatureApplType : {}", id);
        productFeatureApplTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
