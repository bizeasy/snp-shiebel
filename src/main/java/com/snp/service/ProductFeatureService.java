package com.snp.service;

import com.snp.domain.ProductFeature;
import com.snp.repository.ProductFeatureRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeature}.
 */
@Service
@Transactional
public class ProductFeatureService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureService.class);

    private final ProductFeatureRepository productFeatureRepository;

    public ProductFeatureService(ProductFeatureRepository productFeatureRepository) {
        this.productFeatureRepository = productFeatureRepository;
    }

    /**
     * Save a productFeature.
     *
     * @param productFeature the entity to save.
     * @return the persisted entity.
     */
    public ProductFeature save(ProductFeature productFeature) {
        LOG.debug("Request to save ProductFeature : {}", productFeature);
        return productFeatureRepository.save(productFeature);
    }

    /**
     * Update a productFeature.
     *
     * @param productFeature the entity to save.
     * @return the persisted entity.
     */
    public ProductFeature update(ProductFeature productFeature) {
        LOG.debug("Request to update ProductFeature : {}", productFeature);
        return productFeatureRepository.save(productFeature);
    }

    /**
     * Partially update a productFeature.
     *
     * @param productFeature the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeature> partialUpdate(ProductFeature productFeature) {
        LOG.debug("Request to partially update ProductFeature : {}", productFeature);

        return productFeatureRepository
            .findById(productFeature.getId())
            .map(existingProductFeature -> {
                updateIfPresent(existingProductFeature::setName, productFeature.getName());
                updateIfPresent(existingProductFeature::setDescription, productFeature.getDescription());
                updateIfPresent(existingProductFeature::setAmount, productFeature.getAmount());
                updateIfPresent(existingProductFeature::setSequenceNo, productFeature.getSequenceNo());
                updateIfPresent(existingProductFeature::setAbbrev, productFeature.getAbbrev());

                return existingProductFeature;
            })
            .map(productFeatureRepository::save);
    }

    /**
     * Get all the productFeatures with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductFeature> findAllWithEagerRelationships(Pageable pageable) {
        return productFeatureRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productFeature by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeature> findOne(Long id) {
        LOG.debug("Request to get ProductFeature : {}", id);
        return productFeatureRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productFeature by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeature : {}", id);
        productFeatureRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
