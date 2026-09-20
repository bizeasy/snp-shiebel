package com.snp.web.rest;

import com.snp.domain.OrderItemType;
import com.snp.repository.OrderItemTypeRepository;
import com.snp.service.OrderItemTypeQueryService;
import com.snp.service.OrderItemTypeService;
import com.snp.service.criteria.OrderItemTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderItemType}.
 */
@RestController
@RequestMapping("/api/order-item-types")
public class OrderItemTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemTypeResource.class);

    private static final String ENTITY_NAME = "orderItemType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderItemTypeService orderItemTypeService;

    private final OrderItemTypeRepository orderItemTypeRepository;

    private final OrderItemTypeQueryService orderItemTypeQueryService;

    public OrderItemTypeResource(
        OrderItemTypeService orderItemTypeService,
        OrderItemTypeRepository orderItemTypeRepository,
        OrderItemTypeQueryService orderItemTypeQueryService
    ) {
        this.orderItemTypeService = orderItemTypeService;
        this.orderItemTypeRepository = orderItemTypeRepository;
        this.orderItemTypeQueryService = orderItemTypeQueryService;
    }

    /**
     * {@code POST  /order-item-types} : Create a new orderItemType.
     *
     * @param orderItemType the orderItemType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderItemType, or with status {@code 400 (Bad Request)} if the orderItemType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderItemType> createOrderItemType(@Valid @RequestBody OrderItemType orderItemType) throws URISyntaxException {
        LOG.debug("REST request to save OrderItemType : {}", orderItemType);
        if (orderItemType.getId() != null) {
            throw new BadRequestAlertException("A new orderItemType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderItemType = orderItemTypeService.save(orderItemType);
        return ResponseEntity.created(new URI("/api/order-item-types/" + orderItemType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderItemType.getId().toString()))
            .body(orderItemType);
    }

    /**
     * {@code PUT  /order-item-types/:id} : Updates an existing orderItemType.
     *
     * @param id the id of the orderItemType to save.
     * @param orderItemType the orderItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemType,
     * or with status {@code 400 (Bad Request)} if the orderItemType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemType> updateOrderItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderItemType orderItemType
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderItemType : {}, {}", id, orderItemType);
        if (orderItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderItemType = orderItemTypeService.update(orderItemType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemType.getId().toString()))
            .body(orderItemType);
    }

    /**
     * {@code PATCH  /order-item-types/:id} : Partial updates given fields of an existing orderItemType, field will ignore if it is null
     *
     * @param id the id of the orderItemType to save.
     * @param orderItemType the orderItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemType,
     * or with status {@code 400 (Bad Request)} if the orderItemType is not valid,
     * or with status {@code 404 (Not Found)} if the orderItemType is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemType> partialUpdateOrderItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderItemType orderItemType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderItemType : {}, {}", id, orderItemType);
        if (orderItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderItemType> result = orderItemTypeService.partialUpdate(orderItemType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemType.getId().toString())
        );
    }

    /**
     * {@code GET  /order-item-types} : get all the Order Item Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Item Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderItemType>> getAllOrderItemTypes(OrderItemTypeCriteria criteria) {
        LOG.debug("REST request to get OrderItemTypes by criteria: {}", criteria);

        List<OrderItemType> entityList = orderItemTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /order-item-types/count} : count all the orderItemTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderItemTypes(OrderItemTypeCriteria criteria) {
        LOG.debug("REST request to count OrderItemTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderItemTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-item-types/:id} : get the "id" orderItemType.
     *
     * @param id the id of the orderItemType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderItemType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemType> getOrderItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderItemType : {}", id);
        Optional<OrderItemType> orderItemType = orderItemTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemType);
    }

    /**
     * {@code DELETE  /order-item-types/:id} : delete the "id" orderItemType.
     *
     * @param id the id of the orderItemType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderItemType : {}", id);
        orderItemTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
