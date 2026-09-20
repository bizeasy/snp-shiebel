package com.snp.service;

import com.snp.domain.ProductAssocType;
import com.snp.repository.ProductAssocTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductAssocType}.
 */
@Service
@Transactional
public class ProductAssocTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAssocTypeService.class);

    private final ProductAssocTypeRepository productAssocTypeRepository;

    public ProductAssocTypeService(ProductAssocTypeRepository productAssocTypeRepository) {
        this.productAssocTypeRepository = productAssocTypeRepository;
    }

    /**
     * Save a productAssocType.
     *
     * @param productAssocType the entity to save.
     * @return the persisted entity.
     */
    public ProductAssocType save(ProductAssocType productAssocType) {
        LOG.debug("Request to save ProductAssocType : {}", productAssocType);
        return productAssocTypeRepository.save(productAssocType);
    }

    /**
     * Update a productAssocType.
     *
     * @param productAssocType the entity to save.
     * @return the persisted entity.
     */
    public ProductAssocType update(ProductAssocType productAssocType) {
        LOG.debug("Request to update ProductAssocType : {}", productAssocType);
        return productAssocTypeRepository.save(productAssocType);
    }

    /**
     * Partially update a productAssocType.
     *
     * @param productAssocType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductAssocType> partialUpdate(ProductAssocType productAssocType) {
        LOG.debug("Request to partially update ProductAssocType : {}", productAssocType);

        return productAssocTypeRepository
            .findById(productAssocType.getId())
            .map(existingProductAssocType -> {
                updateIfPresent(existingProductAssocType::setName, productAssocType.getName());
                updateIfPresent(existingProductAssocType::setDescription, productAssocType.getDescription());

                return existingProductAssocType;
            })
            .map(productAssocTypeRepository::save);
    }

    /**
     * Get one productAssocType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductAssocType> findOne(Long id) {
        LOG.debug("Request to get ProductAssocType : {}", id);
        return productAssocTypeRepository.findById(id);
    }

    /**
     * Delete the productAssocType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductAssocType : {}", id);
        productAssocTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
