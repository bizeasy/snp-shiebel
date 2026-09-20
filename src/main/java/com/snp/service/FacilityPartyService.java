package com.snp.service;

import com.snp.domain.FacilityParty;
import com.snp.repository.FacilityPartyRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityParty}.
 */
@Service
@Transactional
public class FacilityPartyService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityPartyService.class);

    private final FacilityPartyRepository facilityPartyRepository;

    public FacilityPartyService(FacilityPartyRepository facilityPartyRepository) {
        this.facilityPartyRepository = facilityPartyRepository;
    }

    /**
     * Save a facilityParty.
     *
     * @param facilityParty the entity to save.
     * @return the persisted entity.
     */
    public FacilityParty save(FacilityParty facilityParty) {
        LOG.debug("Request to save FacilityParty : {}", facilityParty);
        return facilityPartyRepository.save(facilityParty);
    }

    /**
     * Update a facilityParty.
     *
     * @param facilityParty the entity to save.
     * @return the persisted entity.
     */
    public FacilityParty update(FacilityParty facilityParty) {
        LOG.debug("Request to update FacilityParty : {}", facilityParty);
        return facilityPartyRepository.save(facilityParty);
    }

    /**
     * Partially update a facilityParty.
     *
     * @param facilityParty the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityParty> partialUpdate(FacilityParty facilityParty) {
        LOG.debug("Request to partially update FacilityParty : {}", facilityParty);

        return facilityPartyRepository
            .findById(facilityParty.getId())
            .map(existingFacilityParty -> {
                updateIfPresent(existingFacilityParty::setFromDate, facilityParty.getFromDate());
                updateIfPresent(existingFacilityParty::setThruDate, facilityParty.getThruDate());

                return existingFacilityParty;
            })
            .map(facilityPartyRepository::save);
    }

    /**
     * Get all the facilityParties with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacilityParty> findAllWithEagerRelationships(Pageable pageable) {
        return facilityPartyRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one facilityParty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityParty> findOne(Long id) {
        LOG.debug("Request to get FacilityParty : {}", id);
        return facilityPartyRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facilityParty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityParty : {}", id);
        facilityPartyRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
