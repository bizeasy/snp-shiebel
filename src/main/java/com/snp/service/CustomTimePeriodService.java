package com.snp.service;

import com.snp.domain.CustomTimePeriod;
import com.snp.repository.CustomTimePeriodRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustomTimePeriod}.
 */
@Service
@Transactional
public class CustomTimePeriodService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomTimePeriodService.class);

    private final CustomTimePeriodRepository customTimePeriodRepository;

    public CustomTimePeriodService(CustomTimePeriodRepository customTimePeriodRepository) {
        this.customTimePeriodRepository = customTimePeriodRepository;
    }

    /**
     * Save a customTimePeriod.
     *
     * @param customTimePeriod the entity to save.
     * @return the persisted entity.
     */
    public CustomTimePeriod save(CustomTimePeriod customTimePeriod) {
        LOG.debug("Request to save CustomTimePeriod : {}", customTimePeriod);
        return customTimePeriodRepository.save(customTimePeriod);
    }

    /**
     * Update a customTimePeriod.
     *
     * @param customTimePeriod the entity to save.
     * @return the persisted entity.
     */
    public CustomTimePeriod update(CustomTimePeriod customTimePeriod) {
        LOG.debug("Request to update CustomTimePeriod : {}", customTimePeriod);
        return customTimePeriodRepository.save(customTimePeriod);
    }

    /**
     * Partially update a customTimePeriod.
     *
     * @param customTimePeriod the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomTimePeriod> partialUpdate(CustomTimePeriod customTimePeriod) {
        LOG.debug("Request to partially update CustomTimePeriod : {}", customTimePeriod);

        return customTimePeriodRepository
            .findById(customTimePeriod.getId())
            .map(existingCustomTimePeriod -> {
                updateIfPresent(existingCustomTimePeriod::setName, customTimePeriod.getName());
                updateIfPresent(existingCustomTimePeriod::setFromDate, customTimePeriod.getFromDate());
                updateIfPresent(existingCustomTimePeriod::setThruDate, customTimePeriod.getThruDate());

                return existingCustomTimePeriod;
            })
            .map(customTimePeriodRepository::save);
    }

    /**
     * Get one customTimePeriod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomTimePeriod> findOne(Long id) {
        LOG.debug("Request to get CustomTimePeriod : {}", id);
        return customTimePeriodRepository.findById(id);
    }

    /**
     * Delete the customTimePeriod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomTimePeriod : {}", id);
        customTimePeriodRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
