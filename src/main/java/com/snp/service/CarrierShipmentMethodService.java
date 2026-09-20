package com.snp.service;

import com.snp.domain.CarrierShipmentMethod;
import com.snp.repository.CarrierShipmentMethodRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CarrierShipmentMethod}.
 */
@Service
@Transactional
public class CarrierShipmentMethodService {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierShipmentMethodService.class);

    private final CarrierShipmentMethodRepository carrierShipmentMethodRepository;

    public CarrierShipmentMethodService(CarrierShipmentMethodRepository carrierShipmentMethodRepository) {
        this.carrierShipmentMethodRepository = carrierShipmentMethodRepository;
    }

    /**
     * Save a carrierShipmentMethod.
     *
     * @param carrierShipmentMethod the entity to save.
     * @return the persisted entity.
     */
    public CarrierShipmentMethod save(CarrierShipmentMethod carrierShipmentMethod) {
        LOG.debug("Request to save CarrierShipmentMethod : {}", carrierShipmentMethod);
        return carrierShipmentMethodRepository.save(carrierShipmentMethod);
    }

    /**
     * Update a carrierShipmentMethod.
     *
     * @param carrierShipmentMethod the entity to save.
     * @return the persisted entity.
     */
    public CarrierShipmentMethod update(CarrierShipmentMethod carrierShipmentMethod) {
        LOG.debug("Request to update CarrierShipmentMethod : {}", carrierShipmentMethod);
        return carrierShipmentMethodRepository.save(carrierShipmentMethod);
    }

    /**
     * Partially update a carrierShipmentMethod.
     *
     * @param carrierShipmentMethod the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CarrierShipmentMethod> partialUpdate(CarrierShipmentMethod carrierShipmentMethod) {
        LOG.debug("Request to partially update CarrierShipmentMethod : {}", carrierShipmentMethod);

        return carrierShipmentMethodRepository
            .findById(carrierShipmentMethod.getId())
            .map(existingCarrierShipmentMethod -> {
                updateIfPresent(existingCarrierShipmentMethod::setSequenceNo, carrierShipmentMethod.getSequenceNo());
                updateIfPresent(existingCarrierShipmentMethod::setCarrierServiceCode, carrierShipmentMethod.getCarrierServiceCode());

                return existingCarrierShipmentMethod;
            })
            .map(carrierShipmentMethodRepository::save);
    }

    /**
     * Get all the carrierShipmentMethods with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<CarrierShipmentMethod> findAllWithEagerRelationships() {
        return carrierShipmentMethodRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one carrierShipmentMethod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CarrierShipmentMethod> findOne(Long id) {
        LOG.debug("Request to get CarrierShipmentMethod : {}", id);
        return carrierShipmentMethodRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the carrierShipmentMethod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CarrierShipmentMethod : {}", id);
        carrierShipmentMethodRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
