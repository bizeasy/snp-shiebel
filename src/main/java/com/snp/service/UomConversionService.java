package com.snp.service;

import com.snp.domain.UomConversion;
import com.snp.repository.UomConversionRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.UomConversion}.
 */
@Service
@Transactional
public class UomConversionService {

    private static final Logger LOG = LoggerFactory.getLogger(UomConversionService.class);

    private final UomConversionRepository uomConversionRepository;

    public UomConversionService(UomConversionRepository uomConversionRepository) {
        this.uomConversionRepository = uomConversionRepository;
    }

    /**
     * Save a uomConversion.
     *
     * @param uomConversion the entity to save.
     * @return the persisted entity.
     */
    public UomConversion save(UomConversion uomConversion) {
        LOG.debug("Request to save UomConversion : {}", uomConversion);
        return uomConversionRepository.save(uomConversion);
    }

    /**
     * Update a uomConversion.
     *
     * @param uomConversion the entity to save.
     * @return the persisted entity.
     */
    public UomConversion update(UomConversion uomConversion) {
        LOG.debug("Request to update UomConversion : {}", uomConversion);
        return uomConversionRepository.save(uomConversion);
    }

    /**
     * Partially update a uomConversion.
     *
     * @param uomConversion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UomConversion> partialUpdate(UomConversion uomConversion) {
        LOG.debug("Request to partially update UomConversion : {}", uomConversion);

        return uomConversionRepository
            .findById(uomConversion.getId())
            .map(existingUomConversion -> {
                updateIfPresent(existingUomConversion::setName, uomConversion.getName());
                updateIfPresent(existingUomConversion::setConversionFactor, uomConversion.getConversionFactor());
                updateIfPresent(existingUomConversion::setFromDate, uomConversion.getFromDate());
                updateIfPresent(existingUomConversion::setToDate, uomConversion.getToDate());

                return existingUomConversion;
            })
            .map(uomConversionRepository::save);
    }

    /**
     * Get all the uomConversions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<UomConversion> findAllWithEagerRelationships() {
        return uomConversionRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one uomConversion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UomConversion> findOne(Long id) {
        LOG.debug("Request to get UomConversion : {}", id);
        return uomConversionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the uomConversion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UomConversion : {}", id);
        uomConversionRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
