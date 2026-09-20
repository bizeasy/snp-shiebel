package com.snp.service;

import com.snp.domain.PartyContactMech;
import com.snp.repository.PartyContactMechRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyContactMech}.
 */
@Service
@Transactional
public class PartyContactMechService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyContactMechService.class);

    private final PartyContactMechRepository partyContactMechRepository;

    public PartyContactMechService(PartyContactMechRepository partyContactMechRepository) {
        this.partyContactMechRepository = partyContactMechRepository;
    }

    /**
     * Save a partyContactMech.
     *
     * @param partyContactMech the entity to save.
     * @return the persisted entity.
     */
    public PartyContactMech save(PartyContactMech partyContactMech) {
        LOG.debug("Request to save PartyContactMech : {}", partyContactMech);
        return partyContactMechRepository.save(partyContactMech);
    }

    /**
     * Update a partyContactMech.
     *
     * @param partyContactMech the entity to save.
     * @return the persisted entity.
     */
    public PartyContactMech update(PartyContactMech partyContactMech) {
        LOG.debug("Request to update PartyContactMech : {}", partyContactMech);
        return partyContactMechRepository.save(partyContactMech);
    }

    /**
     * Partially update a partyContactMech.
     *
     * @param partyContactMech the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyContactMech> partialUpdate(PartyContactMech partyContactMech) {
        LOG.debug("Request to partially update PartyContactMech : {}", partyContactMech);

        return partyContactMechRepository
            .findById(partyContactMech.getId())
            .map(existingPartyContactMech -> {
                updateIfPresent(existingPartyContactMech::setFromDate, partyContactMech.getFromDate());
                updateIfPresent(existingPartyContactMech::setThruDate, partyContactMech.getThruDate());

                return existingPartyContactMech;
            })
            .map(partyContactMechRepository::save);
    }

    /**
     * Get all the partyContactMeches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PartyContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return partyContactMechRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one partyContactMech by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyContactMech> findOne(Long id) {
        LOG.debug("Request to get PartyContactMech : {}", id);
        return partyContactMechRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partyContactMech by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyContactMech : {}", id);
        partyContactMechRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
