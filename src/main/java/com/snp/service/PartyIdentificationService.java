package com.snp.service;

import com.snp.domain.PartyIdentification;
import com.snp.repository.PartyIdentificationRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyIdentification}.
 */
@Service
@Transactional
public class PartyIdentificationService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyIdentificationService.class);

    private final PartyIdentificationRepository partyIdentificationRepository;

    public PartyIdentificationService(PartyIdentificationRepository partyIdentificationRepository) {
        this.partyIdentificationRepository = partyIdentificationRepository;
    }

    /**
     * Save a partyIdentification.
     *
     * @param partyIdentification the entity to save.
     * @return the persisted entity.
     */
    public PartyIdentification save(PartyIdentification partyIdentification) {
        LOG.debug("Request to save PartyIdentification : {}", partyIdentification);
        return partyIdentificationRepository.save(partyIdentification);
    }

    /**
     * Update a partyIdentification.
     *
     * @param partyIdentification the entity to save.
     * @return the persisted entity.
     */
    public PartyIdentification update(PartyIdentification partyIdentification) {
        LOG.debug("Request to update PartyIdentification : {}", partyIdentification);
        return partyIdentificationRepository.save(partyIdentification);
    }

    /**
     * Partially update a partyIdentification.
     *
     * @param partyIdentification the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyIdentification> partialUpdate(PartyIdentification partyIdentification) {
        LOG.debug("Request to partially update PartyIdentification : {}", partyIdentification);

        return partyIdentificationRepository
            .findById(partyIdentification.getId())
            .map(existingPartyIdentification -> {
                updateIfPresent(existingPartyIdentification::setIdValue, partyIdentification.getIdValue());

                return existingPartyIdentification;
            })
            .map(partyIdentificationRepository::save);
    }

    /**
     * Get all the partyIdentifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PartyIdentification> findAllWithEagerRelationships(Pageable pageable) {
        return partyIdentificationRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one partyIdentification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyIdentification> findOne(Long id) {
        LOG.debug("Request to get PartyIdentification : {}", id);
        return partyIdentificationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partyIdentification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyIdentification : {}", id);
        partyIdentificationRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
