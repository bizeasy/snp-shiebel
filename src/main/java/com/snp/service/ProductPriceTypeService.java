package com.snp.service;

import com.snp.domain.ProductPriceType;
import com.snp.repository.ProductPriceTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductPriceType}.
 */
@Service
@Transactional
public class ProductPriceTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceTypeService.class);

    private final ProductPriceTypeRepository productPriceTypeRepository;

    public ProductPriceTypeService(ProductPriceTypeRepository productPriceTypeRepository) {
        this.productPriceTypeRepository = productPriceTypeRepository;
    }

    /**
     * Save a productPriceType.
     *
     * @param productPriceType the entity to save.
     * @return the persisted entity.
     */
    public ProductPriceType save(ProductPriceType productPriceType) {
        LOG.debug("Request to save ProductPriceType : {}", productPriceType);
        return productPriceTypeRepository.save(productPriceType);
    }

    /**
     * Update a productPriceType.
     *
     * @param productPriceType the entity to save.
     * @return the persisted entity.
     */
    public ProductPriceType update(ProductPriceType productPriceType) {
        LOG.debug("Request to update ProductPriceType : {}", productPriceType);
        return productPriceTypeRepository.save(productPriceType);
    }

    /**
     * Partially update a productPriceType.
     *
     * @param productPriceType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductPriceType> partialUpdate(ProductPriceType productPriceType) {
        LOG.debug("Request to partially update ProductPriceType : {}", productPriceType);

        return productPriceTypeRepository
            .findById(productPriceType.getId())
            .map(existingProductPriceType -> {
                updateIfPresent(existingProductPriceType::setName, productPriceType.getName());
                updateIfPresent(existingProductPriceType::setDescription, productPriceType.getDescription());

                return existingProductPriceType;
            })
            .map(productPriceTypeRepository::save);
    }

    /**
     * Get one productPriceType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductPriceType> findOne(Long id) {
        LOG.debug("Request to get ProductPriceType : {}", id);
        return productPriceTypeRepository.findById(id);
    }

    /**
     * Delete the productPriceType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductPriceType : {}", id);
        productPriceTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
