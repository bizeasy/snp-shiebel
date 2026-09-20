package com.snp.service;

import com.snp.domain.LocationType;
import com.snp.repository.LocationTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.LocationType}.
 */
@Service
@Transactional
public class LocationTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(LocationTypeService.class);

    private final LocationTypeRepository locationTypeRepository;

    public LocationTypeService(LocationTypeRepository locationTypeRepository) {
        this.locationTypeRepository = locationTypeRepository;
    }

    /**
     * Save a locationType.
     *
     * @param locationType the entity to save.
     * @return the persisted entity.
     */
    public LocationType save(LocationType locationType) {
        LOG.debug("Request to save LocationType : {}", locationType);
        return locationTypeRepository.save(locationType);
    }

    /**
     * Update a locationType.
     *
     * @param locationType the entity to save.
     * @return the persisted entity.
     */
    public LocationType update(LocationType locationType) {
        LOG.debug("Request to update LocationType : {}", locationType);
        return locationTypeRepository.save(locationType);
    }

    /**
     * Partially update a locationType.
     *
     * @param locationType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocationType> partialUpdate(LocationType locationType) {
        LOG.debug("Request to partially update LocationType : {}", locationType);

        return locationTypeRepository
            .findById(locationType.getId())
            .map(existingLocationType -> {
                updateIfPresent(existingLocationType::setName, locationType.getName());
                updateIfPresent(existingLocationType::setDescription, locationType.getDescription());

                return existingLocationType;
            })
            .map(locationTypeRepository::save);
    }

    /**
     * Get one locationType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocationType> findOne(Long id) {
        LOG.debug("Request to get LocationType : {}", id);
        return locationTypeRepository.findById(id);
    }

    /**
     * Delete the locationType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LocationType : {}", id);
        locationTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
