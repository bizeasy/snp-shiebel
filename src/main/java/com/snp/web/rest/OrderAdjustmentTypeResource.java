package com.snp.web.rest;

import com.snp.domain.OrderAdjustmentType;
import com.snp.repository.OrderAdjustmentTypeRepository;
import com.snp.service.OrderAdjustmentTypeQueryService;
import com.snp.service.OrderAdjustmentTypeService;
import com.snp.service.criteria.OrderAdjustmentTypeCriteria;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.OrderAdjustmentType}.
 */
@RestController
@RequestMapping("/api/order-adjustment-types")
public class OrderAdjustmentTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAdjustmentTypeResource.class);

    private static final String ENTITY_NAME = "orderAdjustmentType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderAdjustmentTypeService orderAdjustmentTypeService;

    private final OrderAdjustmentTypeRepository orderAdjustmentTypeRepository;

    private final OrderAdjustmentTypeQueryService orderAdjustmentTypeQueryService;

    public OrderAdjustmentTypeResource(
        OrderAdjustmentTypeService orderAdjustmentTypeService,
        OrderAdjustmentTypeRepository orderAdjustmentTypeRepository,
        OrderAdjustmentTypeQueryService orderAdjustmentTypeQueryService
    ) {
        this.orderAdjustmentTypeService = orderAdjustmentTypeService;
        this.orderAdjustmentTypeRepository = orderAdjustmentTypeRepository;
        this.orderAdjustmentTypeQueryService = orderAdjustmentTypeQueryService;
    }

    /**
     * {@code POST  /order-adjustment-types} : Create a new orderAdjustmentType.
     *
     * @param orderAdjustmentType the orderAdjustmentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderAdjustmentType, or with status {@code 400 (Bad Request)} if the orderAdjustmentType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderAdjustmentType> createOrderAdjustmentType(@Valid @RequestBody OrderAdjustmentType orderAdjustmentType)
        throws URISyntaxException {
        LOG.debug("REST request to save OrderAdjustmentType : {}", orderAdjustmentType);
        if (orderAdjustmentType.getId() != null) {
            throw new BadRequestAlertException("A new orderAdjustmentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderAdjustmentType = orderAdjustmentTypeService.save(orderAdjustmentType);
        return ResponseEntity.created(new URI("/api/order-adjustment-types/" + orderAdjustmentType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderAdjustmentType.getId().toString()))
            .body(orderAdjustmentType);
    }

    /**
     * {@code PUT  /order-adjustment-types/:id} : Updates an existing orderAdjustmentType.
     *
     * @param id the id of the orderAdjustmentType to save.
     * @param orderAdjustmentType the orderAdjustmentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAdjustmentType,
     * or with status {@code 400 (Bad Request)} if the orderAdjustmentType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderAdjustmentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderAdjustmentType> updateOrderAdjustmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderAdjustmentType orderAdjustmentType
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderAdjustmentType : {}, {}", id, orderAdjustmentType);
        if (orderAdjustmentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAdjustmentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAdjustmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderAdjustmentType = orderAdjustmentTypeService.update(orderAdjustmentType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAdjustmentType.getId().toString()))
            .body(orderAdjustmentType);
    }

    /**
     * {@code PATCH  /order-adjustment-types/:id} : Partial updates given fields of an existing orderAdjustmentType, field will ignore if it is null
     *
     * @param id the id of the orderAdjustmentType to save.
     * @param orderAdjustmentType the orderAdjustmentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAdjustmentType,
     * or with status {@code 400 (Bad Request)} if the orderAdjustmentType is not valid,
     * or with status {@code 404 (Not Found)} if the orderAdjustmentType is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderAdjustmentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderAdjustmentType> partialUpdateOrderAdjustmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderAdjustmentType orderAdjustmentType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderAdjustmentType : {}, {}", id, orderAdjustmentType);
        if (orderAdjustmentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAdjustmentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAdjustmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderAdjustmentType> result = orderAdjustmentTypeService.partialUpdate(orderAdjustmentType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAdjustmentType.getId().toString())
        );
    }

    /**
     * {@code GET  /order-adjustment-types} : get all the Order Adjustment Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Adjustment Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderAdjustmentType>> getAllOrderAdjustmentTypes(OrderAdjustmentTypeCriteria criteria) {
        LOG.debug("REST request to get OrderAdjustmentTypes by criteria: {}", criteria);

        List<OrderAdjustmentType> entityList = orderAdjustmentTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /order-adjustment-types/count} : count all the orderAdjustmentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderAdjustmentTypes(OrderAdjustmentTypeCriteria criteria) {
        LOG.debug("REST request to count OrderAdjustmentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderAdjustmentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-adjustment-types/:id} : get the "id" orderAdjustmentType.
     *
     * @param id the id of the orderAdjustmentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderAdjustmentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderAdjustmentType> getOrderAdjustmentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderAdjustmentType : {}", id);
        Optional<OrderAdjustmentType> orderAdjustmentType = orderAdjustmentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderAdjustmentType);
    }

    /**
     * {@code DELETE  /order-adjustment-types/:id} : delete the "id" orderAdjustmentType.
     *
     * @param id the id of the orderAdjustmentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderAdjustmentType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderAdjustmentType : {}", id);
        orderAdjustmentTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
