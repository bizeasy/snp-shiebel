package com.snp.service;

import com.snp.domain.GeoPoint;
import com.snp.repository.GeoPointRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.GeoPoint}.
 */
@Service
@Transactional
public class GeoPointService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoPointService.class);

    private final GeoPointRepository geoPointRepository;

    public GeoPointService(GeoPointRepository geoPointRepository) {
        this.geoPointRepository = geoPointRepository;
    }

    /**
     * Save a geoPoint.
     *
     * @param geoPoint the entity to save.
     * @return the persisted entity.
     */
    public GeoPoint save(GeoPoint geoPoint) {
        LOG.debug("Request to save GeoPoint : {}", geoPoint);
        return geoPointRepository.save(geoPoint);
    }

    /**
     * Update a geoPoint.
     *
     * @param geoPoint the entity to save.
     * @return the persisted entity.
     */
    public GeoPoint update(GeoPoint geoPoint) {
        LOG.debug("Request to update GeoPoint : {}", geoPoint);
        return geoPointRepository.save(geoPoint);
    }

    /**
     * Partially update a geoPoint.
     *
     * @param geoPoint the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GeoPoint> partialUpdate(GeoPoint geoPoint) {
        LOG.debug("Request to partially update GeoPoint : {}", geoPoint);

        return geoPointRepository
            .findById(geoPoint.getId())
            .map(existingGeoPoint -> {
                updateIfPresent(existingGeoPoint::setLatitude, geoPoint.getLatitude());
                updateIfPresent(existingGeoPoint::setLongitude, geoPoint.getLongitude());

                return existingGeoPoint;
            })
            .map(geoPointRepository::save);
    }

    /**
     * Get one geoPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GeoPoint> findOne(Long id) {
        LOG.debug("Request to get GeoPoint : {}", id);
        return geoPointRepository.findById(id);
    }

    /**
     * Delete the geoPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GeoPoint : {}", id);
        geoPointRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
