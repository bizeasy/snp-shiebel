package com.snp.service;

import com.snp.domain.ProductCategory;
import com.snp.repository.ProductCategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductCategory}.
 */
@Service
@Transactional
public class ProductCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * Save a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    public ProductCategory save(ProductCategory productCategory) {
        LOG.debug("Request to save ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Update a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    public ProductCategory update(ProductCategory productCategory) {
        LOG.debug("Request to update ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Partially update a productCategory.
     *
     * @param productCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductCategory> partialUpdate(ProductCategory productCategory) {
        LOG.debug("Request to partially update ProductCategory : {}", productCategory);

        return productCategoryRepository
            .findById(productCategory.getId())
            .map(existingProductCategory -> {
                updateIfPresent(existingProductCategory::setName, productCategory.getName());
                updateIfPresent(existingProductCategory::setDescription, productCategory.getDescription());
                updateIfPresent(existingProductCategory::setLongDescription, productCategory.getLongDescription());
                updateIfPresent(existingProductCategory::setAttribute, productCategory.getAttribute());
                updateIfPresent(existingProductCategory::setSequenceNo, productCategory.getSequenceNo());
                updateIfPresent(existingProductCategory::setImageUrl, productCategory.getImageUrl());
                updateIfPresent(existingProductCategory::setAltImageUrl, productCategory.getAltImageUrl());
                updateIfPresent(existingProductCategory::setBannerUrl, productCategory.getBannerUrl());
                updateIfPresent(existingProductCategory::setLargeImageUrl, productCategory.getLargeImageUrl());
                updateIfPresent(existingProductCategory::setAltLargeImageUrl, productCategory.getAltLargeImageUrl());
                updateIfPresent(existingProductCategory::setInfo, productCategory.getInfo());
                updateIfPresent(existingProductCategory::setIsBanner, productCategory.getIsBanner());

                return existingProductCategory;
            })
            .map(productCategoryRepository::save);
    }

    /**
     * Get all the productCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<ProductCategory> findAllWithEagerRelationships() {
        return productCategoryRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one productCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductCategory> findOne(Long id) {
        LOG.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
