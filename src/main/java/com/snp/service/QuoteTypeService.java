package com.snp.service;

import com.snp.domain.QuoteType;
import com.snp.repository.QuoteTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.QuoteType}.
 */
@Service
@Transactional
public class QuoteTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteTypeService.class);

    private final QuoteTypeRepository quoteTypeRepository;

    public QuoteTypeService(QuoteTypeRepository quoteTypeRepository) {
        this.quoteTypeRepository = quoteTypeRepository;
    }

    /**
     * Save a quoteType.
     *
     * @param quoteType the entity to save.
     * @return the persisted entity.
     */
    public QuoteType save(QuoteType quoteType) {
        LOG.debug("Request to save QuoteType : {}", quoteType);
        return quoteTypeRepository.save(quoteType);
    }

    /**
     * Update a quoteType.
     *
     * @param quoteType the entity to save.
     * @return the persisted entity.
     */
    public QuoteType update(QuoteType quoteType) {
        LOG.debug("Request to update QuoteType : {}", quoteType);
        return quoteTypeRepository.save(quoteType);
    }

    /**
     * Partially update a quoteType.
     *
     * @param quoteType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuoteType> partialUpdate(QuoteType quoteType) {
        LOG.debug("Request to partially update QuoteType : {}", quoteType);

        return quoteTypeRepository
            .findById(quoteType.getId())
            .map(existingQuoteType -> {
                updateIfPresent(existingQuoteType::setName, quoteType.getName());
                updateIfPresent(existingQuoteType::setDescription, quoteType.getDescription());

                return existingQuoteType;
            })
            .map(quoteTypeRepository::save);
    }

    /**
     * Get one quoteType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuoteType> findOne(Long id) {
        LOG.debug("Request to get QuoteType : {}", id);
        return quoteTypeRepository.findById(id);
    }

    /**
     * Delete the quoteType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QuoteType : {}", id);
        quoteTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
