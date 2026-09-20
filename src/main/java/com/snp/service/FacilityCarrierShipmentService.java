package com.snp.service;

import com.snp.domain.FacilityCarrierShipment;
import com.snp.repository.FacilityCarrierShipmentRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityCarrierShipment}.
 */
@Service
@Transactional
public class FacilityCarrierShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityCarrierShipmentService.class);

    private final FacilityCarrierShipmentRepository facilityCarrierShipmentRepository;

    public FacilityCarrierShipmentService(FacilityCarrierShipmentRepository facilityCarrierShipmentRepository) {
        this.facilityCarrierShipmentRepository = facilityCarrierShipmentRepository;
    }

    /**
     * Save a facilityCarrierShipment.
     *
     * @param facilityCarrierShipment the entity to save.
     * @return the persisted entity.
     */
    public FacilityCarrierShipment save(FacilityCarrierShipment facilityCarrierShipment) {
        LOG.debug("Request to save FacilityCarrierShipment : {}", facilityCarrierShipment);
        return facilityCarrierShipmentRepository.save(facilityCarrierShipment);
    }

    /**
     * Update a facilityCarrierShipment.
     *
     * @param facilityCarrierShipment the entity to save.
     * @return the persisted entity.
     */
    public FacilityCarrierShipment update(FacilityCarrierShipment facilityCarrierShipment) {
        LOG.debug("Request to update FacilityCarrierShipment : {}", facilityCarrierShipment);
        return facilityCarrierShipmentRepository.save(facilityCarrierShipment);
    }

    /**
     * Partially update a facilityCarrierShipment.
     *
     * @param facilityCarrierShipment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityCarrierShipment> partialUpdate(FacilityCarrierShipment facilityCarrierShipment) {
        LOG.debug("Request to partially update FacilityCarrierShipment : {}", facilityCarrierShipment);

        return facilityCarrierShipmentRepository.findById(facilityCarrierShipment.getId()).map(facilityCarrierShipmentRepository::save);
    }

    /**
     * Get all the facilityCarrierShipments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<FacilityCarrierShipment> findAllWithEagerRelationships() {
        return facilityCarrierShipmentRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one facilityCarrierShipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityCarrierShipment> findOne(Long id) {
        LOG.debug("Request to get FacilityCarrierShipment : {}", id);
        return facilityCarrierShipmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facilityCarrierShipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityCarrierShipment : {}", id);
        facilityCarrierShipmentRepository.deleteById(id);
    }
}
