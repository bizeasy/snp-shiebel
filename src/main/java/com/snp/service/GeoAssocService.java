package com.snp.service;

import com.snp.domain.GeoAssoc;
import com.snp.repository.GeoAssocRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.GeoAssoc}.
 */
@Service
@Transactional
public class GeoAssocService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoAssocService.class);

    private final GeoAssocRepository geoAssocRepository;

    public GeoAssocService(GeoAssocRepository geoAssocRepository) {
        this.geoAssocRepository = geoAssocRepository;
    }

    /**
     * Save a geoAssoc.
     *
     * @param geoAssoc the entity to save.
     * @return the persisted entity.
     */
    public GeoAssoc save(GeoAssoc geoAssoc) {
        LOG.debug("Request to save GeoAssoc : {}", geoAssoc);
        return geoAssocRepository.save(geoAssoc);
    }

    /**
     * Update a geoAssoc.
     *
     * @param geoAssoc the entity to save.
     * @return the persisted entity.
     */
    public GeoAssoc update(GeoAssoc geoAssoc) {
        LOG.debug("Request to update GeoAssoc : {}", geoAssoc);
        return geoAssocRepository.save(geoAssoc);
    }

    /**
     * Partially update a geoAssoc.
     *
     * @param geoAssoc the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GeoAssoc> partialUpdate(GeoAssoc geoAssoc) {
        LOG.debug("Request to partially update GeoAssoc : {}", geoAssoc);

        return geoAssocRepository.findById(geoAssoc.getId()).map(geoAssocRepository::save);
    }

    /**
     * Get all the geoAssocs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GeoAssoc> findAllWithEagerRelationships(Pageable pageable) {
        return geoAssocRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one geoAssoc by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GeoAssoc> findOne(Long id) {
        LOG.debug("Request to get GeoAssoc : {}", id);
        return geoAssocRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the geoAssoc by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GeoAssoc : {}", id);
        geoAssocRepository.deleteById(id);
    }
}
