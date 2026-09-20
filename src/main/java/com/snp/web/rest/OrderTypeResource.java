package com.snp.web.rest;

import com.snp.domain.OrderType;
import com.snp.repository.OrderTypeRepository;
import com.snp.service.OrderTypeQueryService;
import com.snp.service.OrderTypeService;
import com.snp.service.criteria.OrderTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderType}.
 */
@RestController
@RequestMapping("/api/order-types")
public class OrderTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderTypeResource.class);

    private static final String ENTITY_NAME = "orderType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderTypeService orderTypeService;

    private final OrderTypeRepository orderTypeRepository;

    private final OrderTypeQueryService orderTypeQueryService;

    public OrderTypeResource(
        OrderTypeService orderTypeService,
        OrderTypeRepository orderTypeRepository,
        OrderTypeQueryService orderTypeQueryService
    ) {
        this.orderTypeService = orderTypeService;
        this.orderTypeRepository = orderTypeRepository;
        this.orderTypeQueryService = orderTypeQueryService;
    }

    /**
     * {@code POST  /order-types} : Create a new orderType.
     *
     * @param orderType the orderType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderType, or with status {@code 400 (Bad Request)} if the orderType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderType> createOrderType(@Valid @RequestBody OrderType orderType) throws URISyntaxException {
        LOG.debug("REST request to save OrderType : {}", orderType);
        if (orderType.getId() != null) {
            throw new BadRequestAlertException("A new orderType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderType = orderTypeService.save(orderType);
        return ResponseEntity.created(new URI("/api/order-types/" + orderType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderType.getId().toString()))
            .body(orderType);
    }

    /**
     * {@code PUT  /order-types/:id} : Updates an existing orderType.
     *
     * @param id the id of the orderType to save.
     * @param orderType the orderType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderType,
     * or with status {@code 400 (Bad Request)} if the orderType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderType> updateOrderType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderType orderType
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderType : {}, {}", id, orderType);
        if (orderType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderType = orderTypeService.update(orderType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderType.getId().toString()))
            .body(orderType);
    }

    /**
     * {@code PATCH  /order-types/:id} : Partial updates given fields of an existing orderType, field will ignore if it is null
     *
     * @param id the id of the orderType to save.
     * @param orderType the orderType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderType,
     * or with status {@code 400 (Bad Request)} if the orderType is not valid,
     * or with status {@code 404 (Not Found)} if the orderType is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderType> partialUpdateOrderType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderType orderType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderType : {}, {}", id, orderType);
        if (orderType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderType> result = orderTypeService.partialUpdate(orderType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderType.getId().toString())
        );
    }

    /**
     * {@code GET  /order-types} : get all the Order Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderType>> getAllOrderTypes(OrderTypeCriteria criteria) {
        LOG.debug("REST request to get OrderTypes by criteria: {}", criteria);

        List<OrderType> entityList = orderTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /order-types/count} : count all the orderTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderTypes(OrderTypeCriteria criteria) {
        LOG.debug("REST request to count OrderTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-types/:id} : get the "id" orderType.
     *
     * @param id the id of the orderType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderType> getOrderType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderType : {}", id);
        Optional<OrderType> orderType = orderTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderType);
    }

    /**
     * {@code DELETE  /order-types/:id} : delete the "id" orderType.
     *
     * @param id the id of the orderType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderType : {}", id);
        orderTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
