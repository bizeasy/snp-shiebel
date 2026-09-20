package com.snp.web.rest;

import com.snp.domain.OrderItemBilling;
import com.snp.repository.OrderItemBillingRepository;
import com.snp.service.OrderItemBillingQueryService;
import com.snp.service.OrderItemBillingService;
import com.snp.service.criteria.OrderItemBillingCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.OrderItemBilling}.
 */
@RestController
@RequestMapping("/api/order-item-billings")
public class OrderItemBillingResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemBillingResource.class);

    private static final String ENTITY_NAME = "orderItemBilling";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderItemBillingService orderItemBillingService;

    private final OrderItemBillingRepository orderItemBillingRepository;

    private final OrderItemBillingQueryService orderItemBillingQueryService;

    public OrderItemBillingResource(
        OrderItemBillingService orderItemBillingService,
        OrderItemBillingRepository orderItemBillingRepository,
        OrderItemBillingQueryService orderItemBillingQueryService
    ) {
        this.orderItemBillingService = orderItemBillingService;
        this.orderItemBillingRepository = orderItemBillingRepository;
        this.orderItemBillingQueryService = orderItemBillingQueryService;
    }

    /**
     * {@code POST  /order-item-billings} : Create a new orderItemBilling.
     *
     * @param orderItemBilling the orderItemBilling to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderItemBilling, or with status {@code 400 (Bad Request)} if the orderItemBilling already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderItemBilling> createOrderItemBilling(@Valid @RequestBody OrderItemBilling orderItemBilling)
        throws URISyntaxException {
        LOG.debug("REST request to save OrderItemBilling : {}", orderItemBilling);
        if (orderItemBilling.getId() != null) {
            throw new BadRequestAlertException("A new orderItemBilling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderItemBilling = orderItemBillingService.save(orderItemBilling);
        return ResponseEntity.created(new URI("/api/order-item-billings/" + orderItemBilling.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderItemBilling.getId().toString()))
            .body(orderItemBilling);
    }

    /**
     * {@code PUT  /order-item-billings/:id} : Updates an existing orderItemBilling.
     *
     * @param id the id of the orderItemBilling to save.
     * @param orderItemBilling the orderItemBilling to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemBilling,
     * or with status {@code 400 (Bad Request)} if the orderItemBilling is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderItemBilling couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemBilling> updateOrderItemBilling(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderItemBilling orderItemBilling
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderItemBilling : {}, {}", id, orderItemBilling);
        if (orderItemBilling.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemBilling.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemBillingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderItemBilling = orderItemBillingService.update(orderItemBilling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemBilling.getId().toString()))
            .body(orderItemBilling);
    }

    /**
     * {@code PATCH  /order-item-billings/:id} : Partial updates given fields of an existing orderItemBilling, field will ignore if it is null
     *
     * @param id the id of the orderItemBilling to save.
     * @param orderItemBilling the orderItemBilling to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemBilling,
     * or with status {@code 400 (Bad Request)} if the orderItemBilling is not valid,
     * or with status {@code 404 (Not Found)} if the orderItemBilling is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderItemBilling couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemBilling> partialUpdateOrderItemBilling(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderItemBilling orderItemBilling
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderItemBilling : {}, {}", id, orderItemBilling);
        if (orderItemBilling.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemBilling.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemBillingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderItemBilling> result = orderItemBillingService.partialUpdate(orderItemBilling);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemBilling.getId().toString())
        );
    }

    /**
     * {@code GET  /order-item-billings} : get all the Order Item Billings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Item Billings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderItemBilling>> getAllOrderItemBillings(
        OrderItemBillingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderItemBillings by criteria: {}", criteria);

        Page<OrderItemBilling> page = orderItemBillingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-item-billings/count} : count all the orderItemBillings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderItemBillings(OrderItemBillingCriteria criteria) {
        LOG.debug("REST request to count OrderItemBillings by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderItemBillingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-item-billings/:id} : get the "id" orderItemBilling.
     *
     * @param id the id of the orderItemBilling to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderItemBilling, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemBilling> getOrderItemBilling(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderItemBilling : {}", id);
        Optional<OrderItemBilling> orderItemBilling = orderItemBillingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemBilling);
    }

    /**
     * {@code DELETE  /order-item-billings/:id} : delete the "id" orderItemBilling.
     *
     * @param id the id of the orderItemBilling to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItemBilling(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderItemBilling : {}", id);
        orderItemBillingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
