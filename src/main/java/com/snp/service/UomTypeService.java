package com.snp.service;

import com.snp.domain.UomType;
import com.snp.repository.UomTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.UomType}.
 */
@Service
@Transactional
public class UomTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(UomTypeService.class);

    private final UomTypeRepository uomTypeRepository;

    public UomTypeService(UomTypeRepository uomTypeRepository) {
        this.uomTypeRepository = uomTypeRepository;
    }

    /**
     * Save a uomType.
     *
     * @param uomType the entity to save.
     * @return the persisted entity.
     */
    public UomType save(UomType uomType) {
        LOG.debug("Request to save UomType : {}", uomType);
        return uomTypeRepository.save(uomType);
    }

    /**
     * Update a uomType.
     *
     * @param uomType the entity to save.
     * @return the persisted entity.
     */
    public UomType update(UomType uomType) {
        LOG.debug("Request to update UomType : {}", uomType);
        return uomTypeRepository.save(uomType);
    }

    /**
     * Partially update a uomType.
     *
     * @param uomType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UomType> partialUpdate(UomType uomType) {
        LOG.debug("Request to partially update UomType : {}", uomType);

        return uomTypeRepository
            .findById(uomType.getId())
            .map(existingUomType -> {
                updateIfPresent(existingUomType::setName, uomType.getName());
                updateIfPresent(existingUomType::setDescription, uomType.getDescription());

                return existingUomType;
            })
            .map(uomTypeRepository::save);
    }

    /**
     * Get one uomType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UomType> findOne(Long id) {
        LOG.debug("Request to get UomType : {}", id);
        return uomTypeRepository.findById(id);
    }

    /**
     * Delete the uomType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UomType : {}", id);
        uomTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
