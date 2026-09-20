package com.snp.service;

import com.snp.domain.GeoAssocType;
import com.snp.repository.GeoAssocTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.GeoAssocType}.
 */
@Service
@Transactional
public class GeoAssocTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoAssocTypeService.class);

    private final GeoAssocTypeRepository geoAssocTypeRepository;

    public GeoAssocTypeService(GeoAssocTypeRepository geoAssocTypeRepository) {
        this.geoAssocTypeRepository = geoAssocTypeRepository;
    }

    /**
     * Save a geoAssocType.
     *
     * @param geoAssocType the entity to save.
     * @return the persisted entity.
     */
    public GeoAssocType save(GeoAssocType geoAssocType) {
        LOG.debug("Request to save GeoAssocType : {}", geoAssocType);
        return geoAssocTypeRepository.save(geoAssocType);
    }

    /**
     * Update a geoAssocType.
     *
     * @param geoAssocType the entity to save.
     * @return the persisted entity.
     */
    public GeoAssocType update(GeoAssocType geoAssocType) {
        LOG.debug("Request to update GeoAssocType : {}", geoAssocType);
        return geoAssocTypeRepository.save(geoAssocType);
    }

    /**
     * Partially update a geoAssocType.
     *
     * @param geoAssocType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GeoAssocType> partialUpdate(GeoAssocType geoAssocType) {
        LOG.debug("Request to partially update GeoAssocType : {}", geoAssocType);

        return geoAssocTypeRepository
            .findById(geoAssocType.getId())
            .map(existingGeoAssocType -> {
                updateIfPresent(existingGeoAssocType::setName, geoAssocType.getName());

                return existingGeoAssocType;
            })
            .map(geoAssocTypeRepository::save);
    }

    /**
     * Get one geoAssocType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GeoAssocType> findOne(Long id) {
        LOG.debug("Request to get GeoAssocType : {}", id);
        return geoAssocTypeRepository.findById(id);
    }

    /**
     * Delete the geoAssocType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GeoAssocType : {}", id);
        geoAssocTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
