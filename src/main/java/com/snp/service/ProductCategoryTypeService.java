package com.snp.service;

import com.snp.domain.ProductCategoryType;
import com.snp.repository.ProductCategoryTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductCategoryType}.
 */
@Service
@Transactional
public class ProductCategoryTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryTypeService.class);

    private final ProductCategoryTypeRepository productCategoryTypeRepository;

    public ProductCategoryTypeService(ProductCategoryTypeRepository productCategoryTypeRepository) {
        this.productCategoryTypeRepository = productCategoryTypeRepository;
    }

    /**
     * Save a productCategoryType.
     *
     * @param productCategoryType the entity to save.
     * @return the persisted entity.
     */
    public ProductCategoryType save(ProductCategoryType productCategoryType) {
        LOG.debug("Request to save ProductCategoryType : {}", productCategoryType);
        return productCategoryTypeRepository.save(productCategoryType);
    }

    /**
     * Update a productCategoryType.
     *
     * @param productCategoryType the entity to save.
     * @return the persisted entity.
     */
    public ProductCategoryType update(ProductCategoryType productCategoryType) {
        LOG.debug("Request to update ProductCategoryType : {}", productCategoryType);
        return productCategoryTypeRepository.save(productCategoryType);
    }

    /**
     * Partially update a productCategoryType.
     *
     * @param productCategoryType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductCategoryType> partialUpdate(ProductCategoryType productCategoryType) {
        LOG.debug("Request to partially update ProductCategoryType : {}", productCategoryType);

        return productCategoryTypeRepository
            .findById(productCategoryType.getId())
            .map(existingProductCategoryType -> {
                updateIfPresent(existingProductCategoryType::setName, productCategoryType.getName());
                updateIfPresent(existingProductCategoryType::setDescription, productCategoryType.getDescription());

                return existingProductCategoryType;
            })
            .map(productCategoryTypeRepository::save);
    }

    /**
     * Get one productCategoryType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductCategoryType> findOne(Long id) {
        LOG.debug("Request to get ProductCategoryType : {}", id);
        return productCategoryTypeRepository.findById(id);
    }

    /**
     * Delete the productCategoryType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductCategoryType : {}", id);
        productCategoryTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
