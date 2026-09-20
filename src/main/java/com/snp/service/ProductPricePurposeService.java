package com.snp.service;

import com.snp.domain.ProductPricePurpose;
import com.snp.repository.ProductPricePurposeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductPricePurpose}.
 */
@Service
@Transactional
public class ProductPricePurposeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPricePurposeService.class);

    private final ProductPricePurposeRepository productPricePurposeRepository;

    public ProductPricePurposeService(ProductPricePurposeRepository productPricePurposeRepository) {
        this.productPricePurposeRepository = productPricePurposeRepository;
    }

    /**
     * Save a productPricePurpose.
     *
     * @param productPricePurpose the entity to save.
     * @return the persisted entity.
     */
    public ProductPricePurpose save(ProductPricePurpose productPricePurpose) {
        LOG.debug("Request to save ProductPricePurpose : {}", productPricePurpose);
        return productPricePurposeRepository.save(productPricePurpose);
    }

    /**
     * Update a productPricePurpose.
     *
     * @param productPricePurpose the entity to save.
     * @return the persisted entity.
     */
    public ProductPricePurpose update(ProductPricePurpose productPricePurpose) {
        LOG.debug("Request to update ProductPricePurpose : {}", productPricePurpose);
        return productPricePurposeRepository.save(productPricePurpose);
    }

    /**
     * Partially update a productPricePurpose.
     *
     * @param productPricePurpose the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductPricePurpose> partialUpdate(ProductPricePurpose productPricePurpose) {
        LOG.debug("Request to partially update ProductPricePurpose : {}", productPricePurpose);

        return productPricePurposeRepository
            .findById(productPricePurpose.getId())
            .map(existingProductPricePurpose -> {
                updateIfPresent(existingProductPricePurpose::setName, productPricePurpose.getName());
                updateIfPresent(existingProductPricePurpose::setDescription, productPricePurpose.getDescription());

                return existingProductPricePurpose;
            })
            .map(productPricePurposeRepository::save);
    }

    /**
     * Get one productPricePurpose by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductPricePurpose> findOne(Long id) {
        LOG.debug("Request to get ProductPricePurpose : {}", id);
        return productPricePurposeRepository.findById(id);
    }

    /**
     * Delete the productPricePurpose by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductPricePurpose : {}", id);
        productPricePurposeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
