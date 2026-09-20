package com.snp.service;

import com.snp.domain.GeoType;
import com.snp.repository.GeoTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.GeoType}.
 */
@Service
@Transactional
public class GeoTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoTypeService.class);

    private final GeoTypeRepository geoTypeRepository;

    public GeoTypeService(GeoTypeRepository geoTypeRepository) {
        this.geoTypeRepository = geoTypeRepository;
    }

    /**
     * Save a geoType.
     *
     * @param geoType the entity to save.
     * @return the persisted entity.
     */
    public GeoType save(GeoType geoType) {
        LOG.debug("Request to save GeoType : {}", geoType);
        return geoTypeRepository.save(geoType);
    }

    /**
     * Update a geoType.
     *
     * @param geoType the entity to save.
     * @return the persisted entity.
     */
    public GeoType update(GeoType geoType) {
        LOG.debug("Request to update GeoType : {}", geoType);
        return geoTypeRepository.save(geoType);
    }

    /**
     * Partially update a geoType.
     *
     * @param geoType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GeoType> partialUpdate(GeoType geoType) {
        LOG.debug("Request to partially update GeoType : {}", geoType);

        return geoTypeRepository
            .findById(geoType.getId())
            .map(existingGeoType -> {
                updateIfPresent(existingGeoType::setName, geoType.getName());

                return existingGeoType;
            })
            .map(geoTypeRepository::save);
    }

    /**
     * Get one geoType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GeoType> findOne(Long id) {
        LOG.debug("Request to get GeoType : {}", id);
        return geoTypeRepository.findById(id);
    }

    /**
     * Delete the geoType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GeoType : {}", id);
        geoTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
