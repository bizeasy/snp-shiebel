package com.snp.service;

import com.snp.domain.Facility;
import com.snp.repository.FacilityRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Facility}.
 */
@Service
@Transactional
public class FacilityService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityService.class);

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    /**
     * Save a facility.
     *
     * @param facility the entity to save.
     * @return the persisted entity.
     */
    public Facility save(Facility facility) {
        LOG.debug("Request to save Facility : {}", facility);
        return facilityRepository.save(facility);
    }

    /**
     * Update a facility.
     *
     * @param facility the entity to save.
     * @return the persisted entity.
     */
    public Facility update(Facility facility) {
        LOG.debug("Request to update Facility : {}", facility);
        return facilityRepository.save(facility);
    }

    /**
     * Partially update a facility.
     *
     * @param facility the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Facility> partialUpdate(Facility facility) {
        LOG.debug("Request to partially update Facility : {}", facility);

        return facilityRepository
            .findById(facility.getId())
            .map(existingFacility -> {
                updateIfPresent(existingFacility::setName, facility.getName());
                updateIfPresent(existingFacility::setDescription, facility.getDescription());
                updateIfPresent(existingFacility::setFacilitySize, facility.getFacilitySize());

                return existingFacility;
            })
            .map(facilityRepository::save);
    }

    /**
     * Get all the facilities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Facility> findAllWithEagerRelationships(Pageable pageable) {
        return facilityRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one facility by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Facility> findOne(Long id) {
        LOG.debug("Request to get Facility : {}", id);
        return facilityRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facility by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Facility : {}", id);
        facilityRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
