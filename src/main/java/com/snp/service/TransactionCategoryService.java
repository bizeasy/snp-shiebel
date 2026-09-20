package com.snp.service;

import com.snp.domain.TransactionCategory;
import com.snp.repository.TransactionCategoryRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.TransactionCategory}.
 */
@Service
@Transactional
public class TransactionCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionCategoryService.class);

    private final TransactionCategoryRepository transactionCategoryRepository;

    public TransactionCategoryService(TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    /**
     * Save a transactionCategory.
     *
     * @param transactionCategory the entity to save.
     * @return the persisted entity.
     */
    public TransactionCategory save(TransactionCategory transactionCategory) {
        LOG.debug("Request to save TransactionCategory : {}", transactionCategory);
        return transactionCategoryRepository.save(transactionCategory);
    }

    /**
     * Update a transactionCategory.
     *
     * @param transactionCategory the entity to save.
     * @return the persisted entity.
     */
    public TransactionCategory update(TransactionCategory transactionCategory) {
        LOG.debug("Request to update TransactionCategory : {}", transactionCategory);
        return transactionCategoryRepository.save(transactionCategory);
    }

    /**
     * Partially update a transactionCategory.
     *
     * @param transactionCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransactionCategory> partialUpdate(TransactionCategory transactionCategory) {
        LOG.debug("Request to partially update TransactionCategory : {}", transactionCategory);

        return transactionCategoryRepository
            .findById(transactionCategory.getId())
            .map(existingTransactionCategory -> {
                updateIfPresent(existingTransactionCategory::setName, transactionCategory.getName());
                updateIfPresent(existingTransactionCategory::setDescription, transactionCategory.getDescription());

                return existingTransactionCategory;
            })
            .map(transactionCategoryRepository::save);
    }

    /**
     * Get one transactionCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionCategory> findOne(Long id) {
        LOG.debug("Request to get TransactionCategory : {}", id);
        return transactionCategoryRepository.findById(id);
    }

    /**
     * Delete the transactionCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransactionCategory : {}", id);
        transactionCategoryRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
