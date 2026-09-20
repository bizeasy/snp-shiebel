package com.snp.service;

import com.snp.domain.PartyClassification;
import com.snp.repository.PartyClassificationRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyClassification}.
 */
@Service
@Transactional
public class PartyClassificationService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationService.class);

    private final PartyClassificationRepository partyClassificationRepository;

    public PartyClassificationService(PartyClassificationRepository partyClassificationRepository) {
        this.partyClassificationRepository = partyClassificationRepository;
    }

    /**
     * Save a partyClassification.
     *
     * @param partyClassification the entity to save.
     * @return the persisted entity.
     */
    public PartyClassification save(PartyClassification partyClassification) {
        LOG.debug("Request to save PartyClassification : {}", partyClassification);
        return partyClassificationRepository.save(partyClassification);
    }

    /**
     * Update a partyClassification.
     *
     * @param partyClassification the entity to save.
     * @return the persisted entity.
     */
    public PartyClassification update(PartyClassification partyClassification) {
        LOG.debug("Request to update PartyClassification : {}", partyClassification);
        return partyClassificationRepository.save(partyClassification);
    }

    /**
     * Partially update a partyClassification.
     *
     * @param partyClassification the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyClassification> partialUpdate(PartyClassification partyClassification) {
        LOG.debug("Request to partially update PartyClassification : {}", partyClassification);

        return partyClassificationRepository
            .findById(partyClassification.getId())
            .map(existingPartyClassification -> {
                updateIfPresent(existingPartyClassification::setFromDate, partyClassification.getFromDate());
                updateIfPresent(existingPartyClassification::setThruDate, partyClassification.getThruDate());

                return existingPartyClassification;
            })
            .map(partyClassificationRepository::save);
    }

    /**
     * Get all the partyClassifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PartyClassification> findAllWithEagerRelationships(Pageable pageable) {
        return partyClassificationRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one partyClassification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyClassification> findOne(Long id) {
        LOG.debug("Request to get PartyClassification : {}", id);
        return partyClassificationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partyClassification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyClassification : {}", id);
        partyClassificationRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
