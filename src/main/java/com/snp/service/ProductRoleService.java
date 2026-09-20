package com.snp.service;

import com.snp.domain.ProductRole;
import com.snp.repository.ProductRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductRole}.
 */
@Service
@Transactional
public class ProductRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRoleService.class);

    private final ProductRoleRepository productRoleRepository;

    public ProductRoleService(ProductRoleRepository productRoleRepository) {
        this.productRoleRepository = productRoleRepository;
    }

    /**
     * Save a productRole.
     *
     * @param productRole the entity to save.
     * @return the persisted entity.
     */
    public ProductRole save(ProductRole productRole) {
        LOG.debug("Request to save ProductRole : {}", productRole);
        return productRoleRepository.save(productRole);
    }

    /**
     * Update a productRole.
     *
     * @param productRole the entity to save.
     * @return the persisted entity.
     */
    public ProductRole update(ProductRole productRole) {
        LOG.debug("Request to update ProductRole : {}", productRole);
        return productRoleRepository.save(productRole);
    }

    /**
     * Partially update a productRole.
     *
     * @param productRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductRole> partialUpdate(ProductRole productRole) {
        LOG.debug("Request to partially update ProductRole : {}", productRole);

        return productRoleRepository.findById(productRole.getId()).map(productRoleRepository::save);
    }

    /**
     * Get all the productRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductRole> findAllWithEagerRelationships(Pageable pageable) {
        return productRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductRole> findOne(Long id) {
        LOG.debug("Request to get ProductRole : {}", id);
        return productRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductRole : {}", id);
        productRoleRepository.deleteById(id);
    }
}
