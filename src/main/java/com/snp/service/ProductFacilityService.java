package com.snp.service;

import com.snp.domain.ProductFacility;
import com.snp.repository.ProductFacilityRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFacility}.
 */
@Service
@Transactional
public class ProductFacilityService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityService.class);

    private final ProductFacilityRepository productFacilityRepository;

    public ProductFacilityService(ProductFacilityRepository productFacilityRepository) {
        this.productFacilityRepository = productFacilityRepository;
    }

    /**
     * Save a productFacility.
     *
     * @param productFacility the entity to save.
     * @return the persisted entity.
     */
    public ProductFacility save(ProductFacility productFacility) {
        LOG.debug("Request to save ProductFacility : {}", productFacility);
        return productFacilityRepository.save(productFacility);
    }

    /**
     * Update a productFacility.
     *
     * @param productFacility the entity to save.
     * @return the persisted entity.
     */
    public ProductFacility update(ProductFacility productFacility) {
        LOG.debug("Request to update ProductFacility : {}", productFacility);
        return productFacilityRepository.save(productFacility);
    }

    /**
     * Partially update a productFacility.
     *
     * @param productFacility the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFacility> partialUpdate(ProductFacility productFacility) {
        LOG.debug("Request to partially update ProductFacility : {}", productFacility);

        return productFacilityRepository
            .findById(productFacility.getId())
            .map(existingProductFacility -> {
                updateIfPresent(existingProductFacility::setMinimumStock, productFacility.getMinimumStock());
                updateIfPresent(existingProductFacility::setReorderQty, productFacility.getReorderQty());
                updateIfPresent(existingProductFacility::setDaysToShip, productFacility.getDaysToShip());
                updateIfPresent(existingProductFacility::setLastInventoryCount, productFacility.getLastInventoryCount());

                return existingProductFacility;
            })
            .map(productFacilityRepository::save);
    }

    /**
     * Get all the productFacilities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductFacility> findAllWithEagerRelationships(Pageable pageable) {
        return productFacilityRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productFacility by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFacility> findOne(Long id) {
        LOG.debug("Request to get ProductFacility : {}", id);
        return productFacilityRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productFacility by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFacility : {}", id);
        productFacilityRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
