package com.snp.service;

import com.snp.domain.ProductCategoryMember;
import com.snp.repository.ProductCategoryMemberRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductCategoryMember}.
 */
@Service
@Transactional
public class ProductCategoryMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryMemberService.class);

    private final ProductCategoryMemberRepository productCategoryMemberRepository;

    public ProductCategoryMemberService(ProductCategoryMemberRepository productCategoryMemberRepository) {
        this.productCategoryMemberRepository = productCategoryMemberRepository;
    }

    /**
     * Save a productCategoryMember.
     *
     * @param productCategoryMember the entity to save.
     * @return the persisted entity.
     */
    public ProductCategoryMember save(ProductCategoryMember productCategoryMember) {
        LOG.debug("Request to save ProductCategoryMember : {}", productCategoryMember);
        return productCategoryMemberRepository.save(productCategoryMember);
    }

    /**
     * Update a productCategoryMember.
     *
     * @param productCategoryMember the entity to save.
     * @return the persisted entity.
     */
    public ProductCategoryMember update(ProductCategoryMember productCategoryMember) {
        LOG.debug("Request to update ProductCategoryMember : {}", productCategoryMember);
        return productCategoryMemberRepository.save(productCategoryMember);
    }

    /**
     * Partially update a productCategoryMember.
     *
     * @param productCategoryMember the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductCategoryMember> partialUpdate(ProductCategoryMember productCategoryMember) {
        LOG.debug("Request to partially update ProductCategoryMember : {}", productCategoryMember);

        return productCategoryMemberRepository
            .findById(productCategoryMember.getId())
            .map(existingProductCategoryMember -> {
                updateIfPresent(existingProductCategoryMember::setFromDate, productCategoryMember.getFromDate());
                updateIfPresent(existingProductCategoryMember::setThruDate, productCategoryMember.getThruDate());
                updateIfPresent(existingProductCategoryMember::setSequenceNo, productCategoryMember.getSequenceNo());

                return existingProductCategoryMember;
            })
            .map(productCategoryMemberRepository::save);
    }

    /**
     * Get all the productCategoryMembers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductCategoryMember> findAllWithEagerRelationships(Pageable pageable) {
        return productCategoryMemberRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productCategoryMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductCategoryMember> findOne(Long id) {
        LOG.debug("Request to get ProductCategoryMember : {}", id);
        return productCategoryMemberRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productCategoryMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductCategoryMember : {}", id);
        productCategoryMemberRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
