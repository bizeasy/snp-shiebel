package com.snp.service;

import com.snp.domain.PartyRelationship;
import com.snp.repository.PartyRelationshipRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyRelationship}.
 */
@Service
@Transactional
public class PartyRelationshipService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRelationshipService.class);

    private final PartyRelationshipRepository partyRelationshipRepository;

    public PartyRelationshipService(PartyRelationshipRepository partyRelationshipRepository) {
        this.partyRelationshipRepository = partyRelationshipRepository;
    }

    /**
     * Save a partyRelationship.
     *
     * @param partyRelationship the entity to save.
     * @return the persisted entity.
     */
    public PartyRelationship save(PartyRelationship partyRelationship) {
        LOG.debug("Request to save PartyRelationship : {}", partyRelationship);
        return partyRelationshipRepository.save(partyRelationship);
    }

    /**
     * Update a partyRelationship.
     *
     * @param partyRelationship the entity to save.
     * @return the persisted entity.
     */
    public PartyRelationship update(PartyRelationship partyRelationship) {
        LOG.debug("Request to update PartyRelationship : {}", partyRelationship);
        return partyRelationshipRepository.save(partyRelationship);
    }

    /**
     * Partially update a partyRelationship.
     *
     * @param partyRelationship the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyRelationship> partialUpdate(PartyRelationship partyRelationship) {
        LOG.debug("Request to partially update PartyRelationship : {}", partyRelationship);

        return partyRelationshipRepository
            .findById(partyRelationship.getId())
            .map(existingPartyRelationship -> {
                updateIfPresent(existingPartyRelationship::setFromDate, partyRelationship.getFromDate());
                updateIfPresent(existingPartyRelationship::setThruDate, partyRelationship.getThruDate());
                updateIfPresent(existingPartyRelationship::setRelationshipName, partyRelationship.getRelationshipName());

                return existingPartyRelationship;
            })
            .map(partyRelationshipRepository::save);
    }

    /**
     * Get all the partyRelationships with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PartyRelationship> findAllWithEagerRelationships(Pageable pageable) {
        return partyRelationshipRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one partyRelationship by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyRelationship> findOne(Long id) {
        LOG.debug("Request to get PartyRelationship : {}", id);
        return partyRelationshipRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partyRelationship by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyRelationship : {}", id);
        partyRelationshipRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
