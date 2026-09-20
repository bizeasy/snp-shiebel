package com.snp.service;

import com.snp.domain.Order;
import com.snp.repository.OrderRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Order}.
 */
@Service
@Transactional
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Save a order.
     *
     * @param order the entity to save.
     * @return the persisted entity.
     */
    public Order save(Order order) {
        LOG.debug("Request to save Order : {}", order);
        return orderRepository.save(order);
    }

    /**
     * Update a order.
     *
     * @param order the entity to save.
     * @return the persisted entity.
     */
    public Order update(Order order) {
        LOG.debug("Request to update Order : {}", order);
        return orderRepository.save(order);
    }

    /**
     * Partially update a order.
     *
     * @param order the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Order> partialUpdate(Order order) {
        LOG.debug("Request to partially update Order : {}", order);

        return orderRepository
            .findById(order.getId())
            .map(existingOrder -> {
                updateIfPresent(existingOrder::setName, order.getName());
                updateIfPresent(existingOrder::setOrderRefNo, order.getOrderRefNo());
                updateIfPresent(existingOrder::setExternalId, order.getExternalId());
                updateIfPresent(existingOrder::setOrderDate, order.getOrderDate());
                updateIfPresent(existingOrder::setEntryDate, order.getEntryDate());
                updateIfPresent(existingOrder::setDeliveryDate, order.getDeliveryDate());
                updateIfPresent(existingOrder::setPriority, order.getPriority());
                updateIfPresent(existingOrder::setIsRushOrder, order.getIsRushOrder());
                updateIfPresent(existingOrder::setNeedsInventoryIssuance, order.getNeedsInventoryIssuance());
                updateIfPresent(existingOrder::setRemainingSubTotal, order.getRemainingSubTotal());
                updateIfPresent(existingOrder::setGrandTotal, order.getGrandTotal());
                updateIfPresent(existingOrder::setProjectName, order.getProjectName());
                updateIfPresent(existingOrder::setVersion, order.getVersion());
                updateIfPresent(existingOrder::setVersionRef, order.getVersionRef());
                updateIfPresent(existingOrder::setDescription, order.getDescription());
                updateIfPresent(existingOrder::setRemarks, order.getRemarks());
                updateIfPresent(existingOrder::setPaymentTerms, order.getPaymentTerms());
                updateIfPresent(existingOrder::setDeliveryTerms, order.getDeliveryTerms());
                updateIfPresent(existingOrder::setInfo, order.getInfo());
                updateIfPresent(existingOrder::setAttr1, order.getAttr1());
                updateIfPresent(existingOrder::setAttr2, order.getAttr2());
                updateIfPresent(existingOrder::setAttr3, order.getAttr3());

                return existingOrder;
            })
            .map(orderRepository::save);
    }

    /**
     * Get all the orders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Order> findAllWithEagerRelationships(Pageable pageable) {
        return orderRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Order> findOne(Long id) {
        LOG.debug("Request to get Order : {}", id);
        return orderRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
