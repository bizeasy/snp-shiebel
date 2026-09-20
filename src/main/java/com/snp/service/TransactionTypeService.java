package com.snp.service;

import com.snp.domain.TransactionType;
import com.snp.repository.TransactionTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.TransactionType}.
 */
@Service
@Transactional
public class TransactionTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTypeService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * Save a transactionType.
     *
     * @param transactionType the entity to save.
     * @return the persisted entity.
     */
    public TransactionType save(TransactionType transactionType) {
        LOG.debug("Request to save TransactionType : {}", transactionType);
        return transactionTypeRepository.save(transactionType);
    }

    /**
     * Update a transactionType.
     *
     * @param transactionType the entity to save.
     * @return the persisted entity.
     */
    public TransactionType update(TransactionType transactionType) {
        LOG.debug("Request to update TransactionType : {}", transactionType);
        return transactionTypeRepository.save(transactionType);
    }

    /**
     * Partially update a transactionType.
     *
     * @param transactionType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransactionType> partialUpdate(TransactionType transactionType) {
        LOG.debug("Request to partially update TransactionType : {}", transactionType);

        return transactionTypeRepository
            .findById(transactionType.getId())
            .map(existingTransactionType -> {
                updateIfPresent(existingTransactionType::setName, transactionType.getName());
                updateIfPresent(existingTransactionType::setDescription, transactionType.getDescription());

                return existingTransactionType;
            })
            .map(transactionTypeRepository::save);
    }

    /**
     * Get all the transactionTypes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<TransactionType> findAllWithEagerRelationships() {
        return transactionTypeRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one transactionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionType> findOne(Long id) {
        LOG.debug("Request to get TransactionType : {}", id);
        return transactionTypeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the transactionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransactionType : {}", id);
        transactionTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
