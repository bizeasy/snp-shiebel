package com.snp.service;

import com.snp.domain.ProductFacilityUser;
import com.snp.repository.ProductFacilityUserRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductFacilityUser}.
 */
@Service
@Transactional
public class ProductFacilityUserService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityUserService.class);

    private final ProductFacilityUserRepository productFacilityUserRepository;

    public ProductFacilityUserService(ProductFacilityUserRepository productFacilityUserRepository) {
        this.productFacilityUserRepository = productFacilityUserRepository;
    }

    /**
     * Save a productFacilityUser.
     *
     * @param productFacilityUser the entity to save.
     * @return the persisted entity.
     */
    public ProductFacilityUser save(ProductFacilityUser productFacilityUser) {
        LOG.debug("Request to save ProductFacilityUser : {}", productFacilityUser);
        return productFacilityUserRepository.save(productFacilityUser);
    }

    /**
     * Update a productFacilityUser.
     *
     * @param productFacilityUser the entity to save.
     * @return the persisted entity.
     */
    public ProductFacilityUser update(ProductFacilityUser productFacilityUser) {
        LOG.debug("Request to update ProductFacilityUser : {}", productFacilityUser);
        return productFacilityUserRepository.save(productFacilityUser);
    }

    /**
     * Partially update a productFacilityUser.
     *
     * @param productFacilityUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductFacilityUser> partialUpdate(ProductFacilityUser productFacilityUser) {
        LOG.debug("Request to partially update ProductFacilityUser : {}", productFacilityUser);

        return productFacilityUserRepository
            .findById(productFacilityUser.getId())
            .map(existingProductFacilityUser -> {
                updateIfPresent(existingProductFacilityUser::setFromDate, productFacilityUser.getFromDate());
                updateIfPresent(existingProductFacilityUser::setThruDate, productFacilityUser.getThruDate());
                updateIfPresent(existingProductFacilityUser::setCostPerDay, productFacilityUser.getCostPerDay());
                updateIfPresent(existingProductFacilityUser::setTotalCost, productFacilityUser.getTotalCost());
                updateIfPresent(existingProductFacilityUser::setDiscount, productFacilityUser.getDiscount());

                return existingProductFacilityUser;
            })
            .map(productFacilityUserRepository::save);
    }

    /**
     * Get all the productFacilityUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductFacilityUser> findAllWithEagerRelationships(Pageable pageable) {
        return productFacilityUserRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productFacilityUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductFacilityUser> findOne(Long id) {
        LOG.debug("Request to get ProductFacilityUser : {}", id);
        return productFacilityUserRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productFacilityUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductFacilityUser : {}", id);
        productFacilityUserRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
