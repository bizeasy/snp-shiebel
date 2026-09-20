package com.snp.service;

import com.snp.domain.StatusCategory;
import com.snp.repository.StatusCategoryRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.StatusCategory}.
 */
@Service
@Transactional
public class StatusCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(StatusCategoryService.class);

    private final StatusCategoryRepository statusCategoryRepository;

    public StatusCategoryService(StatusCategoryRepository statusCategoryRepository) {
        this.statusCategoryRepository = statusCategoryRepository;
    }

    /**
     * Save a statusCategory.
     *
     * @param statusCategory the entity to save.
     * @return the persisted entity.
     */
    public StatusCategory save(StatusCategory statusCategory) {
        LOG.debug("Request to save StatusCategory : {}", statusCategory);
        return statusCategoryRepository.save(statusCategory);
    }

    /**
     * Update a statusCategory.
     *
     * @param statusCategory the entity to save.
     * @return the persisted entity.
     */
    public StatusCategory update(StatusCategory statusCategory) {
        LOG.debug("Request to update StatusCategory : {}", statusCategory);
        return statusCategoryRepository.save(statusCategory);
    }

    /**
     * Partially update a statusCategory.
     *
     * @param statusCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StatusCategory> partialUpdate(StatusCategory statusCategory) {
        LOG.debug("Request to partially update StatusCategory : {}", statusCategory);

        return statusCategoryRepository
            .findById(statusCategory.getId())
            .map(existingStatusCategory -> {
                updateIfPresent(existingStatusCategory::setName, statusCategory.getName());
                updateIfPresent(existingStatusCategory::setDescription, statusCategory.getDescription());

                return existingStatusCategory;
            })
            .map(statusCategoryRepository::save);
    }

    /**
     * Get one statusCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StatusCategory> findOne(Long id) {
        LOG.debug("Request to get StatusCategory : {}", id);
        return statusCategoryRepository.findById(id);
    }

    /**
     * Delete the statusCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StatusCategory : {}", id);
        statusCategoryRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
