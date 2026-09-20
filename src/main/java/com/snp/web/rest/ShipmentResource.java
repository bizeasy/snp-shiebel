package com.snp.web.rest;

import com.snp.domain.Shipment;
import com.snp.repository.ShipmentRepository;
import com.snp.service.ShipmentQueryService;
import com.snp.service.ShipmentService;
import com.snp.service.criteria.ShipmentCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.snp.domain.Shipment}.
 */
@RestController
@RequestMapping("/api/shipments")
public class ShipmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentResource.class);

    private static final String ENTITY_NAME = "shipment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ShipmentService shipmentService;

    private final ShipmentRepository shipmentRepository;

    private final ShipmentQueryService shipmentQueryService;

    public ShipmentResource(
        ShipmentService shipmentService,
        ShipmentRepository shipmentRepository,
        ShipmentQueryService shipmentQueryService
    ) {
        this.shipmentService = shipmentService;
        this.shipmentRepository = shipmentRepository;
        this.shipmentQueryService = shipmentQueryService;
    }

    /**
     * {@code POST  /shipments} : Create a new shipment.
     *
     * @param shipment the shipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipment, or with status {@code 400 (Bad Request)} if the shipment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment) throws URISyntaxException {
        LOG.debug("REST request to save Shipment : {}", shipment);
        if (shipment.getId() != null) {
            throw new BadRequestAlertException("A new shipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipment = shipmentService.save(shipment);
        return ResponseEntity.created(new URI("/api/shipments/" + shipment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipment.getId().toString()))
            .body(shipment);
    }

    /**
     * {@code PUT  /shipments/:id} : Updates an existing shipment.
     *
     * @param id the id of the shipment to save.
     * @param shipment the shipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipment,
     * or with status {@code 400 (Bad Request)} if the shipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Shipment> updateShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Shipment shipment
    ) throws URISyntaxException {
        LOG.debug("REST request to update Shipment : {}, {}", id, shipment);
        if (shipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipment = shipmentService.update(shipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipment.getId().toString()))
            .body(shipment);
    }

    /**
     * {@code PATCH  /shipments/:id} : Partial updates given fields of an existing shipment, field will ignore if it is null
     *
     * @param id the id of the shipment to save.
     * @param shipment the shipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipment,
     * or with status {@code 400 (Bad Request)} if the shipment is not valid,
     * or with status {@code 404 (Not Found)} if the shipment is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Shipment> partialUpdateShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Shipment shipment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Shipment : {}, {}", id, shipment);
        if (shipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Shipment> result = shipmentService.partialUpdate(shipment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipment.getId().toString())
        );
    }

    /**
     * {@code GET  /shipments} : get all the Shipments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Shipments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Shipment>> getAllShipments(
        ShipmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Shipments by criteria: {}", criteria);

        Page<Shipment> page = shipmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipments/count} : count all the shipments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipments(ShipmentCriteria criteria) {
        LOG.debug("REST request to count Shipments by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipments/:id} : get the "id" shipment.
     *
     * @param id the id of the shipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Shipment : {}", id);
        Optional<Shipment> shipment = shipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipment);
    }

    /**
     * {@code DELETE  /shipments/:id} : delete the "id" shipment.
     *
     * @param id the id of the shipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Shipment : {}", id);
        shipmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
