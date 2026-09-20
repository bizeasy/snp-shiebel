package com.snp.service;

import com.snp.domain.FacilityType;
import com.snp.repository.FacilityTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityType}.
 */
@Service
@Transactional
public class FacilityTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityTypeService.class);

    private final FacilityTypeRepository facilityTypeRepository;

    public FacilityTypeService(FacilityTypeRepository facilityTypeRepository) {
        this.facilityTypeRepository = facilityTypeRepository;
    }

    /**
     * Save a facilityType.
     *
     * @param facilityType the entity to save.
     * @return the persisted entity.
     */
    public FacilityType save(FacilityType facilityType) {
        LOG.debug("Request to save FacilityType : {}", facilityType);
        return facilityTypeRepository.save(facilityType);
    }

    /**
     * Update a facilityType.
     *
     * @param facilityType the entity to save.
     * @return the persisted entity.
     */
    public FacilityType update(FacilityType facilityType) {
        LOG.debug("Request to update FacilityType : {}", facilityType);
        return facilityTypeRepository.save(facilityType);
    }

    /**
     * Partially update a facilityType.
     *
     * @param facilityType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityType> partialUpdate(FacilityType facilityType) {
        LOG.debug("Request to partially update FacilityType : {}", facilityType);

        return facilityTypeRepository
            .findById(facilityType.getId())
            .map(existingFacilityType -> {
                updateIfPresent(existingFacilityType::setName, facilityType.getName());
                updateIfPresent(existingFacilityType::setDescription, facilityType.getDescription());

                return existingFacilityType;
            })
            .map(facilityTypeRepository::save);
    }

    /**
     * Get one facilityType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityType> findOne(Long id) {
        LOG.debug("Request to get FacilityType : {}", id);
        return facilityTypeRepository.findById(id);
    }

    /**
     * Delete the facilityType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityType : {}", id);
        facilityTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
