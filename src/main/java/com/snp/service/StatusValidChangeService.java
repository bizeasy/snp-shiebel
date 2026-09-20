package com.snp.service;

import com.snp.domain.StatusValidChange;
import com.snp.repository.StatusValidChangeRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.StatusValidChange}.
 */
@Service
@Transactional
public class StatusValidChangeService {

    private static final Logger LOG = LoggerFactory.getLogger(StatusValidChangeService.class);

    private final StatusValidChangeRepository statusValidChangeRepository;

    public StatusValidChangeService(StatusValidChangeRepository statusValidChangeRepository) {
        this.statusValidChangeRepository = statusValidChangeRepository;
    }

    /**
     * Save a statusValidChange.
     *
     * @param statusValidChange the entity to save.
     * @return the persisted entity.
     */
    public StatusValidChange save(StatusValidChange statusValidChange) {
        LOG.debug("Request to save StatusValidChange : {}", statusValidChange);
        return statusValidChangeRepository.save(statusValidChange);
    }

    /**
     * Update a statusValidChange.
     *
     * @param statusValidChange the entity to save.
     * @return the persisted entity.
     */
    public StatusValidChange update(StatusValidChange statusValidChange) {
        LOG.debug("Request to update StatusValidChange : {}", statusValidChange);
        return statusValidChangeRepository.save(statusValidChange);
    }

    /**
     * Partially update a statusValidChange.
     *
     * @param statusValidChange the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StatusValidChange> partialUpdate(StatusValidChange statusValidChange) {
        LOG.debug("Request to partially update StatusValidChange : {}", statusValidChange);

        return statusValidChangeRepository
            .findById(statusValidChange.getId())
            .map(existingStatusValidChange -> {
                updateIfPresent(existingStatusValidChange::setTransitionName, statusValidChange.getTransitionName());

                return existingStatusValidChange;
            })
            .map(statusValidChangeRepository::save);
    }

    /**
     * Get all the statusValidChanges with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<StatusValidChange> findAllWithEagerRelationships() {
        return statusValidChangeRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one statusValidChange by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StatusValidChange> findOne(Long id) {
        LOG.debug("Request to get StatusValidChange : {}", id);
        return statusValidChangeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the statusValidChange by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StatusValidChange : {}", id);
        statusValidChangeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
