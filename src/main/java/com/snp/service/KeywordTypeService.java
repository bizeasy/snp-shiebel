package com.snp.service;

import com.snp.domain.KeywordType;
import com.snp.repository.KeywordTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.KeywordType}.
 */
@Service
@Transactional
public class KeywordTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(KeywordTypeService.class);

    private final KeywordTypeRepository keywordTypeRepository;

    public KeywordTypeService(KeywordTypeRepository keywordTypeRepository) {
        this.keywordTypeRepository = keywordTypeRepository;
    }

    /**
     * Save a keywordType.
     *
     * @param keywordType the entity to save.
     * @return the persisted entity.
     */
    public KeywordType save(KeywordType keywordType) {
        LOG.debug("Request to save KeywordType : {}", keywordType);
        return keywordTypeRepository.save(keywordType);
    }

    /**
     * Update a keywordType.
     *
     * @param keywordType the entity to save.
     * @return the persisted entity.
     */
    public KeywordType update(KeywordType keywordType) {
        LOG.debug("Request to update KeywordType : {}", keywordType);
        return keywordTypeRepository.save(keywordType);
    }

    /**
     * Partially update a keywordType.
     *
     * @param keywordType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KeywordType> partialUpdate(KeywordType keywordType) {
        LOG.debug("Request to partially update KeywordType : {}", keywordType);

        return keywordTypeRepository
            .findById(keywordType.getId())
            .map(existingKeywordType -> {
                updateIfPresent(existingKeywordType::setName, keywordType.getName());

                return existingKeywordType;
            })
            .map(keywordTypeRepository::save);
    }

    /**
     * Get one keywordType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KeywordType> findOne(Long id) {
        LOG.debug("Request to get KeywordType : {}", id);
        return keywordTypeRepository.findById(id);
    }

    /**
     * Delete the keywordType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KeywordType : {}", id);
        keywordTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
