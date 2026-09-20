package com.snp.service;

import com.snp.domain.FacilityContactMech;
import com.snp.repository.FacilityContactMechRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityContactMech}.
 */
@Service
@Transactional
public class FacilityContactMechService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityContactMechService.class);

    private final FacilityContactMechRepository facilityContactMechRepository;

    public FacilityContactMechService(FacilityContactMechRepository facilityContactMechRepository) {
        this.facilityContactMechRepository = facilityContactMechRepository;
    }

    /**
     * Save a facilityContactMech.
     *
     * @param facilityContactMech the entity to save.
     * @return the persisted entity.
     */
    public FacilityContactMech save(FacilityContactMech facilityContactMech) {
        LOG.debug("Request to save FacilityContactMech : {}", facilityContactMech);
        return facilityContactMechRepository.save(facilityContactMech);
    }

    /**
     * Update a facilityContactMech.
     *
     * @param facilityContactMech the entity to save.
     * @return the persisted entity.
     */
    public FacilityContactMech update(FacilityContactMech facilityContactMech) {
        LOG.debug("Request to update FacilityContactMech : {}", facilityContactMech);
        return facilityContactMechRepository.save(facilityContactMech);
    }

    /**
     * Partially update a facilityContactMech.
     *
     * @param facilityContactMech the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityContactMech> partialUpdate(FacilityContactMech facilityContactMech) {
        LOG.debug("Request to partially update FacilityContactMech : {}", facilityContactMech);

        return facilityContactMechRepository
            .findById(facilityContactMech.getId())
            .map(existingFacilityContactMech -> {
                updateIfPresent(existingFacilityContactMech::setFromDate, facilityContactMech.getFromDate());
                updateIfPresent(existingFacilityContactMech::setThruDate, facilityContactMech.getThruDate());

                return existingFacilityContactMech;
            })
            .map(facilityContactMechRepository::save);
    }

    /**
     * Get all the facilityContactMeches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacilityContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return facilityContactMechRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one facilityContactMech by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityContactMech> findOne(Long id) {
        LOG.debug("Request to get FacilityContactMech : {}", id);
        return facilityContactMechRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facilityContactMech by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityContactMech : {}", id);
        facilityContactMechRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
