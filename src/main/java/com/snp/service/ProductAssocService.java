package com.snp.service;

import com.snp.domain.ProductAssoc;
import com.snp.repository.ProductAssocRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductAssoc}.
 */
@Service
@Transactional
public class ProductAssocService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAssocService.class);

    private final ProductAssocRepository productAssocRepository;

    public ProductAssocService(ProductAssocRepository productAssocRepository) {
        this.productAssocRepository = productAssocRepository;
    }

    /**
     * Save a productAssoc.
     *
     * @param productAssoc the entity to save.
     * @return the persisted entity.
     */
    public ProductAssoc save(ProductAssoc productAssoc) {
        LOG.debug("Request to save ProductAssoc : {}", productAssoc);
        return productAssocRepository.save(productAssoc);
    }

    /**
     * Update a productAssoc.
     *
     * @param productAssoc the entity to save.
     * @return the persisted entity.
     */
    public ProductAssoc update(ProductAssoc productAssoc) {
        LOG.debug("Request to update ProductAssoc : {}", productAssoc);
        return productAssocRepository.save(productAssoc);
    }

    /**
     * Partially update a productAssoc.
     *
     * @param productAssoc the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductAssoc> partialUpdate(ProductAssoc productAssoc) {
        LOG.debug("Request to partially update ProductAssoc : {}", productAssoc);

        return productAssocRepository
            .findById(productAssoc.getId())
            .map(existingProductAssoc -> {
                updateIfPresent(existingProductAssoc::setFromDate, productAssoc.getFromDate());
                updateIfPresent(existingProductAssoc::setThruDate, productAssoc.getThruDate());
                updateIfPresent(existingProductAssoc::setSequenceNo, productAssoc.getSequenceNo());
                updateIfPresent(existingProductAssoc::setQuantity, productAssoc.getQuantity());

                return existingProductAssoc;
            })
            .map(productAssocRepository::save);
    }

    /**
     * Get all the productAssocs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductAssoc> findAllWithEagerRelationships(Pageable pageable) {
        return productAssocRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productAssoc by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductAssoc> findOne(Long id) {
        LOG.debug("Request to get ProductAssoc : {}", id);
        return productAssocRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productAssoc by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductAssoc : {}", id);
        productAssocRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
