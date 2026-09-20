package com.snp.service;

import com.snp.domain.Quote;
import com.snp.repository.QuoteRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Quote}.
 */
@Service
@Transactional
public class QuoteService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteService.class);

    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    /**
     * Save a quote.
     *
     * @param quote the entity to save.
     * @return the persisted entity.
     */
    public Quote save(Quote quote) {
        LOG.debug("Request to save Quote : {}", quote);
        return quoteRepository.save(quote);
    }

    /**
     * Update a quote.
     *
     * @param quote the entity to save.
     * @return the persisted entity.
     */
    public Quote update(Quote quote) {
        LOG.debug("Request to update Quote : {}", quote);
        return quoteRepository.save(quote);
    }

    /**
     * Partially update a quote.
     *
     * @param quote the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Quote> partialUpdate(Quote quote) {
        LOG.debug("Request to partially update Quote : {}", quote);

        return quoteRepository
            .findById(quote.getId())
            .map(existingQuote -> {
                updateIfPresent(existingQuote::setName, quote.getName());
                updateIfPresent(existingQuote::setOfferNo, quote.getOfferNo());
                updateIfPresent(existingQuote::setIssueDate, quote.getIssueDate());
                updateIfPresent(existingQuote::setValidFromDate, quote.getValidFromDate());
                updateIfPresent(existingQuote::setValidThruDate, quote.getValidThruDate());
                updateIfPresent(existingQuote::setDescription, quote.getDescription());
                updateIfPresent(existingQuote::setOfferValue, quote.getOfferValue());
                updateIfPresent(existingQuote::setRemarks, quote.getRemarks());

                return existingQuote;
            })
            .map(quoteRepository::save);
    }

    /**
     * Get all the quotes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Quote> findAllWithEagerRelationships(Pageable pageable) {
        return quoteRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one quote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Quote> findOne(Long id) {
        LOG.debug("Request to get Quote : {}", id);
        return quoteRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the quote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Quote : {}", id);
        quoteRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
