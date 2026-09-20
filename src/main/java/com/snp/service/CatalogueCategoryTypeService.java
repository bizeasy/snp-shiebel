package com.snp.service;

import com.snp.domain.CatalogueCategoryType;
import com.snp.repository.CatalogueCategoryTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CatalogueCategoryType}.
 */
@Service
@Transactional
public class CatalogueCategoryTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueCategoryTypeService.class);

    private final CatalogueCategoryTypeRepository catalogueCategoryTypeRepository;

    public CatalogueCategoryTypeService(CatalogueCategoryTypeRepository catalogueCategoryTypeRepository) {
        this.catalogueCategoryTypeRepository = catalogueCategoryTypeRepository;
    }

    /**
     * Save a catalogueCategoryType.
     *
     * @param catalogueCategoryType the entity to save.
     * @return the persisted entity.
     */
    public CatalogueCategoryType save(CatalogueCategoryType catalogueCategoryType) {
        LOG.debug("Request to save CatalogueCategoryType : {}", catalogueCategoryType);
        return catalogueCategoryTypeRepository.save(catalogueCategoryType);
    }

    /**
     * Update a catalogueCategoryType.
     *
     * @param catalogueCategoryType the entity to save.
     * @return the persisted entity.
     */
    public CatalogueCategoryType update(CatalogueCategoryType catalogueCategoryType) {
        LOG.debug("Request to update CatalogueCategoryType : {}", catalogueCategoryType);
        return catalogueCategoryTypeRepository.save(catalogueCategoryType);
    }

    /**
     * Partially update a catalogueCategoryType.
     *
     * @param catalogueCategoryType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogueCategoryType> partialUpdate(CatalogueCategoryType catalogueCategoryType) {
        LOG.debug("Request to partially update CatalogueCategoryType : {}", catalogueCategoryType);

        return catalogueCategoryTypeRepository
            .findById(catalogueCategoryType.getId())
            .map(existingCatalogueCategoryType -> {
                updateIfPresent(existingCatalogueCategoryType::setName, catalogueCategoryType.getName());
                updateIfPresent(existingCatalogueCategoryType::setDescription, catalogueCategoryType.getDescription());

                return existingCatalogueCategoryType;
            })
            .map(catalogueCategoryTypeRepository::save);
    }

    /**
     * Get one catalogueCategoryType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogueCategoryType> findOne(Long id) {
        LOG.debug("Request to get CatalogueCategoryType : {}", id);
        return catalogueCategoryTypeRepository.findById(id);
    }

    /**
     * Delete the catalogueCategoryType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CatalogueCategoryType : {}", id);
        catalogueCategoryTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
