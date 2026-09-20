package com.snp.service;

import com.snp.domain.Status;
import com.snp.repository.StatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Status}.
 */
@Service
@Transactional
public class StatusService {

    private static final Logger LOG = LoggerFactory.getLogger(StatusService.class);

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Save a status.
     *
     * @param status the entity to save.
     * @return the persisted entity.
     */
    public Status save(Status status) {
        LOG.debug("Request to save Status : {}", status);
        return statusRepository.save(status);
    }

    /**
     * Update a status.
     *
     * @param status the entity to save.
     * @return the persisted entity.
     */
    public Status update(Status status) {
        LOG.debug("Request to update Status : {}", status);
        return statusRepository.save(status);
    }

    /**
     * Partially update a status.
     *
     * @param status the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Status> partialUpdate(Status status) {
        LOG.debug("Request to partially update Status : {}", status);

        return statusRepository
            .findById(status.getId())
            .map(existingStatus -> {
                updateIfPresent(existingStatus::setName, status.getName());
                updateIfPresent(existingStatus::setSequenceNo, status.getSequenceNo());
                updateIfPresent(existingStatus::setDescription, status.getDescription());

                return existingStatus;
            })
            .map(statusRepository::save);
    }

    /**
     * Get all the statuses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<Status> findAllWithEagerRelationships() {
        return statusRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one status by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Status> findOne(Long id) {
        LOG.debug("Request to get Status : {}", id);
        return statusRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the status by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Status : {}", id);
        statusRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
