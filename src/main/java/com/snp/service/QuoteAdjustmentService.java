package com.snp.service;

import com.snp.domain.QuoteAdjustment;
import com.snp.repository.QuoteAdjustmentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.QuoteAdjustment}.
 */
@Service
@Transactional
public class QuoteAdjustmentService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteAdjustmentService.class);

    private final QuoteAdjustmentRepository quoteAdjustmentRepository;

    public QuoteAdjustmentService(QuoteAdjustmentRepository quoteAdjustmentRepository) {
        this.quoteAdjustmentRepository = quoteAdjustmentRepository;
    }

    /**
     * Save a quoteAdjustment.
     *
     * @param quoteAdjustment the entity to save.
     * @return the persisted entity.
     */
    public QuoteAdjustment save(QuoteAdjustment quoteAdjustment) {
        LOG.debug("Request to save QuoteAdjustment : {}", quoteAdjustment);
        return quoteAdjustmentRepository.save(quoteAdjustment);
    }

    /**
     * Update a quoteAdjustment.
     *
     * @param quoteAdjustment the entity to save.
     * @return the persisted entity.
     */
    public QuoteAdjustment update(QuoteAdjustment quoteAdjustment) {
        LOG.debug("Request to update QuoteAdjustment : {}", quoteAdjustment);
        return quoteAdjustmentRepository.save(quoteAdjustment);
    }

    /**
     * Partially update a quoteAdjustment.
     *
     * @param quoteAdjustment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuoteAdjustment> partialUpdate(QuoteAdjustment quoteAdjustment) {
        LOG.debug("Request to partially update QuoteAdjustment : {}", quoteAdjustment);

        return quoteAdjustmentRepository
            .findById(quoteAdjustment.getId())
            .map(existingQuoteAdjustment -> {
                updateIfPresent(existingQuoteAdjustment::setDescription, quoteAdjustment.getDescription());
                updateIfPresent(existingQuoteAdjustment::setComments, quoteAdjustment.getComments());
                updateIfPresent(existingQuoteAdjustment::setAmount, quoteAdjustment.getAmount());
                updateIfPresent(existingQuoteAdjustment::setSourcePercentage, quoteAdjustment.getSourcePercentage());
                updateIfPresent(existingQuoteAdjustment::setSourceRefId, quoteAdjustment.getSourceRefId());
                updateIfPresent(existingQuoteAdjustment::setExemptAmount, quoteAdjustment.getExemptAmount());
                updateIfPresent(existingQuoteAdjustment::setIncludeInTax, quoteAdjustment.getIncludeInTax());
                updateIfPresent(existingQuoteAdjustment::setIncludeInShipping, quoteAdjustment.getIncludeInShipping());
                updateIfPresent(existingQuoteAdjustment::setIncludedInPrice, quoteAdjustment.getIncludedInPrice());
                updateIfPresent(existingQuoteAdjustment::setEntryDate, quoteAdjustment.getEntryDate());

                return existingQuoteAdjustment;
            })
            .map(quoteAdjustmentRepository::save);
    }

    /**
     * Get all the quoteAdjustments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<QuoteAdjustment> findAllWithEagerRelationships(Pageable pageable) {
        return quoteAdjustmentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one quoteAdjustment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuoteAdjustment> findOne(Long id) {
        LOG.debug("Request to get QuoteAdjustment : {}", id);
        return quoteAdjustmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the quoteAdjustment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QuoteAdjustment : {}", id);
        quoteAdjustmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
