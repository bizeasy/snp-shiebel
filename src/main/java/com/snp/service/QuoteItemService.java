package com.snp.service;

import com.snp.domain.QuoteItem;
import com.snp.repository.QuoteItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.QuoteItem}.
 */
@Service
@Transactional
public class QuoteItemService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteItemService.class);

    private final QuoteItemRepository quoteItemRepository;

    public QuoteItemService(QuoteItemRepository quoteItemRepository) {
        this.quoteItemRepository = quoteItemRepository;
    }

    /**
     * Save a quoteItem.
     *
     * @param quoteItem the entity to save.
     * @return the persisted entity.
     */
    public QuoteItem save(QuoteItem quoteItem) {
        LOG.debug("Request to save QuoteItem : {}", quoteItem);
        return quoteItemRepository.save(quoteItem);
    }

    /**
     * Update a quoteItem.
     *
     * @param quoteItem the entity to save.
     * @return the persisted entity.
     */
    public QuoteItem update(QuoteItem quoteItem) {
        LOG.debug("Request to update QuoteItem : {}", quoteItem);
        return quoteItemRepository.save(quoteItem);
    }

    /**
     * Partially update a quoteItem.
     *
     * @param quoteItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuoteItem> partialUpdate(QuoteItem quoteItem) {
        LOG.debug("Request to partially update QuoteItem : {}", quoteItem);

        return quoteItemRepository
            .findById(quoteItem.getId())
            .map(existingQuoteItem -> {
                updateIfPresent(existingQuoteItem::setSequenceNo, quoteItem.getSequenceNo());
                updateIfPresent(existingQuoteItem::setQuantity, quoteItem.getQuantity());
                updateIfPresent(existingQuoteItem::setSelectedAmount, quoteItem.getSelectedAmount());
                updateIfPresent(existingQuoteItem::setQuoteUnitPrice, quoteItem.getQuoteUnitPrice());
                updateIfPresent(existingQuoteItem::setTotalAmount, quoteItem.getTotalAmount());
                updateIfPresent(existingQuoteItem::setEstimatedDeliveryDate, quoteItem.getEstimatedDeliveryDate());
                updateIfPresent(existingQuoteItem::setLeadTimeDays, quoteItem.getLeadTimeDays());
                updateIfPresent(existingQuoteItem::setComments, quoteItem.getComments());

                return existingQuoteItem;
            })
            .map(quoteItemRepository::save);
    }

    /**
     * Get all the quoteItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<QuoteItem> findAllWithEagerRelationships(Pageable pageable) {
        return quoteItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one quoteItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuoteItem> findOne(Long id) {
        LOG.debug("Request to get QuoteItem : {}", id);
        return quoteItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the quoteItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QuoteItem : {}", id);
        quoteItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
