package com.snp.service;

import com.snp.domain.ProductStore;
import com.snp.repository.ProductStoreRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductStore}.
 */
@Service
@Transactional
public class ProductStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductStoreService.class);

    private final ProductStoreRepository productStoreRepository;

    public ProductStoreService(ProductStoreRepository productStoreRepository) {
        this.productStoreRepository = productStoreRepository;
    }

    /**
     * Save a productStore.
     *
     * @param productStore the entity to save.
     * @return the persisted entity.
     */
    public ProductStore save(ProductStore productStore) {
        LOG.debug("Request to save ProductStore : {}", productStore);
        return productStoreRepository.save(productStore);
    }

    /**
     * Update a productStore.
     *
     * @param productStore the entity to save.
     * @return the persisted entity.
     */
    public ProductStore update(ProductStore productStore) {
        LOG.debug("Request to update ProductStore : {}", productStore);
        return productStoreRepository.save(productStore);
    }

    /**
     * Partially update a productStore.
     *
     * @param productStore the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductStore> partialUpdate(ProductStore productStore) {
        LOG.debug("Request to partially update ProductStore : {}", productStore);

        return productStoreRepository
            .findById(productStore.getId())
            .map(existingProductStore -> {
                updateIfPresent(existingProductStore::setName, productStore.getName());
                updateIfPresent(existingProductStore::setTitle, productStore.getTitle());
                updateIfPresent(existingProductStore::setPostalAddress, productStore.getPostalAddress());

                return existingProductStore;
            })
            .map(productStoreRepository::save);
    }

    /**
     * Get one productStore by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductStore> findOne(Long id) {
        LOG.debug("Request to get ProductStore : {}", id);
        return productStoreRepository.findById(id);
    }

    /**
     * Delete the productStore by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductStore : {}", id);
        productStoreRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
