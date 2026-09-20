package com.snp.web.rest;

import com.snp.domain.OrderContactMech;
import com.snp.repository.OrderContactMechRepository;
import com.snp.service.OrderContactMechQueryService;
import com.snp.service.OrderContactMechService;
import com.snp.service.criteria.OrderContactMechCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderContactMech}.
 */
@RestController
@RequestMapping("/api/order-contact-meches")
public class OrderContactMechResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderContactMechResource.class);

    private static final String ENTITY_NAME = "orderContactMech";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderContactMechService orderContactMechService;

    private final OrderContactMechRepository orderContactMechRepository;

    private final OrderContactMechQueryService orderContactMechQueryService;

    public OrderContactMechResource(
        OrderContactMechService orderContactMechService,
        OrderContactMechRepository orderContactMechRepository,
        OrderContactMechQueryService orderContactMechQueryService
    ) {
        this.orderContactMechService = orderContactMechService;
        this.orderContactMechRepository = orderContactMechRepository;
        this.orderContactMechQueryService = orderContactMechQueryService;
    }

    /**
     * {@code POST  /order-contact-meches} : Create a new orderContactMech.
     *
     * @param orderContactMech the orderContactMech to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderContactMech, or with status {@code 400 (Bad Request)} if the orderContactMech already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderContactMech> createOrderContactMech(@Valid @RequestBody OrderContactMech orderContactMech)
        throws URISyntaxException {
        LOG.debug("REST request to save OrderContactMech : {}", orderContactMech);
        if (orderContactMech.getId() != null) {
            throw new BadRequestAlertException("A new orderContactMech cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderContactMech = orderContactMechService.save(orderContactMech);
        return ResponseEntity.created(new URI("/api/order-contact-meches/" + orderContactMech.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderContactMech.getId().toString()))
            .body(orderContactMech);
    }

    /**
     * {@code PUT  /order-contact-meches/:id} : Updates an existing orderContactMech.
     *
     * @param id the id of the orderContactMech to save.
     * @param orderContactMech the orderContactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderContactMech,
     * or with status {@code 400 (Bad Request)} if the orderContactMech is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderContactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderContactMech> updateOrderContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderContactMech orderContactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderContactMech : {}, {}", id, orderContactMech);
        if (orderContactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderContactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderContactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderContactMech = orderContactMechService.update(orderContactMech);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderContactMech.getId().toString()))
            .body(orderContactMech);
    }

    /**
     * {@code PATCH  /order-contact-meches/:id} : Partial updates given fields of an existing orderContactMech, field will ignore if it is null
     *
     * @param id the id of the orderContactMech to save.
     * @param orderContactMech the orderContactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderContactMech,
     * or with status {@code 400 (Bad Request)} if the orderContactMech is not valid,
     * or with status {@code 404 (Not Found)} if the orderContactMech is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderContactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderContactMech> partialUpdateOrderContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderContactMech orderContactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderContactMech : {}, {}", id, orderContactMech);
        if (orderContactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderContactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderContactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderContactMech> result = orderContactMechService.partialUpdate(orderContactMech);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderContactMech.getId().toString())
        );
    }

    /**
     * {@code GET  /order-contact-meches} : get all the Order Contact Meches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Contact Meches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderContactMech>> getAllOrderContactMeches(
        OrderContactMechCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderContactMeches by criteria: {}", criteria);

        Page<OrderContactMech> page = orderContactMechQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-contact-meches/count} : count all the orderContactMeches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderContactMeches(OrderContactMechCriteria criteria) {
        LOG.debug("REST request to count OrderContactMeches by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderContactMechQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-contact-meches/:id} : get the "id" orderContactMech.
     *
     * @param id the id of the orderContactMech to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderContactMech, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderContactMech> getOrderContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderContactMech : {}", id);
        Optional<OrderContactMech> orderContactMech = orderContactMechService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderContactMech);
    }

    /**
     * {@code DELETE  /order-contact-meches/:id} : delete the "id" orderContactMech.
     *
     * @param id the id of the orderContactMech to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderContactMech : {}", id);
        orderContactMechService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
