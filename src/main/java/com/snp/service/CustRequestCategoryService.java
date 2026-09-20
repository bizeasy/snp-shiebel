package com.snp.service;

import com.snp.domain.CustRequestCategory;
import com.snp.repository.CustRequestCategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustRequestCategory}.
 */
@Service
@Transactional
public class CustRequestCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestCategoryService.class);

    private final CustRequestCategoryRepository custRequestCategoryRepository;

    public CustRequestCategoryService(CustRequestCategoryRepository custRequestCategoryRepository) {
        this.custRequestCategoryRepository = custRequestCategoryRepository;
    }

    /**
     * Save a custRequestCategory.
     *
     * @param custRequestCategory the entity to save.
     * @return the persisted entity.
     */
    public CustRequestCategory save(CustRequestCategory custRequestCategory) {
        LOG.debug("Request to save CustRequestCategory : {}", custRequestCategory);
        return custRequestCategoryRepository.save(custRequestCategory);
    }

    /**
     * Update a custRequestCategory.
     *
     * @param custRequestCategory the entity to save.
     * @return the persisted entity.
     */
    public CustRequestCategory update(CustRequestCategory custRequestCategory) {
        LOG.debug("Request to update CustRequestCategory : {}", custRequestCategory);
        return custRequestCategoryRepository.save(custRequestCategory);
    }

    /**
     * Partially update a custRequestCategory.
     *
     * @param custRequestCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustRequestCategory> partialUpdate(CustRequestCategory custRequestCategory) {
        LOG.debug("Request to partially update CustRequestCategory : {}", custRequestCategory);

        return custRequestCategoryRepository
            .findById(custRequestCategory.getId())
            .map(existingCustRequestCategory -> {
                updateIfPresent(existingCustRequestCategory::setName, custRequestCategory.getName());
                updateIfPresent(existingCustRequestCategory::setDescription, custRequestCategory.getDescription());

                return existingCustRequestCategory;
            })
            .map(custRequestCategoryRepository::save);
    }

    /**
     * Get all the custRequestCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<CustRequestCategory> findAllWithEagerRelationships() {
        return custRequestCategoryRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one custRequestCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustRequestCategory> findOne(Long id) {
        LOG.debug("Request to get CustRequestCategory : {}", id);
        return custRequestCategoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the custRequestCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustRequestCategory : {}", id);
        custRequestCategoryRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
