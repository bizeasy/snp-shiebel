package com.snp.service;

import com.snp.domain.ProductAttachment;
import com.snp.repository.ProductAttachmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductAttachment}.
 */
@Service
@Transactional
public class ProductAttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAttachmentService.class);

    private final ProductAttachmentRepository productAttachmentRepository;

    public ProductAttachmentService(ProductAttachmentRepository productAttachmentRepository) {
        this.productAttachmentRepository = productAttachmentRepository;
    }

    /**
     * Save a productAttachment.
     *
     * @param productAttachment the entity to save.
     * @return the persisted entity.
     */
    public ProductAttachment save(ProductAttachment productAttachment) {
        LOG.debug("Request to save ProductAttachment : {}", productAttachment);
        return productAttachmentRepository.save(productAttachment);
    }

    /**
     * Update a productAttachment.
     *
     * @param productAttachment the entity to save.
     * @return the persisted entity.
     */
    public ProductAttachment update(ProductAttachment productAttachment) {
        LOG.debug("Request to update ProductAttachment : {}", productAttachment);
        return productAttachmentRepository.save(productAttachment);
    }

    /**
     * Partially update a productAttachment.
     *
     * @param productAttachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductAttachment> partialUpdate(ProductAttachment productAttachment) {
        LOG.debug("Request to partially update ProductAttachment : {}", productAttachment);

        return productAttachmentRepository
            .findById(productAttachment.getId())
            .map(existingProductAttachment -> {
                updateIfPresent(existingProductAttachment::setName, productAttachment.getName());

                return existingProductAttachment;
            })
            .map(productAttachmentRepository::save);
    }

    /**
     * Get all the productAttachments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return productAttachmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductAttachment> findOne(Long id) {
        LOG.debug("Request to get ProductAttachment : {}", id);
        return productAttachmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductAttachment : {}", id);
        productAttachmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
