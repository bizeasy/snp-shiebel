package com.snp.service;

import com.snp.domain.Geo;
import com.snp.repository.GeoRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Geo}.
 */
@Service
@Transactional
public class GeoService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoService.class);

    private final GeoRepository geoRepository;

    public GeoService(GeoRepository geoRepository) {
        this.geoRepository = geoRepository;
    }

    /**
     * Save a geo.
     *
     * @param geo the entity to save.
     * @return the persisted entity.
     */
    public Geo save(Geo geo) {
        LOG.debug("Request to save Geo : {}", geo);
        return geoRepository.save(geo);
    }

    /**
     * Update a geo.
     *
     * @param geo the entity to save.
     * @return the persisted entity.
     */
    public Geo update(Geo geo) {
        LOG.debug("Request to update Geo : {}", geo);
        return geoRepository.save(geo);
    }

    /**
     * Partially update a geo.
     *
     * @param geo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Geo> partialUpdate(Geo geo) {
        LOG.debug("Request to partially update Geo : {}", geo);

        return geoRepository
            .findById(geo.getId())
            .map(existingGeo -> {
                updateIfPresent(existingGeo::setName, geo.getName());
                updateIfPresent(existingGeo::setCode, geo.getCode());
                updateIfPresent(existingGeo::setAbbreviation, geo.getAbbreviation());

                return existingGeo;
            })
            .map(geoRepository::save);
    }

    /**
     * Get all the geos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Geo> findAllWithEagerRelationships(Pageable pageable) {
        return geoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one geo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Geo> findOne(Long id) {
        LOG.debug("Request to get Geo : {}", id);
        return geoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the geo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Geo : {}", id);
        geoRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
