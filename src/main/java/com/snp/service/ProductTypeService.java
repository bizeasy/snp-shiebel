package com.snp.service;

import com.snp.domain.ProductType;
import com.snp.repository.ProductTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductType}.
 */
@Service
@Transactional
public class ProductTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductTypeService.class);

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    /**
     * Save a productType.
     *
     * @param productType the entity to save.
     * @return the persisted entity.
     */
    public ProductType save(ProductType productType) {
        LOG.debug("Request to save ProductType : {}", productType);
        return productTypeRepository.save(productType);
    }

    /**
     * Update a productType.
     *
     * @param productType the entity to save.
     * @return the persisted entity.
     */
    public ProductType update(ProductType productType) {
        LOG.debug("Request to update ProductType : {}", productType);
        return productTypeRepository.save(productType);
    }

    /**
     * Partially update a productType.
     *
     * @param productType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductType> partialUpdate(ProductType productType) {
        LOG.debug("Request to partially update ProductType : {}", productType);

        return productTypeRepository
            .findById(productType.getId())
            .map(existingProductType -> {
                updateIfPresent(existingProductType::setName, productType.getName());
                updateIfPresent(existingProductType::setDescription, productType.getDescription());

                return existingProductType;
            })
            .map(productTypeRepository::save);
    }

    /**
     * Get one productType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductType> findOne(Long id) {
        LOG.debug("Request to get ProductType : {}", id);
        return productTypeRepository.findById(id);
    }

    /**
     * Delete the productType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductType : {}", id);
        productTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
