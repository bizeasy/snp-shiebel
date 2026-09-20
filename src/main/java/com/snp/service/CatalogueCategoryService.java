package com.snp.service;

import com.snp.domain.CatalogueCategory;
import com.snp.repository.CatalogueCategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CatalogueCategory}.
 */
@Service
@Transactional
public class CatalogueCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueCategoryService.class);

    private final CatalogueCategoryRepository catalogueCategoryRepository;

    public CatalogueCategoryService(CatalogueCategoryRepository catalogueCategoryRepository) {
        this.catalogueCategoryRepository = catalogueCategoryRepository;
    }

    /**
     * Save a catalogueCategory.
     *
     * @param catalogueCategory the entity to save.
     * @return the persisted entity.
     */
    public CatalogueCategory save(CatalogueCategory catalogueCategory) {
        LOG.debug("Request to save CatalogueCategory : {}", catalogueCategory);
        return catalogueCategoryRepository.save(catalogueCategory);
    }

    /**
     * Update a catalogueCategory.
     *
     * @param catalogueCategory the entity to save.
     * @return the persisted entity.
     */
    public CatalogueCategory update(CatalogueCategory catalogueCategory) {
        LOG.debug("Request to update CatalogueCategory : {}", catalogueCategory);
        return catalogueCategoryRepository.save(catalogueCategory);
    }

    /**
     * Partially update a catalogueCategory.
     *
     * @param catalogueCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogueCategory> partialUpdate(CatalogueCategory catalogueCategory) {
        LOG.debug("Request to partially update CatalogueCategory : {}", catalogueCategory);

        return catalogueCategoryRepository
            .findById(catalogueCategory.getId())
            .map(existingCatalogueCategory -> {
                updateIfPresent(existingCatalogueCategory::setSequenceNo, catalogueCategory.getSequenceNo());

                return existingCatalogueCategory;
            })
            .map(catalogueCategoryRepository::save);
    }

    /**
     * Get all the catalogueCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<CatalogueCategory> findAllWithEagerRelationships() {
        return catalogueCategoryRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one catalogueCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogueCategory> findOne(Long id) {
        LOG.debug("Request to get CatalogueCategory : {}", id);
        return catalogueCategoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the catalogueCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CatalogueCategory : {}", id);
        catalogueCategoryRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
