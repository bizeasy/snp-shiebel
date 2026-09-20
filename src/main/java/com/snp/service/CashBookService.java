package com.snp.service;

import com.snp.domain.CashBook;
import com.snp.repository.CashBookRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CashBook}.
 */
@Service
@Transactional
public class CashBookService {

    private static final Logger LOG = LoggerFactory.getLogger(CashBookService.class);

    private final CashBookRepository cashBookRepository;

    public CashBookService(CashBookRepository cashBookRepository) {
        this.cashBookRepository = cashBookRepository;
    }

    /**
     * Save a cashBook.
     *
     * @param cashBook the entity to save.
     * @return the persisted entity.
     */
    public CashBook save(CashBook cashBook) {
        LOG.debug("Request to save CashBook : {}", cashBook);
        return cashBookRepository.save(cashBook);
    }

    /**
     * Update a cashBook.
     *
     * @param cashBook the entity to save.
     * @return the persisted entity.
     */
    public CashBook update(CashBook cashBook) {
        LOG.debug("Request to update CashBook : {}", cashBook);
        return cashBookRepository.save(cashBook);
    }

    /**
     * Partially update a cashBook.
     *
     * @param cashBook the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CashBook> partialUpdate(CashBook cashBook) {
        LOG.debug("Request to partially update CashBook : {}", cashBook);

        return cashBookRepository
            .findById(cashBook.getId())
            .map(existingCashBook -> {
                updateIfPresent(existingCashBook::setTransactionDate, cashBook.getTransactionDate());
                updateIfPresent(existingCashBook::setDueDate, cashBook.getDueDate());
                updateIfPresent(existingCashBook::setAmount, cashBook.getAmount());
                updateIfPresent(existingCashBook::setDescription, cashBook.getDescription());
                updateIfPresent(existingCashBook::setVendorCustomerName, cashBook.getVendorCustomerName());
                updateIfPresent(existingCashBook::setInvoiceNo, cashBook.getInvoiceNo());
                updateIfPresent(existingCashBook::setInvoiceDate, cashBook.getInvoiceDate());
                updateIfPresent(existingCashBook::setPurpose, cashBook.getPurpose());
                updateIfPresent(existingCashBook::setRemarks, cashBook.getRemarks());
                updateIfPresent(existingCashBook::setProject, cashBook.getProject());
                updateIfPresent(existingCashBook::setUsageUnits, cashBook.getUsageUnits());
                updateIfPresent(existingCashBook::setRecurringTransaction, cashBook.getRecurringTransaction());
                updateIfPresent(existingCashBook::setFrequency, cashBook.getFrequency());

                return existingCashBook;
            })
            .map(cashBookRepository::save);
    }

    /**
     * Get all the cashBooks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CashBook> findAllWithEagerRelationships(Pageable pageable) {
        return cashBookRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one cashBook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashBook> findOne(Long id) {
        LOG.debug("Request to get CashBook : {}", id);
        return cashBookRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the cashBook by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CashBook : {}", id);
        cashBookRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
