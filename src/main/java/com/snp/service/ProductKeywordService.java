package com.snp.service;

import com.snp.domain.ProductKeyword;
import com.snp.repository.ProductKeywordRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductKeyword}.
 */
@Service
@Transactional
public class ProductKeywordService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductKeywordService.class);

    private final ProductKeywordRepository productKeywordRepository;

    public ProductKeywordService(ProductKeywordRepository productKeywordRepository) {
        this.productKeywordRepository = productKeywordRepository;
    }

    /**
     * Save a productKeyword.
     *
     * @param productKeyword the entity to save.
     * @return the persisted entity.
     */
    public ProductKeyword save(ProductKeyword productKeyword) {
        LOG.debug("Request to save ProductKeyword : {}", productKeyword);
        return productKeywordRepository.save(productKeyword);
    }

    /**
     * Update a productKeyword.
     *
     * @param productKeyword the entity to save.
     * @return the persisted entity.
     */
    public ProductKeyword update(ProductKeyword productKeyword) {
        LOG.debug("Request to update ProductKeyword : {}", productKeyword);
        return productKeywordRepository.save(productKeyword);
    }

    /**
     * Partially update a productKeyword.
     *
     * @param productKeyword the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductKeyword> partialUpdate(ProductKeyword productKeyword) {
        LOG.debug("Request to partially update ProductKeyword : {}", productKeyword);

        return productKeywordRepository
            .findById(productKeyword.getId())
            .map(existingProductKeyword -> {
                updateIfPresent(existingProductKeyword::setKeyword, productKeyword.getKeyword());
                updateIfPresent(existingProductKeyword::setRelevancyWeight, productKeyword.getRelevancyWeight());

                return existingProductKeyword;
            })
            .map(productKeywordRepository::save);
    }

    /**
     * Get all the productKeywords with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductKeyword> findAllWithEagerRelationships(Pageable pageable) {
        return productKeywordRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productKeyword by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductKeyword> findOne(Long id) {
        LOG.debug("Request to get ProductKeyword : {}", id);
        return productKeywordRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productKeyword by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductKeyword : {}", id);
        productKeywordRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
