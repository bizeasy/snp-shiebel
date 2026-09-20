package com.snp.web.rest;

import com.snp.domain.OrderShipment;
import com.snp.repository.OrderShipmentRepository;
import com.snp.service.OrderShipmentQueryService;
import com.snp.service.OrderShipmentService;
import com.snp.service.criteria.OrderShipmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderShipment}.
 */
@RestController
@RequestMapping("/api/order-shipments")
public class OrderShipmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderShipmentResource.class);

    private static final String ENTITY_NAME = "orderShipment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderShipmentService orderShipmentService;

    private final OrderShipmentRepository orderShipmentRepository;

    private final OrderShipmentQueryService orderShipmentQueryService;

    public OrderShipmentResource(
        OrderShipmentService orderShipmentService,
        OrderShipmentRepository orderShipmentRepository,
        OrderShipmentQueryService orderShipmentQueryService
    ) {
        this.orderShipmentService = orderShipmentService;
        this.orderShipmentRepository = orderShipmentRepository;
        this.orderShipmentQueryService = orderShipmentQueryService;
    }

    /**
     * {@code POST  /order-shipments} : Create a new orderShipment.
     *
     * @param orderShipment the orderShipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderShipment, or with status {@code 400 (Bad Request)} if the orderShipment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderShipment> createOrderShipment(@Valid @RequestBody OrderShipment orderShipment) throws URISyntaxException {
        LOG.debug("REST request to save OrderShipment : {}", orderShipment);
        if (orderShipment.getId() != null) {
            throw new BadRequestAlertException("A new orderShipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderShipment = orderShipmentService.save(orderShipment);
        return ResponseEntity.created(new URI("/api/order-shipments/" + orderShipment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderShipment.getId().toString()))
            .body(orderShipment);
    }

    /**
     * {@code PUT  /order-shipments/:id} : Updates an existing orderShipment.
     *
     * @param id the id of the orderShipment to save.
     * @param orderShipment the orderShipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderShipment,
     * or with status {@code 400 (Bad Request)} if the orderShipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderShipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderShipment> updateOrderShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderShipment orderShipment
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderShipment : {}, {}", id, orderShipment);
        if (orderShipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderShipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderShipment = orderShipmentService.update(orderShipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderShipment.getId().toString()))
            .body(orderShipment);
    }

    /**
     * {@code PATCH  /order-shipments/:id} : Partial updates given fields of an existing orderShipment, field will ignore if it is null
     *
     * @param id the id of the orderShipment to save.
     * @param orderShipment the orderShipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderShipment,
     * or with status {@code 400 (Bad Request)} if the orderShipment is not valid,
     * or with status {@code 404 (Not Found)} if the orderShipment is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderShipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderShipment> partialUpdateOrderShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderShipment orderShipment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderShipment : {}, {}", id, orderShipment);
        if (orderShipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderShipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderShipment> result = orderShipmentService.partialUpdate(orderShipment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderShipment.getId().toString())
        );
    }

    /**
     * {@code GET  /order-shipments} : get all the Order Shipments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Shipments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderShipment>> getAllOrderShipments(
        OrderShipmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderShipments by criteria: {}", criteria);

        Page<OrderShipment> page = orderShipmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-shipments/count} : count all the orderShipments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderShipments(OrderShipmentCriteria criteria) {
        LOG.debug("REST request to count OrderShipments by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderShipmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-shipments/:id} : get the "id" orderShipment.
     *
     * @param id the id of the orderShipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderShipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderShipment> getOrderShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderShipment : {}", id);
        Optional<OrderShipment> orderShipment = orderShipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderShipment);
    }

    /**
     * {@code DELETE  /order-shipments/:id} : delete the "id" orderShipment.
     *
     * @param id the id of the orderShipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderShipment : {}", id);
        orderShipmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
