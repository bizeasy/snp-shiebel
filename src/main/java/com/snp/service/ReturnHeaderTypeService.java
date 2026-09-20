package com.snp.service;

import com.snp.domain.ReturnHeaderType;
import com.snp.repository.ReturnHeaderTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ReturnHeaderType}.
 */
@Service
@Transactional
public class ReturnHeaderTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnHeaderTypeService.class);

    private final ReturnHeaderTypeRepository returnHeaderTypeRepository;

    public ReturnHeaderTypeService(ReturnHeaderTypeRepository returnHeaderTypeRepository) {
        this.returnHeaderTypeRepository = returnHeaderTypeRepository;
    }

    /**
     * Save a returnHeaderType.
     *
     * @param returnHeaderType the entity to save.
     * @return the persisted entity.
     */
    public ReturnHeaderType save(ReturnHeaderType returnHeaderType) {
        LOG.debug("Request to save ReturnHeaderType : {}", returnHeaderType);
        return returnHeaderTypeRepository.save(returnHeaderType);
    }

    /**
     * Update a returnHeaderType.
     *
     * @param returnHeaderType the entity to save.
     * @return the persisted entity.
     */
    public ReturnHeaderType update(ReturnHeaderType returnHeaderType) {
        LOG.debug("Request to update ReturnHeaderType : {}", returnHeaderType);
        return returnHeaderTypeRepository.save(returnHeaderType);
    }

    /**
     * Partially update a returnHeaderType.
     *
     * @param returnHeaderType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReturnHeaderType> partialUpdate(ReturnHeaderType returnHeaderType) {
        LOG.debug("Request to partially update ReturnHeaderType : {}", returnHeaderType);

        return returnHeaderTypeRepository
            .findById(returnHeaderType.getId())
            .map(existingReturnHeaderType -> {
                updateIfPresent(existingReturnHeaderType::setName, returnHeaderType.getName());
                updateIfPresent(existingReturnHeaderType::setDescription, returnHeaderType.getDescription());

                return existingReturnHeaderType;
            })
            .map(returnHeaderTypeRepository::save);
    }

    /**
     * Get one returnHeaderType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReturnHeaderType> findOne(Long id) {
        LOG.debug("Request to get ReturnHeaderType : {}", id);
        return returnHeaderTypeRepository.findById(id);
    }

    /**
     * Delete the returnHeaderType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReturnHeaderType : {}", id);
        returnHeaderTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
