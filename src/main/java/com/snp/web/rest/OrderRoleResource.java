package com.snp.web.rest;

import com.snp.domain.OrderRole;
import com.snp.repository.OrderRoleRepository;
import com.snp.service.OrderRoleQueryService;
import com.snp.service.OrderRoleService;
import com.snp.service.criteria.OrderRoleCriteria;
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
 * REST controller for managing {@link com.snp.domain.OrderRole}.
 */
@RestController
@RequestMapping("/api/order-roles")
public class OrderRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderRoleResource.class);

    private static final String ENTITY_NAME = "orderRole";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final OrderRoleService orderRoleService;

    private final OrderRoleRepository orderRoleRepository;

    private final OrderRoleQueryService orderRoleQueryService;

    public OrderRoleResource(
        OrderRoleService orderRoleService,
        OrderRoleRepository orderRoleRepository,
        OrderRoleQueryService orderRoleQueryService
    ) {
        this.orderRoleService = orderRoleService;
        this.orderRoleRepository = orderRoleRepository;
        this.orderRoleQueryService = orderRoleQueryService;
    }

    /**
     * {@code POST  /order-roles} : Create a new orderRole.
     *
     * @param orderRole the orderRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderRole, or with status {@code 400 (Bad Request)} if the orderRole already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderRole> createOrderRole(@Valid @RequestBody OrderRole orderRole) throws URISyntaxException {
        LOG.debug("REST request to save OrderRole : {}", orderRole);
        if (orderRole.getId() != null) {
            throw new BadRequestAlertException("A new orderRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderRole = orderRoleService.save(orderRole);
        return ResponseEntity.created(new URI("/api/order-roles/" + orderRole.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderRole.getId().toString()))
            .body(orderRole);
    }

    /**
     * {@code PUT  /order-roles/:id} : Updates an existing orderRole.
     *
     * @param id the id of the orderRole to save.
     * @param orderRole the orderRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderRole,
     * or with status {@code 400 (Bad Request)} if the orderRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderRole> updateOrderRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderRole orderRole
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderRole : {}, {}", id, orderRole);
        if (orderRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderRole = orderRoleService.update(orderRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderRole.getId().toString()))
            .body(orderRole);
    }

    /**
     * {@code PATCH  /order-roles/:id} : Partial updates given fields of an existing orderRole, field will ignore if it is null
     *
     * @param id the id of the orderRole to save.
     * @param orderRole the orderRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderRole,
     * or with status {@code 400 (Bad Request)} if the orderRole is not valid,
     * or with status {@code 404 (Not Found)} if the orderRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderRole> partialUpdateOrderRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderRole orderRole
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update OrderRole : {}, {}", id, orderRole);
        if (orderRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderRole> result = orderRoleService.partialUpdate(orderRole);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderRole.getId().toString())
        );
    }

    /**
     * {@code GET  /order-roles} : get all the Order Roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Order Roles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderRole>> getAllOrderRoles(
        OrderRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderRoles by criteria: {}", criteria);

        Page<OrderRole> page = orderRoleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-roles/count} : count all the orderRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderRoles(OrderRoleCriteria criteria) {
        LOG.debug("REST request to count OrderRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-roles/:id} : get the "id" orderRole.
     *
     * @param id the id of the orderRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderRole> getOrderRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderRole : {}", id);
        Optional<OrderRole> orderRole = orderRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderRole);
    }

    /**
     * {@code DELETE  /order-roles/:id} : delete the "id" orderRole.
     *
     * @param id the id of the orderRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderRole : {}", id);
        orderRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
