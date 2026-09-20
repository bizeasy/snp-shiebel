package com.snp.web.rest;

import com.snp.domain.ShipmentStatus;
import com.snp.repository.ShipmentStatusRepository;
import com.snp.service.ShipmentStatusQueryService;
import com.snp.service.ShipmentStatusService;
import com.snp.service.criteria.ShipmentStatusCriteria;
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
 * REST controller for managing {@link com.snp.domain.ShipmentStatus}.
 */
@RestController
@RequestMapping("/api/shipment-statuses")
public class ShipmentStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentStatusResource.class);

    private static final String ENTITY_NAME = "shipmentStatus";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShipmentStatusService shipmentStatusService;

    private final ShipmentStatusRepository shipmentStatusRepository;

    private final ShipmentStatusQueryService shipmentStatusQueryService;

    public ShipmentStatusResource(
        ShipmentStatusService shipmentStatusService,
        ShipmentStatusRepository shipmentStatusRepository,
        ShipmentStatusQueryService shipmentStatusQueryService
    ) {
        this.shipmentStatusService = shipmentStatusService;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.shipmentStatusQueryService = shipmentStatusQueryService;
    }

    /**
     * {@code POST  /shipment-statuses} : Create a new shipmentStatus.
     *
     * @param shipmentStatus the shipmentStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentStatus, or with status {@code 400 (Bad Request)} if the shipmentStatus already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentStatus> createShipmentStatus(@Valid @RequestBody ShipmentStatus shipmentStatus)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentStatus : {}", shipmentStatus);
        if (shipmentStatus.getId() != null) {
            throw new BadRequestAlertException("A new shipmentStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentStatus = shipmentStatusService.save(shipmentStatus);
        return ResponseEntity.created(new URI("/api/shipment-statuses/" + shipmentStatus.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentStatus.getId().toString()))
            .body(shipmentStatus);
    }

    /**
     * {@code PUT  /shipment-statuses/:id} : Updates an existing shipmentStatus.
     *
     * @param id the id of the shipmentStatus to save.
     * @param shipmentStatus the shipmentStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentStatus,
     * or with status {@code 400 (Bad Request)} if the shipmentStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentStatus> updateShipmentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentStatus shipmentStatus
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentStatus : {}, {}", id, shipmentStatus);
        if (shipmentStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentStatus = shipmentStatusService.update(shipmentStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentStatus.getId().toString()))
            .body(shipmentStatus);
    }

    /**
     * {@code PATCH  /shipment-statuses/:id} : Partial updates given fields of an existing shipmentStatus, field will ignore if it is null
     *
     * @param id the id of the shipmentStatus to save.
     * @param shipmentStatus the shipmentStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentStatus,
     * or with status {@code 400 (Bad Request)} if the shipmentStatus is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentStatus> partialUpdateShipmentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentStatus shipmentStatus
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ShipmentStatus : {}, {}", id, shipmentStatus);
        if (shipmentStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentStatus> result = shipmentStatusService.partialUpdate(shipmentStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-statuses} : get all the Shipment Statuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shipment Statuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentStatus>> getAllShipmentStatuses(
        ShipmentStatusCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentStatuses by criteria: {}", criteria);

        Page<ShipmentStatus> page = shipmentStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-statuses/count} : count all the shipmentStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentStatuses(ShipmentStatusCriteria criteria) {
        LOG.debug("REST request to count ShipmentStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-statuses/:id} : get the "id" shipmentStatus.
     *
     * @param id the id of the shipmentStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentStatus> getShipmentStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentStatus : {}", id);
        Optional<ShipmentStatus> shipmentStatus = shipmentStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentStatus);
    }

    /**
     * {@code DELETE  /shipment-statuses/:id} : delete the "id" shipmentStatus.
     *
     * @param id the id of the shipmentStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentStatus : {}", id);
        shipmentStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
