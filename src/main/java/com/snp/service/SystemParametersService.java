package com.snp.service;

import com.snp.domain.SystemParameters;
import com.snp.repository.SystemParametersRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.SystemParameters}.
 */
@Service
@Transactional
public class SystemParametersService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemParametersService.class);

    private final SystemParametersRepository systemParametersRepository;

    public SystemParametersService(SystemParametersRepository systemParametersRepository) {
        this.systemParametersRepository = systemParametersRepository;
    }

    /**
     * Save a systemParameters.
     *
     * @param systemParameters the entity to save.
     * @return the persisted entity.
     */
    public SystemParameters save(SystemParameters systemParameters) {
        LOG.debug("Request to save SystemParameters : {}", systemParameters);
        return systemParametersRepository.save(systemParameters);
    }

    /**
     * Update a systemParameters.
     *
     * @param systemParameters the entity to save.
     * @return the persisted entity.
     */
    public SystemParameters update(SystemParameters systemParameters) {
        LOG.debug("Request to update SystemParameters : {}", systemParameters);
        return systemParametersRepository.save(systemParameters);
    }

    /**
     * Partially update a systemParameters.
     *
     * @param systemParameters the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SystemParameters> partialUpdate(SystemParameters systemParameters) {
        LOG.debug("Request to partially update SystemParameters : {}", systemParameters);

        return systemParametersRepository
            .findById(systemParameters.getId())
            .map(existingSystemParameters -> {
                updateIfPresent(existingSystemParameters::setKey, systemParameters.getKey());
                updateIfPresent(existingSystemParameters::setValue, systemParameters.getValue());
                updateIfPresent(existingSystemParameters::setDescription, systemParameters.getDescription());
                updateIfPresent(existingSystemParameters::setEnvironment, systemParameters.getEnvironment());

                return existingSystemParameters;
            })
            .map(systemParametersRepository::save);
    }

    /**
     * Get one systemParameters by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemParameters> findOne(Long id) {
        LOG.debug("Request to get SystemParameters : {}", id);
        return systemParametersRepository.findById(id);
    }

    /**
     * Delete the systemParameters by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SystemParameters : {}", id);
        systemParametersRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
