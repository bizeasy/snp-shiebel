package com.snp.service;

import com.snp.domain.FacilityGroupType;
import com.snp.repository.FacilityGroupTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityGroupType}.
 */
@Service
@Transactional
public class FacilityGroupTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupTypeService.class);

    private final FacilityGroupTypeRepository facilityGroupTypeRepository;

    public FacilityGroupTypeService(FacilityGroupTypeRepository facilityGroupTypeRepository) {
        this.facilityGroupTypeRepository = facilityGroupTypeRepository;
    }

    /**
     * Save a facilityGroupType.
     *
     * @param facilityGroupType the entity to save.
     * @return the persisted entity.
     */
    public FacilityGroupType save(FacilityGroupType facilityGroupType) {
        LOG.debug("Request to save FacilityGroupType : {}", facilityGroupType);
        return facilityGroupTypeRepository.save(facilityGroupType);
    }

    /**
     * Update a facilityGroupType.
     *
     * @param facilityGroupType the entity to save.
     * @return the persisted entity.
     */
    public FacilityGroupType update(FacilityGroupType facilityGroupType) {
        LOG.debug("Request to update FacilityGroupType : {}", facilityGroupType);
        return facilityGroupTypeRepository.save(facilityGroupType);
    }

    /**
     * Partially update a facilityGroupType.
     *
     * @param facilityGroupType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityGroupType> partialUpdate(FacilityGroupType facilityGroupType) {
        LOG.debug("Request to partially update FacilityGroupType : {}", facilityGroupType);

        return facilityGroupTypeRepository
            .findById(facilityGroupType.getId())
            .map(existingFacilityGroupType -> {
                updateIfPresent(existingFacilityGroupType::setName, facilityGroupType.getName());
                updateIfPresent(existingFacilityGroupType::setDescription, facilityGroupType.getDescription());

                return existingFacilityGroupType;
            })
            .map(facilityGroupTypeRepository::save);
    }

    /**
     * Get one facilityGroupType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityGroupType> findOne(Long id) {
        LOG.debug("Request to get FacilityGroupType : {}", id);
        return facilityGroupTypeRepository.findById(id);
    }

    /**
     * Delete the facilityGroupType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityGroupType : {}", id);
        facilityGroupTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
