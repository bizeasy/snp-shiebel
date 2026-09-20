package com.snp.service;

import com.snp.domain.FacilityLocation;
import com.snp.repository.FacilityLocationRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityLocation}.
 */
@Service
@Transactional
public class FacilityLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityLocationService.class);

    private final FacilityLocationRepository facilityLocationRepository;

    public FacilityLocationService(FacilityLocationRepository facilityLocationRepository) {
        this.facilityLocationRepository = facilityLocationRepository;
    }

    /**
     * Save a facilityLocation.
     *
     * @param facilityLocation the entity to save.
     * @return the persisted entity.
     */
    public FacilityLocation save(FacilityLocation facilityLocation) {
        LOG.debug("Request to save FacilityLocation : {}", facilityLocation);
        return facilityLocationRepository.save(facilityLocation);
    }

    /**
     * Update a facilityLocation.
     *
     * @param facilityLocation the entity to save.
     * @return the persisted entity.
     */
    public FacilityLocation update(FacilityLocation facilityLocation) {
        LOG.debug("Request to update FacilityLocation : {}", facilityLocation);
        return facilityLocationRepository.save(facilityLocation);
    }

    /**
     * Partially update a facilityLocation.
     *
     * @param facilityLocation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityLocation> partialUpdate(FacilityLocation facilityLocation) {
        LOG.debug("Request to partially update FacilityLocation : {}", facilityLocation);

        return facilityLocationRepository
            .findById(facilityLocation.getId())
            .map(existingFacilityLocation -> {
                updateIfPresent(existingFacilityLocation::setLocationSeqId, facilityLocation.getLocationSeqId());
                updateIfPresent(existingFacilityLocation::setAreaId, facilityLocation.getAreaId());
                updateIfPresent(existingFacilityLocation::setAisleId, facilityLocation.getAisleId());
                updateIfPresent(existingFacilityLocation::setSectionId, facilityLocation.getSectionId());
                updateIfPresent(existingFacilityLocation::setLevelId, facilityLocation.getLevelId());
                updateIfPresent(existingFacilityLocation::setPositionId, facilityLocation.getPositionId());

                return existingFacilityLocation;
            })
            .map(facilityLocationRepository::save);
    }

    /**
     * Get all the facilityLocations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacilityLocation> findAllWithEagerRelationships(Pageable pageable) {
        return facilityLocationRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one facilityLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityLocation> findOne(Long id) {
        LOG.debug("Request to get FacilityLocation : {}", id);
        return facilityLocationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facilityLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityLocation : {}", id);
        facilityLocationRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
