package com.snp.service;

import com.snp.domain.ProductFeatureGroup;
import com.snp.repository.ProductFeatureGroupRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFeatureGroup}.
 */
@Service
@Transactional
public class ProductFeatureGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureGroupService.class);

    private final ProductFeatureGroupRepository productFeatureGroupRepository;

    public ProductFeatureGroupService(ProductFeatureGroupRepository productFeatureGroupRepository) {
        this.productFeatureGroupRepository = productFeatureGroupRepository;
    }

    /**
     * Save a productFeatureGroup.
     *
     * @param productFeatureGroup the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureGroup save(ProductFeatureGroup productFeatureGroup) {
        LOG.debug("Request to save ProductFeatureGroup : {}", productFeatureGroup);
        return productFeatureGroupRepository.save(productFeatureGroup);
    }

    /**
     * Update a productFeatureGroup.
     *
     * @param productFeatureGroup the entity to save.
     * @return the persisted entity.
     */
    public ProductFeatureGroup update(ProductFeatureGroup productFeatureGroup) {
        LOG.debug("Request to update ProductFeatureGroup : {}", productFeatureGroup);
        return productFeatureGroupRepository.save(productFeatureGroup);
    }

    /**
     * Partially update a productFeatureGroup.
     *
     * @param productFeatureGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFeatureGroup> partialUpdate(ProductFeatureGroup productFeatureGroup) {
        LOG.debug("Request to partially update ProductFeatureGroup : {}", productFeatureGroup);

        return productFeatureGroupRepository
            .findById(productFeatureGroup.getId())
            .map(existingProductFeatureGroup -> {
                updateIfPresent(existingProductFeatureGroup::setName, productFeatureGroup.getName());
                updateIfPresent(existingProductFeatureGroup::setDescription, productFeatureGroup.getDescription());

                return existingProductFeatureGroup;
            })
            .map(productFeatureGroupRepository::save);
    }

    /**
     * Get one productFeatureGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFeatureGroup> findOne(Long id) {
        LOG.debug("Request to get ProductFeatureGroup : {}", id);
        return productFeatureGroupRepository.findById(id);
    }

    /**
     * Delete the productFeatureGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFeatureGroup : {}", id);
        productFeatureGroupRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
