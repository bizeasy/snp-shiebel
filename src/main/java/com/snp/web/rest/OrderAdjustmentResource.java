package com.snp.web.rest;

import com.snp.domain.OrderAdjustment;
import com.snp.repository.OrderAdjustmentRepository;
import com.snp.service.OrderAdjustmentQueryService;
import com.snp.service.OrderAdjustmentService;
import com.snp.service.criteria.OrderAdjustmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderAdjustment}.
 */
@RestController
@RequestMapping("/api/order-adjustments")
public class OrderAdjustmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAdjustmentResource.class);

    private static final String ENTITY_NAME = "orderAdjustment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderAdjustmentService orderAdjustmentService;

    private final OrderAdjustmentRepository orderAdjustmentRepository;

    private final OrderAdjustmentQueryService orderAdjustmentQueryService;

    public OrderAdjustmentResource(
        OrderAdjustmentService orderAdjustmentService,
        OrderAdjustmentRepository orderAdjustmentRepository,
        OrderAdjustmentQueryService orderAdjustmentQueryService
    ) {
        this.orderAdjustmentService = orderAdjustmentService;
        this.orderAdjustmentRepository = orderAdjustmentRepository;
        this.orderAdjustmentQueryService = orderAdjustmentQueryService;
    }

    /**
     * {@code POST  /order-adjustments} : Create a new orderAdjustment.
     *
     * @param orderAdjustment the orderAdjustment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderAdjustment, or with status {@code 400 (Bad Request)} if the orderAdjustment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderAdjustment> createOrderAdjustment(@Valid @RequestBody OrderAdjustment orderAdjustment)
        throws URISyntaxException {
        LOG.debug("REST request to save OrderAdjustment : {}", orderAdjustment);
        if (orderAdjustment.getId() != null) {
            throw new BadRequestAlertException("A new orderAdjustment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderAdjustment = orderAdjustmentService.save(orderAdjustment);
        return ResponseEntity.created(new URI("/api/order-adjustments/" + orderAdjustment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderAdjustment.getId().toString()))
            .body(orderAdjustment);
    }

    /**
     * {@code PUT  /order-adjustments/:id} : Updates an existing orderAdjustment.
     *
     * @param id the id of the orderAdjustment to save.
     * @param orderAdjustment the orderAdjustment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAdjustment,
     * or with status {@code 400 (Bad Request)} if the orderAdjustment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderAdjustment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderAdjustment> updateOrderAdjustment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderAdjustment orderAdjustment
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderAdjustment : {}, {}", id, orderAdjustment);
        if (orderAdjustment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAdjustment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAdjustmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderAdjustment = orderAdjustmentService.update(orderAdjustment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAdjustment.getId().toString()))
            .body(orderAdjustment);
    }

    /**
     * {@code PATCH  /order-adjustments/:id} : Partial updates given fields of an existing orderAdjustment, field will ignore if it is null
     *
     * @param id the id of the orderAdjustment to save.
     * @param orderAdjustment the orderAdjustment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAdjustment,
     * or with status {@code 400 (Bad Request)} if the orderAdjustment is not valid,
     * or with status {@code 404 (Not Found)} if the orderAdjustment is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderAdjustment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderAdjustment> partialUpdateOrderAdjustment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderAdjustment orderAdjustment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderAdjustment : {}, {}", id, orderAdjustment);
        if (orderAdjustment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAdjustment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAdjustmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderAdjustment> result = orderAdjustmentService.partialUpdate(orderAdjustment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAdjustment.getId().toString())
        );
    }

    /**
     * {@code GET  /order-adjustments} : get all the Order Adjustments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Adjustments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderAdjustment>> getAllOrderAdjustments(
        OrderAdjustmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderAdjustments by criteria: {}", criteria);

        Page<OrderAdjustment> page = orderAdjustmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-adjustments/count} : count all the orderAdjustments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderAdjustments(OrderAdjustmentCriteria criteria) {
        LOG.debug("REST request to count OrderAdjustments by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderAdjustmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-adjustments/:id} : get the "id" orderAdjustment.
     *
     * @param id the id of the orderAdjustment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderAdjustment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderAdjustment> getOrderAdjustment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderAdjustment : {}", id);
        Optional<OrderAdjustment> orderAdjustment = orderAdjustmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderAdjustment);
    }

    /**
     * {@code DELETE  /order-adjustments/:id} : delete the "id" orderAdjustment.
     *
     * @param id the id of the orderAdjustment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderAdjustment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderAdjustment : {}", id);
        orderAdjustmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
