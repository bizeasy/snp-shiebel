package com.snp.service;

import com.snp.domain.ProductFeatureType;
import com.snp.repository.ProductFeatureTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeatureType}.
 */
@Service
@Transactional
public class ProductFeatureTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureTypeService.class);

    private final ProductFeatureTypeRepository productFeatureTypeRepository;

    public ProductFeatureTypeService(ProductFeatureTypeRepository productFeatureTypeRepository) {
        this.productFeatureTypeRepository = productFeatureTypeRepository;
    }

    /**
     * Save a productFeatureType.
     *
     * @param productFeatureType the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureType save(ProductFeatureType productFeatureType) {
        LOG.debug("Request to save ProductFeatureType : {}", productFeatureType);
        return productFeatureTypeRepository.save(productFeatureType);
    }

    /**
     * Update a productFeatureType.
     *
     * @param productFeatureType the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureType update(ProductFeatureType productFeatureType) {
        LOG.debug("Request to update ProductFeatureType : {}", productFeatureType);
        return productFeatureTypeRepository.save(productFeatureType);
    }

    /**
     * Partially update a productFeatureType.
     *
     * @param productFeatureType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeatureType> partialUpdate(ProductFeatureType productFeatureType) {
        LOG.debug("Request to partially update ProductFeatureType : {}", productFeatureType);

        return productFeatureTypeRepository
            .findById(productFeatureType.getId())
            .map(existingProductFeatureType -> {
                updateIfPresent(existingProductFeatureType::setName, productFeatureType.getName());
                updateIfPresent(existingProductFeatureType::setDescription, productFeatureType.getDescription());

                return existingProductFeatureType;
            })
            .map(productFeatureTypeRepository::save);
    }

    /**
     * Get one productFeatureType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeatureType> findOne(Long id) {
        LOG.debug("Request to get ProductFeatureType : {}", id);
        return productFeatureTypeRepository.findById(id);
    }

    /**
     * Delete the productFeatureType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeatureType : {}", id);
        productFeatureTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
