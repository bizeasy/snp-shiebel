package com.snp.service;

import com.snp.domain.Party;
import com.snp.repository.PartyRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Party}.
 */
@Service
@Transactional
public class PartyService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyService.class);

    private final PartyRepository partyRepository;

    public PartyService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    /**
     * Save a party.
     *
     * @param party the entity to save.
     * @return the persisted entity.
     */
    public Party save(Party party) {
        LOG.debug("Request to save Party : {}", party);
        return partyRepository.save(party);
    }

    /**
     * Update a party.
     *
     * @param party the entity to save.
     * @return the persisted entity.
     */
    public Party update(Party party) {
        LOG.debug("Request to update Party : {}", party);
        return partyRepository.save(party);
    }

    /**
     * Partially update a party.
     *
     * @param party the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Party> partialUpdate(Party party) {
        LOG.debug("Request to partially update Party : {}", party);

        return partyRepository
            .findById(party.getId())
            .map(existingParty -> {
                updateIfPresent(existingParty::setIsOrganisation, party.getIsOrganisation());
                updateIfPresent(existingParty::setOrganisationName, party.getOrganisationName());
                updateIfPresent(existingParty::setOrganisationShortName, party.getOrganisationShortName());
                updateIfPresent(existingParty::setSalutation, party.getSalutation());
                updateIfPresent(existingParty::setFirstName, party.getFirstName());
                updateIfPresent(existingParty::setMiddleName, party.getMiddleName());
                updateIfPresent(existingParty::setLastName, party.getLastName());
                updateIfPresent(existingParty::setGender, party.getGender());
                updateIfPresent(existingParty::setBirthDate, party.getBirthDate());
                updateIfPresent(existingParty::setPrimaryPhone, party.getPrimaryPhone());
                updateIfPresent(existingParty::setPrimaryEmail, party.getPrimaryEmail());
                updateIfPresent(existingParty::setIsTemporaryPassword, party.getIsTemporaryPassword());
                updateIfPresent(existingParty::setLogoImageUrl, party.getLogoImageUrl());
                updateIfPresent(existingParty::setProfileImageUrl, party.getProfileImageUrl());
                updateIfPresent(existingParty::setNotes, party.getNotes());
                updateIfPresent(existingParty::setPostalAddress, party.getPostalAddress());

                return existingParty;
            })
            .map(partyRepository::save);
    }

    /**
     * Get all the parties with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Party> findAllWithEagerRelationships(Pageable pageable) {
        return partyRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one party by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Party> findOne(Long id) {
        LOG.debug("Request to get Party : {}", id);
        return partyRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the party by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Party : {}", id);
        partyRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
