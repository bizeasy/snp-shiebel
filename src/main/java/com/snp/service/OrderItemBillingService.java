package com.snp.service;

import com.snp.domain.OrderItemBilling;
import com.snp.repository.OrderItemBillingRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.OrderItemBilling}.
 */
@Service
@Transactional
public class OrderItemBillingService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemBillingService.class);

    private final OrderItemBillingRepository orderItemBillingRepository;

    public OrderItemBillingService(OrderItemBillingRepository orderItemBillingRepository) {
        this.orderItemBillingRepository = orderItemBillingRepository;
    }

    /**
     * Save a orderItemBilling.
     *
     * @param orderItemBilling the entity to save.
     * @return the persisted entity.
     */
    public OrderItemBilling save(OrderItemBilling orderItemBilling) {
        LOG.debug("Request to save OrderItemBilling : {}", orderItemBilling);
        return orderItemBillingRepository.save(orderItemBilling);
    }

    /**
     * Update a orderItemBilling.
     *
     * @param orderItemBilling the entity to save.
     * @return the persisted entity.
     */
    public OrderItemBilling update(OrderItemBilling orderItemBilling) {
        LOG.debug("Request to update OrderItemBilling : {}", orderItemBilling);
        return orderItemBillingRepository.save(orderItemBilling);
    }

    /**
     * Partially update a orderItemBilling.
     *
     * @param orderItemBilling the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItemBilling> partialUpdate(OrderItemBilling orderItemBilling) {
        LOG.debug("Request to partially update OrderItemBilling : {}", orderItemBilling);

        return orderItemBillingRepository
            .findById(orderItemBilling.getId())
            .map(existingOrderItemBilling -> {
                updateIfPresent(existingOrderItemBilling::setQuantity, orderItemBilling.getQuantity());
                updateIfPresent(existingOrderItemBilling::setAmount, orderItemBilling.getAmount());

                return existingOrderItemBilling;
            })
            .map(orderItemBillingRepository::save);
    }

    /**
     * Get one orderItemBilling by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItemBilling> findOne(Long id) {
        LOG.debug("Request to get OrderItemBilling : {}", id);
        return orderItemBillingRepository.findById(id);
    }

    /**
     * Delete the orderItemBilling by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderItemBilling : {}", id);
        orderItemBillingRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
