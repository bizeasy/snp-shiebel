package com.snp.service;

import com.snp.domain.ProductReview;
import com.snp.repository.ProductReviewRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductReview}.
 */
@Service
@Transactional
public class ProductReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductReviewService.class);

    private final ProductReviewRepository productReviewRepository;

    public ProductReviewService(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    /**
     * Save a productReview.
     *
     * @param productReview the entity to save.
     * @return the persisted entity.
     */
    public ProductReview save(ProductReview productReview) {
        LOG.debug("Request to save ProductReview : {}", productReview);
        return productReviewRepository.save(productReview);
    }

    /**
     * Update a productReview.
     *
     * @param productReview the entity to save.
     * @return the persisted entity.
     */
    public ProductReview update(ProductReview productReview) {
        LOG.debug("Request to update ProductReview : {}", productReview);
        return productReviewRepository.save(productReview);
    }

    /**
     * Partially update a productReview.
     *
     * @param productReview the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductReview> partialUpdate(ProductReview productReview) {
        LOG.debug("Request to partially update ProductReview : {}", productReview);

        return productReviewRepository
            .findById(productReview.getId())
            .map(existingProductReview -> {
                updateIfPresent(existingProductReview::setRating, productReview.getRating());
                updateIfPresent(existingProductReview::setRatingInfo, productReview.getRatingInfo());
                updateIfPresent(existingProductReview::setMessage, productReview.getMessage());
                updateIfPresent(existingProductReview::setReviewDate, productReview.getReviewDate());

                return existingProductReview;
            })
            .map(productReviewRepository::save);
    }

    /**
     * Get all the productReviews with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductReview> findAllWithEagerRelationships(Pageable pageable) {
        return productReviewRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductReview> findOne(Long id) {
        LOG.debug("Request to get ProductReview : {}", id);
        return productReviewRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productReview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductReview : {}", id);
        productReviewRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
