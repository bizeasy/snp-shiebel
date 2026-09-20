package com.snp.service;

import com.snp.domain.ProductFacilityLocation;
import com.snp.repository.ProductFacilityLocationRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFacilityLocation}.
 */
@Service
@Transactional
public class ProductFacilityLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityLocationService.class);

    private final ProductFacilityLocationRepository productFacilityLocationRepository;

    public ProductFacilityLocationService(ProductFacilityLocationRepository productFacilityLocationRepository) {
        this.productFacilityLocationRepository = productFacilityLocationRepository;
    }

    /**
     * Save a productFacilityLocation.
     *
     * @param productFacilityLocation the entity to save.
     * @return the persisted entity.
     */
    public ProductFacilityLocation save(ProductFacilityLocation productFacilityLocation) {
        LOG.debug("Request to save ProductFacilityLocation : {}", productFacilityLocation);
        return productFacilityLocationRepository.save(productFacilityLocation);
    }

    /**
     * Update a productFacilityLocation.
     *
     * @param productFacilityLocation the entity to save.
     * @return the persisted entity.
     */
    public ProductFacilityLocation update(ProductFacilityLocation productFacilityLocation) {
        LOG.debug("Request to update ProductFacilityLocation : {}", productFacilityLocation);
        return productFacilityLocationRepository.save(productFacilityLocation);
    }

    /**
     * Partially update a productFacilityLocation.
     *
     * @param productFacilityLocation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFacilityLocation> partialUpdate(ProductFacilityLocation productFacilityLocation) {
        LOG.debug("Request to partially update ProductFacilityLocation : {}", productFacilityLocation);

        return productFacilityLocationRepository
            .findById(productFacilityLocation.getId())
            .map(existingProductFacilityLocation -> {
                updateIfPresent(existingProductFacilityLocation::setMinimumStock, productFacilityLocation.getMinimumStock());
                updateIfPresent(existingProductFacilityLocation::setMoveQuantity, productFacilityLocation.getMoveQuantity());

                return existingProductFacilityLocation;
            })
            .map(productFacilityLocationRepository::save);
    }

    /**
     * Get all the productFacilityLocations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductFacilityLocation> findAllWithEagerRelationships(Pageable pageable) {
        return productFacilityLocationRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productFacilityLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFacilityLocation> findOne(Long id) {
        LOG.debug("Request to get ProductFacilityLocation : {}", id);
        return productFacilityLocationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productFacilityLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFacilityLocation : {}", id);
        productFacilityLocationRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
