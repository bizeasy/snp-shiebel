package com.snp.web.rest;

import com.snp.domain.OrderStatus;
import com.snp.repository.OrderStatusRepository;
import com.snp.service.OrderStatusQueryService;
import com.snp.service.OrderStatusService;
import com.snp.service.criteria.OrderStatusCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderStatus}.
 */
@RestController
@RequestMapping("/api/order-statuses")
public class OrderStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderStatusResource.class);

    private static final String ENTITY_NAME = "orderStatus";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderStatusService orderStatusService;

    private final OrderStatusRepository orderStatusRepository;

    private final OrderStatusQueryService orderStatusQueryService;

    public OrderStatusResource(
        OrderStatusService orderStatusService,
        OrderStatusRepository orderStatusRepository,
        OrderStatusQueryService orderStatusQueryService
    ) {
        this.orderStatusService = orderStatusService;
        this.orderStatusRepository = orderStatusRepository;
        this.orderStatusQueryService = orderStatusQueryService;
    }

    /**
     * {@code POST  /order-statuses} : Create a new orderStatus.
     *
     * @param orderStatus the orderStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderStatus, or with status {@code 400 (Bad Request)} if the orderStatus already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderStatus> createOrderStatus(@Valid @RequestBody OrderStatus orderStatus) throws URISyntaxException {
        LOG.debug("REST request to save OrderStatus : {}", orderStatus);
        if (orderStatus.getId() != null) {
            throw new BadRequestAlertException("A new orderStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderStatus = orderStatusService.save(orderStatus);
        return ResponseEntity.created(new URI("/api/order-statuses/" + orderStatus.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderStatus.getId().toString()))
            .body(orderStatus);
    }

    /**
     * {@code PUT  /order-statuses/:id} : Updates an existing orderStatus.
     *
     * @param id the id of the orderStatus to save.
     * @param orderStatus the orderStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderStatus,
     * or with status {@code 400 (Bad Request)} if the orderStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderStatus> updateOrderStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderStatus orderStatus
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderStatus : {}, {}", id, orderStatus);
        if (orderStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderStatus = orderStatusService.update(orderStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderStatus.getId().toString()))
            .body(orderStatus);
    }

    /**
     * {@code PATCH  /order-statuses/:id} : Partial updates given fields of an existing orderStatus, field will ignore if it is null
     *
     * @param id the id of the orderStatus to save.
     * @param orderStatus the orderStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderStatus,
     * or with status {@code 400 (Bad Request)} if the orderStatus is not valid,
     * or with status {@code 404 (Not Found)} if the orderStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderStatus> partialUpdateOrderStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderStatus orderStatus
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderStatus : {}, {}", id, orderStatus);
        if (orderStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderStatus> result = orderStatusService.partialUpdate(orderStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /order-statuses} : get all the Order Statuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Statuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderStatus>> getAllOrderStatuses(
        OrderStatusCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderStatuses by criteria: {}", criteria);

        Page<OrderStatus> page = orderStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-statuses/count} : count all the orderStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderStatuses(OrderStatusCriteria criteria) {
        LOG.debug("REST request to count OrderStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-statuses/:id} : get the "id" orderStatus.
     *
     * @param id the id of the orderStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderStatus : {}", id);
        Optional<OrderStatus> orderStatus = orderStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderStatus);
    }

    /**
     * {@code DELETE  /order-statuses/:id} : delete the "id" orderStatus.
     *
     * @param id the id of the orderStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderStatus : {}", id);
        orderStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
